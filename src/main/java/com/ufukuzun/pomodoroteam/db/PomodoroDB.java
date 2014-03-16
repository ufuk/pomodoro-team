package com.ufukuzun.pomodoroteam.db;

import com.ufukuzun.pomodoroteam.model.User;

import java.util.ArrayList;
import java.util.List;

public final class PomodoroDB {

    private static PomodoroDB instance;

    private final static List<User> USER_LIST = new ArrayList<User>();

    private PomodoroDB() {
    }

    public synchronized static PomodoroDB connect() {
        if (instance == null) {
            instance = new PomodoroDB();
        }
        return instance;
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

    public List<User> getAllUsers() {
        return USER_LIST;
    }

    public void persistNewUser(User user) {
        USER_LIST.add(user);
    }

}
