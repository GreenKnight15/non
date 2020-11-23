package com.github.greenknight15.non;

import com.github.greenknight15.non.models.*;
import com.github.greenknight15.non.repositories.LocationRepository;
import com.github.greenknight15.non.repositories.entities.Location;
import com.mongodb.client.model.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.time.LocalDate;

import com.github.greenknight15.non.repositories.NoNRepository;
import com.github.greenknight15.non.repositories.entities.NoNSubmission;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import java.util.Arrays;

@ApplicationScoped
public class NoNService {

    @Inject
    LocationRepository locationRepository;

    @Inject
    NoNRepository nonRepository;

    @Inject
    GeoService geoService;

    private static final Logger LOG = Logger.getLogger(NoNService.class);

    public CompletionStage<Void> UpdateUser(String ip, Status status){
        if(ip == null) {
            return null;
        }
        // Don't use indefinitely
        Optional<NoNSubmission> submission = nonRepository.findByIp(ip).await().atMost(Duration.ofSeconds(2));
        LOG.debug("Existing status " + submission.isPresent());
        if(submission.isPresent() && submission.get().get_country() == null) {
            LOG.debug("Adding location to missing record");
            Location location = this.getLocation(ip).toCompletableFuture().join();
            LOG.debug("Adding location "+ location.country);

            NoNSubmission newSubmission = new NoNSubmission(ip,
                    LocalDate.now(),
                    status,
                    location.city,
                    location.state,
                    location.stateCode,
                    location.country,
                    location.stateCode,
                    location.areaCode);
             nonRepository.replaceBySubmission(newSubmission).await().atMost(Duration.ofSeconds(2));
             return null;
        } else if(submission.isPresent() ) {
            LOG.debug("Updating status for " + ip + " to " + status);
            nonRepository.updateStatusByIp(status,ip).await().atMost(Duration.ofSeconds(2));
            return null;
        } else {
            Location location = this.getLocation(ip).toCompletableFuture().join();
            NoNSubmission newSubmission = new NoNSubmission(ip,
                    LocalDate.now(),
                    status,
                    location.city,
                    location.state,
                    location.stateCode,
                    location.country,
                    location.stateCode,
                    location.areaCode);
            Uni<Void> result = nonRepository.persist(newSubmission);
            return result.subscribeAsCompletionStage();
        }
    }

    public CompletionStage<ListStatus> getListStatus(String ip) {
        Uni<Long> niceCount = nonRepository.count("status", Status.NICE);
        Uni<Long> naughtyCount = nonRepository.count("status", Status.NAUGHTY);

        Uni<Optional<NoNSubmission>> submission = nonRepository.findByIp(ip);

        List<Uni<?>> list = Arrays.asList(niceCount, naughtyCount, submission);

        Uni<ListStatus> uni = Uni.combine().all().unis(list).combinedWith(results -> {
             //NoNSubmission noNSubmission = results.get(2);
            return new ListStatus(
                    (Long)results.get(0),
                    (Long)results.get(1),
                    (Optional<NoNSubmission>) results.get(2)
            );
        });

        return  uni.subscribeAsCompletionStage();
    }

    public CompletionStage<Void> UpdateLocation(String ip) {
        if(ip == null) {
            return null;
        }
        LOG.debug("Updating location for "+ ip);
        Location location = locationRepository.findByIp(ip).await().atMost(Duration.ofSeconds(2));
        if(location == null) {
            Location loca = this.getLocation(ip).toCompletableFuture().join();
            Uni<Void> result = locationRepository.persist(loca);
            return result.subscribeAsCompletionStage();
        } else {
            return null;
        }
    }

    private CompletionStage<Location> getLocation(String ip) {

        Uni<JsonObject> jsonObject = geoService.getGeoLocation(ip);
        return jsonObject.onItem().transform(json -> {
            String city = json.getString("geoplugin_city");
            String state = json.getString("geoplugin_region");
            String stateCode = json.getString("geoplugin_regionCode");
            String areaCode = json.getString("geoplugin_areaCode");
            String country = json.getString("geoplugin_countryName");
            String countryCode = json.getString("geoplugin_countryCode");

            return new Location(ip, city, state, stateCode, country, countryCode, areaCode);
        }).subscribeAsCompletionStage();
    }

    public Multi<StateLeaderboardRecord> getStateLeaderboard() {
        LOG.debug("Getting State Leaderboard");
        return nonRepository.getStateLeaderboard();
    }
    public Multi<CityLeaderboardRecord> getCityLeaderboard() {
        LOG.debug("Getting City Leaderboard");
        return locationRepository.getCityLeaderboard();
    }
    public Multi<CountryLeaderboardRecord> getCountryLeaderboard() {
        LOG.debug("Getting Country Leaderboard");
        return locationRepository.getCountryLeaderboard();
    }
}