package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class CityLeaderboardRecord {
    @BsonProperty("city")
    public String city;
    @BsonProperty("count")
    public Long count;
}