package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class CityRecord {
    @BsonProperty("city")
    public String city;
    @BsonProperty("stateCode")
    public String stateCode;
    @BsonProperty("count")
    public Long count;
}
