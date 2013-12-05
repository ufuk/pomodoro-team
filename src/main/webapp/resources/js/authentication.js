var AUTHKEY_COOKIE_KEY = 'authKey';
var USERID_COOKIE_KEY = 'userId';

/**
 * Authentication checker
 */
(function authenticate() {
    var authKey = getAuthKey();

    if (authKey) {
        $.ajax({
            type: 'POST', dataType: 'json', contentType: 'text/plain; charset=utf-8',
            url: BASE_URL + 'pomodoro/checkAuthKey',
            data: authKey,
            success: function (json) {
                var authenticated = json['authenticated'];
                if (authenticated == 'false') {
                    clearCookies();
                    showLogInPopup();
                } else {
                    addCurrentUserIdAsClassToJumbotronContainer(json['userId']);
                    initCurrentState();
                }
            }
        });
    } else {
        showLogInPopup();
    }
})();

/**
 * Shows log in popup
 */
function showLogInPopup() {
    $('#logInButton').click(function () {
        var $usernameInput = $('#usernameInput');
        var $passwordInput = $('#passwordInput');

        if ($usernameInput[0].checkValidity() && $passwordInput[0].checkValidity()) {
            var userId = $usernameInput.val();
            var password = $passwordInput.val();
            $.ajax({
                type: 'POST', dataType: 'json', contentType: 'application/json; charset=utf-8',
                url: BASE_URL + 'pomodoro/logIn',
                data: jQuery.stringifyJSON({userId: userId, password: password}),
                success: function (json) {
                    var authenticated = json['authenticated'];
                    if (authenticated == 'true') {
                        updateAuthKey(json['authKey']);
                        updateUserId(userId);
                        hideLogInPopup();
                        addCurrentUserIdAsClassToJumbotronContainer(userId);
                        initCurrentState();
                    }
                }
            });
        }
    });
    $('#logInPopup').modal({keyboard: false, backdrop: 'static'});
    $('#logInPopup').modal('show');
};

/**
 * Logouts
 */
function logout() {
    pushStoppedMessage();
    clearCookies();
    document.location = BASE_URL;
}

/**
 * Clear cookies
 */
function clearCookies() {
    $.removeCookie(AUTHKEY_COOKIE_KEY, { path: '/' });
    $.removeCookie(USERID_COOKIE_KEY, { path: '/' });
}

/**
 * Adds current user's userId as class to Jumbotron container
 *
 * @param userId, current user's userId
 */
function addCurrentUserIdAsClassToJumbotronContainer(userId) {
    $('.jumbotron').addClass(userId);
}

/**
 * Hides log in popup
 */
function hideLogInPopup() {
    $('#logInPopup').modal('hide');
};

/**
 * Updates authentication key on cookie
 *
 * @param authKey, last authentication key for user
 */
function updateAuthKey(authKey) {
    $.cookie(AUTHKEY_COOKIE_KEY, authKey, { expires: 365, path: '/' });
};

/**
 * Gets current authentication key from cookie
 */
function getAuthKey() {
    var authKey = $.cookie('authKey');
    return authKey;
};

/**
 * Updates username on cookie
 *
 * @param userId
 */
function updateUserId(userId) {
    $.cookie(USERID_COOKIE_KEY, userId, { expires: 365, path: '/' });
};

/**
 * Gets current username key from cookie
 */
function getUserId() {
    var userId = $.cookie(USERID_COOKIE_KEY);
    return userId;
};

/**
 * Inits current pomodoro state for all users
 */
function initCurrentState() {
    toggleUserMenu();

    $.ajax({
        type: 'POST', dataType: 'json', contentType: 'text/plain; charset=utf-8',
        url: BASE_URL + 'pomodoro/currentStatus',
        data: getAuthKey(),
        success: function (jsonResponse) {
            var singleStateResponse = jsonResponse['singleStateResponse'];
            var array = singleStateResponse instanceof Array ? singleStateResponse : new Array(singleStateResponse);
            array.forEach(function (each) {
                setStatusAndCounterByResponse(each);
            });
        }
    });
};

/**
 * Shows logout link if authentication key exist, hides if not
 */
function toggleUserMenu() {
    var $userMenu = $('#userMenu');
    if (getAuthKey()) {
        $('#usernameLabel').text('hello ' + getUserId() + ' | ');
        $userMenu.show();
    } else {
        $userMenu.hide();
    }
}