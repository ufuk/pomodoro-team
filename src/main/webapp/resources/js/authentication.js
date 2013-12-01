/**
 * Authentication checker
 */
(function authenticate() {
    var authKey = $.cookie('authKey');

    if (authKey) {
        $.ajax({
            type: 'POST', dataType: 'json', contentType: 'text/plain; charset=utf-8',
            url: document.location.toString() + 'pomodoro/checkAuthKey',
            data: authKey,
            success: function (json) {
                var authenticated = json['authenticated'];
                if (authenticated == 'false') {
                    showLogInPopup();
                } else {
                    updateAuthKey(authKey);
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
            $.ajax({
                type: 'POST', dataType: 'json', contentType: 'application/json; charset=utf-8',
                url: document.location.toString() + 'pomodoro/logIn',
                data: jQuery.stringifyJSON({userId: $usernameInput.val(), password: $passwordInput.val()}),
                success: function (json) {
                    var authenticated = json['authenticated'];
                    if (authenticated == 'true') {
                        updateAuthKey(json['authKey']);
                        hideLogInPopup();
                    }
                }
            });
        }
    });
    $('#logInPopup').modal('show');
};

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
    $.cookie('authKey', authKey, { expires: 7, path: '/' });
}