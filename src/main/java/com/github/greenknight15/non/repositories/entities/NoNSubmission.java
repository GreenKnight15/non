package com.github.greenknight15.non.repositories.entities;

import com.github.greenknight15.non.models.Status;

import javax.json.bind.annotation.JsonbDateFormat;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity ;

import java.util.*;
import java.time.LocalDate;

@MongoEntity(collection="NoNList")
public class NoNSubmission extends ReactivePanacheMongoEntity {
    public ObjectId id; // used by MongoDB for the _id field
    @BsonProperty("ip")
    public String ip;
    @BsonProperty("last_updated")
    public LocalDate last_updated;
    @BsonProperty("status")
    public Status status;
    @BsonProperty("city")
    public String city;
    @BsonProperty("state")
    public String state;
    @BsonProperty("stateCode")
    public String stateCode;
    @BsonProperty("country")
    public String country;
    @BsonProperty("countryCode")
    public String countryCode;
    @BsonProperty("areaCode")
    public String areaCode;

    public NoNSubmission(){

    }

    public NoNSubmission(String ip, LocalDate last_updated, Status status, String city, String state, String stateCode, String country, String countryCode, String areaCode) {
        this.ip = ip;
        this.last_updated = last_updated;
        this.status = status;
        this.city = city;
        this.state = state;
        this.stateCode = stateCode;
        this.country = country;
        this.countryCode = countryCode;
        this.areaCode = areaCode;
    }

    public String get_country(){
        return this.country;
    }
    public String get_ip(){
        return this.ip;
    }
}