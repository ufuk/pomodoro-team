package com.n11.devteam.pomodoro;

import java.util.HashMap;
import java.util.Map;

public final class PomodoroDB {

    private static PomodoroDB instance;

    private final static Map<String, Object> dataMap = new HashMap<String, Object>();

    private PomodoroDB() {
        dataMap.put("pomodoroStatus", "Stopped");
    }

    public synchronized static PomodoroDB connect() {
        if (instance == null) {
            instance = new PomodoroDB();
        }
        return instance;
    }

    public Object find(String key) {
        return dataMap.get(key);
    }

    public void persist(String key, Object value) {
        dataMap.put(key, value);
    }

}
