package com.github.greenknight15.non.repositories.entities;

public class Location {

    public String ip;
    public String city;
    public String state;
    public String stateCode;
    public String country;
    public String countryCode;
    public String areaCode;

    public Location() {

    }

    public Location(String ip, String city, String state, String stateCode, String country, String countryCode, String areaCode) {
        this.ip = ip;
        this.city = city;
        this.state = state;
        this.stateCode = stateCode;
        this.country = country;
        this.countryCode = countryCode;
        this.areaCode = areaCode;
    }
}
