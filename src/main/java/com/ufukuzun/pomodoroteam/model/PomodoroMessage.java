package com.ufukuzun.pomodoroteam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PomodoroMessage {

    private String authKey;

    private String status;

    private Integer minute;

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
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
