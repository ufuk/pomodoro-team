package com.ufukuzun.pomodoroteam.service;

import com.ufukuzun.pomodoroteam.db.PomodoroDB;
import com.ufukuzun.pomodoroteam.model.PomodoroMessage;
import com.ufukuzun.pomodoroteam.model.PomodoroState;
import com.ufukuzun.pomodoroteam.model.User;

import java.util.Date;

public class PomodoroService {

    private PomodoroDB pomodoroDB = PomodoroDB.connect();

    public PomodoroState processMessage(PomodoroMessage message) {
        User user = pomodoroDB.findUserByAuthKey(message.getAuthKey());

        PomodoroState state = PomodoroState.createFor(message);
        if (state.isStatusStarted()) {
            state.setUpdateTime(new Date().getTime());
        } else if (state.isStatusStopped()) {
            state.setUpdateTime(null);
        }

        user.setPomodoroState(state);

        return state;
    }

    public PomodoroState getCurrentStatus(String authKey) {
        User user = pomodoroDB.findUserByAuthKey(authKey);
        return user.getPomodoroState();
    }

}
