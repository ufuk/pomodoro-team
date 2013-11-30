var STARTED_STATUS = 'Started';
var STOPPED_STATUS = 'Stopped';
var subscribedSocket;

/**
 * Constructor
 */
$(function () {
    "use strict";

    var socket = $.atmosphere;

    var request = {
        url: document.location.toString() + 'pomodoro',
        contentType: "application/json",
        logLevel: 'debug',
        transport: 'websocket',
        trackMessageLength: true,
        fallbackTransport: 'long-polling'
    };

    request.onMessage = function (response) {
        setStatusAndCounterByResponse(jQuery.parseJSON(response.responseBody));
    };

    subscribedSocket = socket.subscribe(request);

    bindEvents();

    (function initCurrentStatus() {
        $.getJSON(document.location.toString() + 'pomodoro/currentStatus', function (response) {
            setStatusAndCounterByResponse(response);
        });
    })();
});

/**
 * Set status label and counter
 *
 * @param json, pomodoro response
 */
function setStatusAndCounterByResponse(json) {
    var $timeLabel = $('#timeLabel');
    var $startButton = startButton();
    var $stopButton = stopButton();

    var countdown = $timeLabel.data('countdown');
    var minute = json['minute'];
    var status = json['status'];
    if (status == STARTED_STATUS && minute) {
        $startButton.attr('disabled', 'disabled');
        $stopButton.removeAttr('disabled');
        var updateTime = parseInt(json['updateTime']);
        minute = parseInt(minute);
        if (updateTime) {
            var date = new Date(updateTime + (1000 * 60 * minute));
            if (countdown) {
                $timeLabel.data('countdown').update(date).start();
            } else {
                $timeLabel.countdown({
                    date: date,
                    render: function (data) {
                        $(this.el).text(this.leadingZeros(data.min, 2) + ":" + this.leadingZeros(data.sec, 2));
                    },
                    onEnd: function () {
                        pushStoppedMessage();
                    }
                });
            }
        }
    } else if (status == STOPPED_STATUS) {
        $stopButton.attr('disabled', 'disabled');
        $startButton.removeAttr('disabled');
        if (countdown) {
            countdown.stop();
        }
        $timeLabel.text('00:00');
    }
};

/**
 * Bind events to related html element
 */
function bindEvents() {
    var $startButton = startButton();
    var $stopButton = stopButton();

    $startButton.click(function () {
        var $that = $(this);
        pushStartedMessage($that.data('minute'));
        $that.attr('disabled', 'disabled');
        $stopButton.removeAttr('disabled');
    });

    $stopButton.click(function () {
        pushStoppedMessage();
        $(this).attr('disabled', 'disabled');
        $startButton.removeAttr('disabled');
    });
};

/**
 * Getter for start button element
 *
 * @returns {*|jQuery|HTMLElement}
 */
function startButton() {
    return $('.startButton');
};

/**
 * Getter for stop button element
 *
 * @returns {*|jQuery|HTMLElement}
 */
function stopButton() {
    return $('#stopButton');
};

/**
 * Push "Started" message
 *
 * @param minute, minute for countdown
 */
function pushStartedMessage(minute) {
    subscribedSocket.push(jQuery.stringifyJSON({ developerId: '', minute: minute, status: STARTED_STATUS }));
}

/**
 * Push "Stopped" message
 */
function pushStoppedMessage() {
    subscribedSocket.push(jQuery.stringifyJSON({ developerId: '', status: STOPPED_STATUS }));
}