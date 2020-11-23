package com.github.greenknight15.non;

import com.github.greenknight15.non.models.StateLeaderboardRecord;
import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.greenknight15.non.models.ListStatus;
import io.vertx.ext.web.RoutingContext;
import com.github.greenknight15.non.models.Status;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NoNController {

    @Inject
    NoNService service;

    @Inject
    RoutingContext rc;

    private static final Logger LOG = Logger.getLogger(NoNController.class);

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<ListStatus> getCount() {
        String remoteAddress = rc.request().headers().get("X-Real-IP");
        //String remoteAddress = rc.request().remoteAddress().toString().split(":",2)[0];
        return service.getListStatus(remoteAddress);
    }

    @GET
    @Path("/states")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<StateLeaderboardRecord> getStates() {
        return service.getLeaderboard("State");
    }


    @POST
    @Path("/nice")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<ListStatus> IncrementNiceCount() {
        String remoteAddress = rc.request().headers().get("X-Real-IP");
        LOG.debug("Post nice request from " + remoteAddress);
        service.UpdateUser(remoteAddress, Status.NICE);
        service.UpdateLocation(remoteAddress);
        return service.getListStatus(remoteAddress);
    }

    @POST
    @Path("/naughty")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<ListStatus> IncrementNaughtyCount() {
        String remoteAddress = rc.request().headers().get("X-Real-IP");
        LOG.debug("Post naughty request from " + remoteAddress);
        service.UpdateUser(remoteAddress, Status.NAUGHTY);
        service.UpdateLocation(remoteAddress);
        return service.getListStatus(remoteAddress);
    }

//    @POST
//    @Path("/naughty")
//    @Produces(MediaType.APPLICATION_JSON)
//    public CompletionStage<Count> IncrementNaughtyCount() {
//        service.IncrementNaughtyCount();
//        return service.getCount();
//    }
}