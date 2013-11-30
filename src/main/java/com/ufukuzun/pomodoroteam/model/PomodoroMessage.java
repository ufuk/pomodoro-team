package com.ufukuzun.pomodoroteam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PomodoroMessage {

    private String developerId;

    private String status;

    private Integer minute;

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

}
