package com.n11.devteam.pomodoro.broadcaster;

import com.n11.devteam.pomodoro.PomodoroDB;
import com.n11.devteam.pomodoro.model.PomodoroMessage;
import com.n11.devteam.pomodoro.model.PomodoroResponse;
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

    @Suspend(contentType = "application/json")
    @GET
    public String suspend() {
        return "";
    }

    @Broadcast(writeEntity = false)
    @POST
    @Produces("application/json")
    public PomodoroResponse broadcast(PomodoroMessage message) {
        PomodoroResponse response = new PomodoroResponse();
        response.setDeveloperId(message.getDeveloperId());
        response.setPomodoroStatus(message.getPomodoroStatus());
        if ("Started".equals(response.getPomodoroStatus())) {
            response.setStatusUpdateTime(new Date().getTime());
        }

        PomodoroDB db = PomodoroDB.connect();
        db.persist("pomodoroStatus", response.getPomodoroStatus());
        if ("Stopped".equals(response.getPomodoroStatus())) {
            db.persist("statusUpdateTime", null);
        } else {
            db.persist("statusUpdateTime", new Date(response.getStatusUpdateTime()));
        }

        return response;
    }

    @GET
    @Produces("application/json")
    @Path("/currentStatus")
    public PomodoroResponse currentStatus() {
        PomodoroDB db = PomodoroDB.connect();
        PomodoroResponse response = new PomodoroResponse();
        response.setPomodoroStatus((String) db.find("pomodoroStatus"));
        Date statusUpdateTime = (Date) db.find("statusUpdateTime");
        if (statusUpdateTime != null) {
            response.setStatusUpdateTime(statusUpdateTime.getTime());
        }
        return response;
    }

}
