package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class StateLeaderboardRecord {
    @BsonProperty("stateCode")
    public String stateCode;
    @BsonProperty("state")
    public String state;
    @BsonProperty("count")
    public Long count;
}
