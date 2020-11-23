package com.github.greenknight15.non.repositories;

import com.github.greenknight15.non.repositories.entities.Location;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class LocationRepository implements ReactivePanacheMongoRepository<Location> {

    public Uni<Location> findByIp(String ip){
        return find("ip", ip).firstResult();
    }

}
