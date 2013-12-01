var STARTED_STATUS = 'Started';
var STOPPED_STATUS = 'Stopped';
var SUBSCRIBED_SOCKET;

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
        var json = jQuery.parseJSON(response.responseBody);
        setStatusAndCounterByResponse(json);
    };

    SUBSCRIBED_SOCKET = socket.subscribe(request);

    bindEvents();
});

/**
 * Sets status label and counter
 *
 * @param json, pomodoro response
 */
function setStatusAndCounterByResponse(singleStateResponse) {
    var userId = singleStateResponse['userId'];
    var state = singleStateResponse['state'];
    var minute = state['minute'];
    var status = state['status'];

    if (userId != getUserId()) {
        appendAndUpdateUserContainer(userId, minute);
    }

    var $timeLabel = $('.' + userId + ' .timeLabel');
    var $startButton = $('.' + userId + ' .startButton');

    var $stopButton = $('.' + userId + ' .stopButton');
    var countdown = $timeLabel.data('countdown');


    if (status == STARTED_STATUS && minute) {
        $startButton.attr('disabled', 'disabled');
        $stopButton.removeAttr('disabled');
        var updateTime = parseInt(state['updateTime']);
        var timeDifference = parseInt(singleStateResponse['currentServerTime']) - new Date().getTime();
        minute = parseInt(minute);
        if (updateTime) {
            var date = new Date(updateTime + (1000 * 60 * minute) - timeDifference);
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
 * Appends new user container elements and updates icon
 *
 * @param userId
 */
function appendAndUpdateUserContainer(userId, minute) {
    if ($('#usersContainer .' + userId).length == 0) {
        var $container = $('<div></div>');
        $container.addClass('col-xs-6');
        $container.addClass(userId);

        var $icon = $('<span></span>');
        $icon.addClass('glyphicon');

        var $header = $('<h4></h4>');

        var $timeLabel = $('<h5></h5>');
        $timeLabel.addClass('timeLabel');
        $timeLabel.text('00:00');

        var $usersContainer = $('#usersContainer');
        $icon.appendTo($header);
        $('<span> ' + userId + '</span>').appendTo($header);
        $header.appendTo($container);
        $timeLabel.appendTo($container);
        $container.appendTo($usersContainer);
    }

    var $icon = $('#usersContainer .' + userId).find('.glyphicon');
    $icon.attr('class', 'glyphicon')
    if (minute) {
        if (minute == "25")
            $icon.addClass('glyphicon-send');
        if (minute == "50")
            $icon.addClass('glyphicon-fire');
        if (minute == "15" || minute == "5")
            $icon.addClass('glyphicon-pause');
    } else {
        $icon.addClass('glyphicon-stop');
    }
}

/**
 * Binds events to related html element
 */
function bindEvents() {
    var $startButton = $('.jumbotron .startButton');
    var $stopButton = $('.jumbotron .stopButton');

    $startButton.click(function () {
        var $that = $(this);
        pushStartedMessage($that.data('minute'));
        $startButton.attr('disabled', 'disabled');
        $stopButton.removeAttr('disabled');
    });

    $stopButton.click(function () {
        pushStoppedMessage();
        $(this).attr('disabled', 'disabled');
        $startButton.removeAttr('disabled');
    });
};

/**
 * Pushes "Started" message
 *
 * @param minute, minute for countdown
 */
function pushStartedMessage(minute) {
    SUBSCRIBED_SOCKET.push(jQuery.stringifyJSON({ authKey: getAuthKey(), minute: minute, status: STARTED_STATUS }));
}

/**
 * Pushes "Stopped" message
 */
function pushStoppedMessage() {
    SUBSCRIBED_SOCKET.push(jQuery.stringifyJSON({ authKey: getAuthKey(), status: STOPPED_STATUS }));
}