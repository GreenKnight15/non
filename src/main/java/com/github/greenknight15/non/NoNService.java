package com.github.greenknight15.non;

import com.github.greenknight15.non.models.StateLeaderboard;
import com.github.greenknight15.non.models.StateLeaderboardRecord;
import com.github.greenknight15.non.repositories.LocationRepository;
import com.github.greenknight15.non.repositories.entities.Location;
import com.mongodb.client.model.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.time.LocalDate;
import com.github.greenknight15.non.models.Status;
import com.github.greenknight15.non.models.ListStatus;
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
        // Don't use indefinitely
        NoNSubmission submission = nonRepository.findByIp(ip).await().atMost(Duration.ofSeconds(2));
        LOG.debug("Existing status " + submission);
        if(submission == null) {
            NoNSubmission newSubmission = new NoNSubmission(ip, LocalDate.now(), status);
            Uni<Void> result = nonRepository.persist(newSubmission);
            return result.subscribeAsCompletionStage();
        } else {
            LOG.debug("Updating status for " + ip + " to " + status);
            nonRepository.updateStatusByIp(status,ip).await().atMost(Duration.ofSeconds(2));
            //nonRepository.update("status", status).where("ip", ip).subscribeAsCompletionStage();
        }
        return null;
    }

    public CompletionStage<ListStatus> getListStatus(String ip) {
        Uni<Long> niceCount = nonRepository.count("status", Status.NICE);
        Uni<Long> naughtyCount = nonRepository.count("status", Status.NAUGHTY);
        Uni<NoNSubmission> submission = nonRepository.findByIp(ip);

        List<Uni<?>> list = Arrays.asList(niceCount, naughtyCount, submission);

        Uni<ListStatus> uni = Uni.combine().all().unis(list).combinedWith(results -> {
             //NoNSubmission noNSubmission = results.get(2);
            return new ListStatus(
                    (Long)results.get(0),
                    (Long)results.get(1),
                    (NoNSubmission)results.get(2)
            );
        });

        return  uni.subscribeAsCompletionStage();
    }

    public CompletionStage<Void> UpdateLocation(String ip) {
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

    // TODO Clean up and make UI
    public Multi<StateLeaderboardRecord> getLeaderboard(String scope) {
        LOG.debug("Getting State Leader Board");

        ArrayList<BsonField> groups1 = new ArrayList<>();
        groups1.add(Accumulators.sum("count", 1));
        groups1.add(Accumulators.max("state", "$state"));

        String groupBy = "";
        String fieldName = "";

        if(scope == "State") {
            groupBy = "$stateCode";
            fieldName = "stateCode";
        }

        List agg = Arrays.asList(
                Aggregates.match(Filters.ne(fieldName, "")),
                Aggregates.group("$stateCode", groups1),
                Aggregates.project(
                        Projections.fields(
                                Projections.excludeId(),
                                Projections.computed("count", 1),
                                Projections.computed("state", 1),
                                Projections.computed(fieldName, "$_id")
                        )
                ),
                Aggregates.sort(Sorts.descending("count"))
        );

        return locationRepository.mongoCollection()
                .aggregate(agg, StateLeaderboardRecord.class);
    }
}