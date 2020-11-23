package com.github.greenknight15.non.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class StateLeaderboardRecord {
    @BsonProperty("states")
    public List<StateRecord> states;
    @BsonProperty("_id")
    public String status;
}
