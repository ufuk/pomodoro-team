var STARTED_STATUS = 'Started';
var STOPPED_STATUS = 'Stopped';
var SUBSCRIBED_SOCKET;
var BASE_URL = document.location.origin + document.location.pathname;

/**
 * Constructor
 */
$(function () {
    "use strict";

    var socket = $.atmosphere;

    var request = {
        url: BASE_URL + 'pomodoro',
        contentType: "application/json",
        logLevel: 'debug',
        transport: 'websocket',
        trackMessageLength: true,
        fallbackTransport: 'long-polling'
    };

    request.onMessage = function (response) {
        var json = jQuery.parseJSON(response.responseBody);
        if (getAuthKey()) {
            setStatusAndCounterByResponse(json);
        }
    };

    SUBSCRIBED_SOCKET = socket.subscribe(request);

    bindEvents();

    initBootstrapFunctionality();
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
        $container.addClass('col-xs-3');
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
        $('.tooltip').remove(); // FIXME because of firefox (or bootstrap), when button clicked removes tooltips manually
    });

    $stopButton.click(function () {
        pushStoppedMessage();
        $(this).attr('disabled', 'disabled');
        $startButton.removeAttr('disabled');
    });

    $('#logoutButton').click(function () {
        logout();
    });

    $('#usernameInput').keypress(function (event) {
        var allowedChars = [8, 13, 9, 37, 39];
        const charCode = event.keyCode || event.which;
        var typedChar = String.fromCharCode(charCode).replace(/[^a-z0-9]/gi, '');
        if (typedChar == "" && allowedChars.lastIndexOf(charCode) == -1) {
            event.preventDefault();
        }
    }).bind('paste', function (event) {
            event.preventDefault();
        });

    $('#usernameInput, #passwordInput').keyup(function (event) {
        if ((event.keyCode || event.which) == 13) {
            $('#logInButton').click();
        }
    });
};

/**
 * Initializes Bootstrap's functionalities (such as tooltip, popover)
 */
function initBootstrapFunctionality() {
    $('.tooltipMe').tooltip();
}

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