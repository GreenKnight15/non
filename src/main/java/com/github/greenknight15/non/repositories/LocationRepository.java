package com.github.greenknight15.non.repositories;

import com.github.greenknight15.non.models.CityLeaderboardRecord;
import com.github.greenknight15.non.models.CountryLeaderboardRecord;
import com.github.greenknight15.non.models.StateLeaderboardRecord;
import com.github.greenknight15.non.repositories.entities.Location;
import com.mongodb.client.model.*;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class LocationRepository implements ReactivePanacheMongoRepository<Location> {

    public Uni<Location> findByIp(String ip){
        return find("ip", ip).firstResult();
    }

    public Multi<StateLeaderboardRecord> getStateLeaderboard() {
        ArrayList<BsonField> groups = new ArrayList<>();
        groups.add(Accumulators.sum("count", 1));
        groups.add(Accumulators.max("state", "$state"));

        List agg = Arrays.asList(
                Aggregates.match(Filters.ne("stateCode", "")),
                Aggregates.group("$stateCode", groups),
                Aggregates.project(
                        Projections.fields(
                                Projections.excludeId(),
                                Projections.computed("count", 1),
                                Projections.computed("state", 1),
                                Projections.computed("stateCode", "$_id")
                        )
                ),
                Aggregates.sort(Sorts.descending("count"))
        );
        return this.mongoCollection().aggregate(agg, StateLeaderboardRecord.class);
    }

    public Multi<CityLeaderboardRecord> getCityLeaderboard() {
        ArrayList<BsonField> groups = new ArrayList<>();
        groups.add(Accumulators.sum("count", 1));

        List agg = Arrays.asList(
                Aggregates.match(Filters.ne("city", "")),
                Aggregates.group("$city", groups),
                Aggregates.project(
                        Projections.fields(
                                Projections.excludeId(),
                                Projections.computed("count", 1),
                                Projections.computed("city", "$_id")
                        )
                ),
                Aggregates.sort(Sorts.descending("count"))
        );
        return this.mongoCollection().aggregate(agg, CityLeaderboardRecord.class);
    }

    public Multi<CountryLeaderboardRecord> getCountryLeaderboard() {
        ArrayList<BsonField> groups = new ArrayList<>();
        groups.add(Accumulators.sum("count", 1));
        groups.add(Accumulators.max("country", "$country"));

        List agg = Arrays.asList(
                Aggregates.match(Filters.ne("countryCode", "")),
                Aggregates.group("$countryCode", groups),
                Aggregates.project(
                        Projections.fields(
                                Projections.excludeId(),
                                Projections.computed("count", 1),
                                Projections.computed("country", 1),
                                Projections.computed("countryCode", "$_id")
                        )
                ),
                Aggregates.sort(Sorts.descending("count"))
        );
        return this.mongoCollection().aggregate(agg, CountryLeaderboardRecord.class);
    }
}
