package com.ufukuzun.pomodoroteam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PomodoroState {

    private PomodoroStatus status = PomodoroStatus.Stopped;

    private Long updateTime;

    private Integer minute;

    private String authKey;

    public static PomodoroState createFor(PomodoroMessage message) {
        PomodoroState state = new PomodoroState();
        state.setStatus(PomodoroStatus.valueOf(message.getStatus()));
        state.setMinute(message.getMinute());
        return state;
    }

    public PomodoroStatus getStatus() {
        return status;
    }

    public void setStatus(PomodoroStatus status) {
        this.status = status;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public boolean isStatusStarted() {
        return status == PomodoroStatus.Started;
    }

    public boolean isStatusStopped() {
        return status == PomodoroStatus.Stopped;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

}
