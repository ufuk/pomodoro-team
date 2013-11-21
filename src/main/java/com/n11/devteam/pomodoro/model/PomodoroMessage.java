package com.n11.devteam.pomodoro.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PomodoroMessage {

    private String developerId;

    private String pomodoroStatus;

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

}
