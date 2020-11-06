package com.github.greenknight15.non;

import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NoNController {

    @Inject
    NoNService service;

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Count> getCount() {
        return service.getCount();
    }


    @POST
    @Path("/nice")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Count> IncrementNiceCount() {
        service.IncrementNiceCount();
        return service.getCount();
    }

    @POST
    @Path("/naughty")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Count> IncrementNaughtyCount() {
        service.IncrementNaughtyCount();
        return service.getCount();
    }
}