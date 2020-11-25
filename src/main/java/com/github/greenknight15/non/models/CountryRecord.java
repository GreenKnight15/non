package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class CountryRecord {
    @BsonProperty("countryCode")
    public String countryCode;
    @BsonProperty("country")
    public String country;
    @BsonProperty("count")
    public Long count;
}
