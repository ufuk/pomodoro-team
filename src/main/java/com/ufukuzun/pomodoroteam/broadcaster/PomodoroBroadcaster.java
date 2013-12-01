package com.ufukuzun.pomodoroteam.broadcaster;

import com.ufukuzun.pomodoroteam.db.PomodoroDB;
import com.ufukuzun.pomodoroteam.model.AuthenticationResponse;
import com.ufukuzun.pomodoroteam.model.LogInRequest;
import com.ufukuzun.pomodoroteam.model.PomodoroMessage;
import com.ufukuzun.pomodoroteam.model.PomodoroState;
import com.ufukuzun.pomodoroteam.service.AuthenticationService;
import com.ufukuzun.pomodoroteam.service.PomodoroService;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.jersey.JerseyBroadcaster;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@AtmosphereService(broadcaster = JerseyBroadcaster.class)
public class PomodoroBroadcaster {

    private PomodoroDB pomodoroDB = PomodoroDB.connect();

    private AuthenticationService authenticationService = new AuthenticationService();

    private PomodoroService pomodoroService = new PomodoroService();

    @GET
    @Suspend(contentType = MediaType.APPLICATION_JSON)
    public String suspend() {
        return "";
    }

    @POST
    @Broadcast(writeEntity = false)
    @Produces(MediaType.APPLICATION_JSON)
    public PomodoroState broadcast(PomodoroMessage message) {
        return pomodoroService.processMessage(message);
    }

    @POST
    @Path("/currentStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public PomodoroState currentStatus(String authKey) {
        return pomodoroService.getCurrentStatus(authKey);
    }

    @POST
    @Path("/checkAuthKey")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse checkAuthKey(String authKey) {
        return authenticationService.checkAuthKey(authKey);
    }

    @POST
    @Path("/logIn")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthenticationResponse logIn(LogInRequest request) {
        return authenticationService.logIn(request);
    }

}
