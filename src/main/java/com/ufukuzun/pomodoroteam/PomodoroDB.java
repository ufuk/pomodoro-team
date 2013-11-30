package com.ufukuzun.pomodoroteam;

import com.ufukuzun.pomodoroteam.model.PomodoroResponse;
import com.ufukuzun.pomodoroteam.model.PomodoroStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class PomodoroDB {

    private static PomodoroDB instance;

    private final static Map<String, Object> dataMap = new HashMap<String, Object>();

    private PomodoroDB() {
        dataMap.put("status", PomodoroStatus.Stopped);
    }

    public synchronized static PomodoroDB connect() {
        if (instance == null) {
            instance = new PomodoroDB();
        }
        return instance;
    }

    public void persist(PomodoroResponse response) {
        dataMap.put("status", response.getStatus());
        dataMap.put("minute", response.getMinute());
        if (response.isStatusStopped()) {
            dataMap.put("updateTime", null);
        } else {
            dataMap.put("updateTime", new Date(response.getUpdateTime()));
        }
    }

    public PomodoroResponse getCurrentStatus() {
        PomodoroResponse response = new PomodoroResponse();
        response.setStatus((PomodoroStatus) dataMap.get("status"));
        response.setMinute((Integer) dataMap.get("minute"));
        Date statusUpdateTime = (Date) dataMap.get("updateTime");
        if (statusUpdateTime != null) {
            response.setUpdateTime(statusUpdateTime.getTime());
        }
        return response;
    }

}
