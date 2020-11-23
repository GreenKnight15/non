package com.github.greenknight15.non;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;

@Path("/geo")
public class GeoService {

    @Inject
    Vertx vertx;

    private WebClient client;
    private static final Logger LOG = Logger.getLogger(NoNController.class);

    @PostConstruct
    void initialize() {
        this.client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("www.geoplugin.net")
                        .setDefaultPort(80).setSsl(false).setTrustAll(true));
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{ip}")
    public Uni<JsonObject> getGeoLocation(String ip) {
        LOG.debug("Getting location for" + ip);
        return client.get("/json.gp")
                .addQueryParam("ip",ip)
                .send()
                .onItem()
                .transform(resp -> resp.bodyAsJsonObject());
    }
}
