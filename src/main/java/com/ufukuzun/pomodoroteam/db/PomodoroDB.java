package com.ufukuzun.pomodoroteam.db;

import com.ufukuzun.pomodoroteam.model.PomodoroResponse;
import com.ufukuzun.pomodoroteam.model.PomodoroStatus;
import com.ufukuzun.pomodoroteam.model.User;

import java.util.*;

public final class PomodoroDB {

    private static PomodoroDB instance;

    private final static Map<String, Object> dataMap = new HashMap<String, Object>();

    private final static List<User> USER_LIST = new ArrayList<User>();

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

    public User findUserByUserIdAndPassword(String userId, String password) {
        for (User eachUser : USER_LIST) {
            if (userId.equals(eachUser.getUserId()) && password.equals(eachUser.getPassword())) {
                return eachUser;
            }
        }
        return null;
    }

    public User findUserByAuthKey(String authKey) {
        for (User eachUser : USER_LIST) {
            if (authKey.equals(eachUser.getLastAuthKey())) {
                return eachUser;
            }
        }
        return null;
    }

    public void persistNewUser(User user) {
        USER_LIST.add(user);
    }

    public void updateUser(User user) {
        int index = USER_LIST.indexOf(user);
        USER_LIST.remove(index);
        USER_LIST.add(user);
    }

}
