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

    var subscribedSocket = socket.subscribe(request);

    bindEvents(subscribedSocket);

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
    var status = json['pomodoroStatus'];

    var $timeLabel = $('#timeLabel');
    var $startButton = startButton();
    var $stopButton = stopButton();

    var countdown = $timeLabel.data('countdown');
    if (status == 'Started') {
        $startButton.attr('disabled', 'disabled');
        $stopButton.removeAttr('disabled');
        var updateTime = parseInt(json['statusUpdateTime']);
        var now = parseInt(json['now']);
        if (updateTime && now) {
            var date = new Date(updateTime + (1000 * 60 * 25));
            if (countdown) {
                $timeLabel.data('countdown').update(date).start();
            } else {
                $timeLabel.countdown({
                    date: date,
                    render: function (data) {
                        $(this.el).text(this.leadingZeros(data.min, 2) + ":" + this.leadingZeros(data.sec, 2));
                    },
                    onEnd: function () {
                        console.log("Countdown ended!");
                    }
                });
            }
        }
    } else if (status == 'Stopped') {
        $stopButton.attr('disabled', 'disabled');
        $startButton.removeAttr('disabled');
        if (countdown) {
            countdown.stop();
        }
        $timeLabel.text("25:00");
    }
};

/**
 * Bind events to related html element
 *
 * @param subscribedSocket
 */
function bindEvents(subscribedSocket) {
    var $startButton = startButton();
    var $stopButton = stopButton();

    $startButton.click(function () {
        subscribedSocket.push(jQuery.stringifyJSON({ developerId: "", pomodoroStatus: "Started" }));
        $(this).attr('disabled', 'disabled');
        $stopButton.removeAttr('disabled');
    });

    $stopButton.click(function () {
        subscribedSocket.push(jQuery.stringifyJSON({ developerId: "", pomodoroStatus: "Stopped" }));
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
    return $('#startButton');
};

/**
 * Getter for stop button element
 *
 * @returns {*|jQuery|HTMLElement}
 */
function stopButton() {
    return $('#stopButton');
};