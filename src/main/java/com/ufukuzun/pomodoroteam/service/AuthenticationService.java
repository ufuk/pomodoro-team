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
            pomodoroDB.updateUser(user);
            return AuthenticationResponse.createFor(user);
        }
        user = User.createFor(request);
        pomodoroDB.persistNewUser(user);

        return AuthenticationResponse.createFor(user);
    }

    public AuthenticationResponse checkAuthKey(String authKey) {
        if (authKey != null && pomodoroDB.findUserByAuthKey(authKey) != null) {
            return AuthenticationResponse.createAuthenticatedResponse();
        }
        return AuthenticationResponse.createNotAuthenticatedResponse();
    }

}
