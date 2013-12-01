package com.ufukuzun.pomodoroteam.service;

import com.ufukuzun.pomodoroteam.db.PomodoroDB;
import com.ufukuzun.pomodoroteam.model.PomodoroMessage;
import com.ufukuzun.pomodoroteam.model.PomodoroState;
import com.ufukuzun.pomodoroteam.model.SingleStateResponse;
import com.ufukuzun.pomodoroteam.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PomodoroService {

    private PomodoroDB pomodoroDB = PomodoroDB.connect();

    public SingleStateResponse processMessage(PomodoroMessage message) {
        User user = pomodoroDB.findUserByAuthKey(message.getAuthKey());

        PomodoroState state = PomodoroState.createFor(message);
        if (state.isStatusStarted()) {
            state.setUpdateTime(new Date().getTime());
        } else if (state.isStatusStopped()) {
            state.setUpdateTime(null);
        }

        user.setPomodoroState(state);

        return SingleStateResponse.createFor(user);
    }

    public List<SingleStateResponse> getCurrentStates(String authKey) {
        List<SingleStateResponse> states = new ArrayList<SingleStateResponse>();
        if (isUserAuthenticated(authKey)) {
            for (User eachUser : pomodoroDB.getAllUsers()) {
                states.add(SingleStateResponse.createFor(eachUser));
            }
        }
        return states;
    }

    private boolean isUserAuthenticated(String authKey) {
        return pomodoroDB.findUserByAuthKey(authKey) != null;
    }

}
