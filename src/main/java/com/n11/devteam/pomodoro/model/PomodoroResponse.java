package com.n11.devteam.pomodoro.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class PomodoroResponse {

    private String developerId;

    private String pomodoroStatus;

    private long statusUpdateTime;

    private long now;

    public PomodoroResponse() {
        now = new Date().getTime();
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getPomodoroStatus() {
        return pomodoroStatus;
    }

    public void setPomodoroStatus(String pomodoroStatus) {
        this.pomodoroStatus = pomodoroStatus;
    }

    public long getStatusUpdateTime() {
        return statusUpdateTime;
    }

    public void setStatusUpdateTime(long statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

}
