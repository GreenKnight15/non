package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class CountryLeaderboardRecord {
    @BsonProperty("countries")
    public List<CountryRecord> countries;
    @BsonProperty("_id")
    public String status;
}
