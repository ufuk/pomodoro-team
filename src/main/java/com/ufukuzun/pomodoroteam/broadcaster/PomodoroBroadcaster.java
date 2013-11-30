package com.ufukuzun.pomodoroteam.broadcaster;

import com.ufukuzun.pomodoroteam.PomodoroDB;
import com.ufukuzun.pomodoroteam.model.PomodoroMessage;
import com.ufukuzun.pomodoroteam.model.PomodoroResponse;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.jersey.JerseyBroadcaster;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

@Path("/")
@AtmosphereService(broadcaster = JerseyBroadcaster.class)
public class PomodoroBroadcaster {

    private PomodoroDB pomodoroDB = PomodoroDB.connect();

    @GET
    @Suspend(contentType = "application/json")
    public String suspend() {
        return "";
    }

    @POST
    @Broadcast(writeEntity = false)
    @Produces("application/json")
    public PomodoroResponse broadcast(PomodoroMessage message) {
        PomodoroResponse response = PomodoroResponse.createFor(message);
        if (response.isStatusStarted()) {
            response.setUpdateTime(new Date().getTime());
        }

        pomodoroDB.persist(response);

        return response;
    }

    @GET
    @Produces("application/json")
    @Path("/currentStatus")
    public PomodoroResponse currentStatus() {
        return pomodoroDB.getCurrentStatus();
    }

}
