package com.github.greenknight15.non;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.concurrent.CompletionStage;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NoNController {

    @Inject
    NoNService service;

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Count> GetCount() {

        return service.GetCount();
    }

    @POST
    @Path("/nice")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Count> IncrementNiceCount() {

        return service.IncrementNiceCount();
    }

    @POST
    @Path("/naughty")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Count> IncrementNaughtyCount() {

        return service.IncrementNaughtyCount();
    }
}