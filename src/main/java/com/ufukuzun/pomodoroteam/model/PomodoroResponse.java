package com.ufukuzun.pomodoroteam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PomodoroResponse {

    private String developerId;

    private PomodoroStatus status;

    private long updateTime;

    private Integer minute;

    public static PomodoroResponse createFor(PomodoroMessage message) {
        PomodoroResponse response = new PomodoroResponse();
        response.setDeveloperId(message.getDeveloperId());
        response.setStatus(PomodoroStatus.valueOf(message.getStatus()));
        response.setMinute(message.getMinute());
        return response;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public PomodoroStatus getStatus() {
        return status;
    }

    public void setStatus(PomodoroStatus status) {
        this.status = status;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
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

}
