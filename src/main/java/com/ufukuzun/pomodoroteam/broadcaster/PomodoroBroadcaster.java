package com.ufukuzun.pomodoroteam.broadcaster;

import com.ufukuzun.pomodoroteam.db.PomodoroDB;
import com.ufukuzun.pomodoroteam.model.AuthenticationResponse;
import com.ufukuzun.pomodoroteam.model.LogInRequest;
import com.ufukuzun.pomodoroteam.model.PomodoroMessage;
import com.ufukuzun.pomodoroteam.model.PomodoroResponse;
import com.ufukuzun.pomodoroteam.service.AuthenticationService;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.jersey.JerseyBroadcaster;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/")
@AtmosphereService(broadcaster = JerseyBroadcaster.class)
public class PomodoroBroadcaster {

    private PomodoroDB pomodoroDB = PomodoroDB.connect();

    private AuthenticationService authenticationService = new AuthenticationService();

    @GET
    @Suspend(contentType = MediaType.APPLICATION_JSON)
    public String suspend() {
        return "";
    }

    @POST
    @Broadcast(writeEntity = false)
    @Produces(MediaType.APPLICATION_JSON)
    public PomodoroResponse broadcast(PomodoroMessage message) {
        PomodoroResponse response = PomodoroResponse.createFor(message);
        if (response.isStatusStarted()) {
            response.setUpdateTime(new Date().getTime());
        }

        pomodoroDB.persist(response);

        return response;
    }

    @GET
    @Path("/currentStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public PomodoroResponse currentStatus() {
        return pomodoroDB.getCurrentStatus();
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
