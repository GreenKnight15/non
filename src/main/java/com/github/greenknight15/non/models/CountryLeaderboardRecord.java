package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class CountryLeaderboardRecord {
    @BsonProperty("country")
    public String country;
    @BsonProperty("countryCode")
    public String countryCode;
    @BsonProperty("count")
    public Long count;
}
