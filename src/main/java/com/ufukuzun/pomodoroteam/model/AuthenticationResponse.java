package com.ufukuzun.pomodoroteam.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthenticationResponse {

    private boolean authenticated = false;

    private String authKey;

    public static AuthenticationResponse createFor(User user) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticated(true);
        response.setAuthKey(user.getLastAuthKey());
        return response;
    }

    public static AuthenticationResponse createNotAuthenticatedResponse() {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticated(false);
        return response;
    }

    public static AuthenticationResponse createAuthenticatedResponse() {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticated(true);
        return response;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

}
