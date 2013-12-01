package com.ufukuzun.pomodoroteam.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class SingleStateResponse {

    private String userId;

    private PomodoroState state;

    private long currentServerTime;

    private SingleStateResponse() {
        currentServerTime = new Date().getTime();
    }

    public static SingleStateResponse createFor(User user) {
        SingleStateResponse response = new SingleStateResponse();
        response.setUserId(user.getUserId());
        response.setState(user.getPomodoroState());
        return response;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PomodoroState getState() {
        return state;
    }

    public void setState(PomodoroState state) {
        this.state = state;
    }

    public long getCurrentServerTime() {
        return currentServerTime;
    }

    public void setCurrentServerTime(long currentServerTime) {
        this.currentServerTime = currentServerTime;
    }

}
