package com.ufukuzun.pomodoroteam.service;

import com.ufukuzun.pomodoroteam.db.PomodoroDB;
import com.ufukuzun.pomodoroteam.model.AuthenticationResponse;
import com.ufukuzun.pomodoroteam.model.LogInRequest;
import com.ufukuzun.pomodoroteam.model.User;

public class AuthenticationService {

    private PomodoroDB pomodoroDB = PomodoroDB.connect();

    public AuthenticationResponse logIn(LogInRequest request) {
        User user = pomodoroDB.findUserByUserIdAndPassword(request.getUserId(), request.getPassword());
        if (user != null) {
            user.generateAuthKey();
            return AuthenticationResponse.createFor(user);
        }
        user = User.createFor(request);
        pomodoroDB.persistNewUser(user);

        return AuthenticationResponse.createFor(user);
    }

    public AuthenticationResponse checkAuthKey(String authKey) {
        if (authKey != null) {
            User user = pomodoroDB.findUserByAuthKey(authKey);
            if (user != null) {
                return AuthenticationResponse.createFor(user);
            }
        }
        return AuthenticationResponse.createNotAuthenticatedResponse();
    }

}
