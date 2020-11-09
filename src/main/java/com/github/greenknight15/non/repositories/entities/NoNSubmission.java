package com.github.greenknight15.non.repositories.entities;

import com.github.greenknight15.non.models.Status;

import javax.json.bind.annotation.JsonbDateFormat;
import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity ;

import java.util.*;
import java.time.LocalDate;

@MongoEntity(collection="NoNList")
public class NoNSubmission extends ReactivePanacheMongoEntity {
    public ObjectId id; // used by MongoDB for the _id field
    public String ip;
    public LocalDate last_updated;
    public Status status;

    public NoNSubmission(){

    }

    public NoNSubmission(String ip, LocalDate last_updated, Status status) {
        this.ip = ip;
        this.last_updated = last_updated;
        this.status = status;
    }

}