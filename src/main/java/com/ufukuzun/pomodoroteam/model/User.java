package com.ufukuzun.pomodoroteam.model;

import java.util.Date;

public class User {

    private String userId;

    private String password;

    private String lastAuthKey;

    private PomodoroState pomodoroState = new PomodoroState();

    public static User createFor(AuthenticationRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(request.getPassword());
        user.generateAuthKey();
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastAuthKey() {
        return lastAuthKey;
    }

    public void setLastAuthKey(String lastAuthKey) {
        this.lastAuthKey = lastAuthKey;
    }

    public PomodoroState getPomodoroState() {
        return pomodoroState;
    }

    public void setPomodoroState(PomodoroState pomodoroState) {
        this.pomodoroState = pomodoroState;
    }

    public void generateAuthKey() {
        lastAuthKey = "" + (userId + new Date().getTime() + password).hashCode();
    }

}
