package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class CityLeaderboardRecord {
    @BsonProperty("cities")
    public List<CityRecord> cities;
    @BsonProperty("_id")
    public String status;
}