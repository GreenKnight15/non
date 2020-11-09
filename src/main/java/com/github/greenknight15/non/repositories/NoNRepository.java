package com.github.greenknight15.non.repositories;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import com.github.greenknight15.non.repositories.entities.NoNSubmission;
import com.github.greenknight15.non.models.Status;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class NoNRepository implements ReactivePanacheMongoRepository<NoNSubmission> {

//    public Uni<Optional<NoNSubmission>> findByIp(String ip){
//        return find("ip", ip).firstResult();
//    }

    public Uni<NoNSubmission> findByIp(String ip){
        return find("ip", ip).firstResult();
    }

    public Uni<List<NoNSubmission>> findNice(){
        return list("status", Status.NICE);
    }

    public Uni<Long> updateStatusByIp(Status status, String ip){
        return update("status", status).where("ip", ip);
    }

//    public Uni<Void> deleteByIp(String ip){
//        return delete("ip", ip);
//    }
}