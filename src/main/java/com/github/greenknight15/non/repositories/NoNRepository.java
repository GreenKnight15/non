package com.github.greenknight15.non.repositories;

import com.github.greenknight15.non.models.CountryLeaderboardRecord;
import com.github.greenknight15.non.models.StateLeaderboardRecord;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.*;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import com.github.greenknight15.non.repositories.entities.NoNSubmission;
import com.github.greenknight15.non.models.Status;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.conversions.Bson;

@ApplicationScoped
public class NoNRepository implements ReactivePanacheMongoRepository<NoNSubmission> {

    public Uni<Optional<NoNSubmission>> findByIp(String ip){
        return find("ip", ip).firstResultOptional();
    }

    public Uni<List<NoNSubmission>> findNice(){
        return list("status", Status.NICE);
    }

    public Uni<Long> updateStatusByIp(Status status, String ip){
        return update("status", status).where("ip", ip);
    }


    public Uni<NoNSubmission> findOneAndUpdateByIp(Status status, String ip){
        return this.mongoCollection().findOneAndUpdate(Filters.eq("ip", ip), Updates.set("status", status));
    }

    public Multi<StateLeaderboardRecord> getStateLeaderboard() {
        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("status", "$status");
        dbObjIdMap.put("stateCode", "$stateCode");
        dbObjIdMap.put("state", "$state");

        ArrayList<BsonField> groups = new ArrayList<>();
        groups.add(Accumulators.sum("stateCount", 1));
        groups.add(Accumulators.max("state", "$state"));
        groups.add(Accumulators.max("stateCode", "$stateCode"));

        Map<String, Object> dbObjIdMap2 = new HashMap<String, Object>();
        dbObjIdMap2.put("state", "$_id.state");
        dbObjIdMap2.put("stateCode", "$_id.stateCode");
        dbObjIdMap2.put("count", "$stateCount");

        ArrayList<BsonField> groups2 = new ArrayList<>();
        groups2.add(Accumulators.push("states",  new BasicDBObject(dbObjIdMap2)));
        groups2.add(Accumulators.sum("stateCount", "$stateCount"));

        ArrayList<Bson> filters = new ArrayList<>();
        filters.add(Filters.ne("stateCode", null));
        filters.add(Filters.ne("stateCode", ""));

        List agg = Arrays.asList(
                Aggregates.match(Filters.and(filters)),
                Aggregates.group( new BasicDBObject(dbObjIdMap), groups),
                Aggregates.group("$_id.status", groups2),
                Aggregates.sort(Sorts.descending("states.count"))

                );
        //TODO add limit 100
        return this.mongoCollection().aggregate(agg, StateLeaderboardRecord.class);
    }

    public Multi<CountryLeaderboardRecord> getCountryLeaderboard() {
        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("status", "$status");
        dbObjIdMap.put("countryCode", "$countryCode");
        dbObjIdMap.put("country", "$country");

        ArrayList<BsonField> groups = new ArrayList<>();
        groups.add(Accumulators.sum("countryCount", 1));
        groups.add(Accumulators.max("country", "$country"));
        groups.add(Accumulators.max("countryCode", "$countryCode"));

        Map<String, Object> dbObjIdMap2 = new HashMap<String, Object>();
        dbObjIdMap2.put("country", "$_id.country");
        dbObjIdMap2.put("countryCode", "$_id.countryCode");
        dbObjIdMap2.put("count", "$countryCount");

        ArrayList<BsonField> groups2 = new ArrayList<>();
        groups2.add(Accumulators.push("countries",  new BasicDBObject(dbObjIdMap2)));
        groups2.add(Accumulators.sum("countryCount", "$countryCount"));

        ArrayList<Bson> filters = new ArrayList<>();
        filters.add(Filters.ne("countryCode", null));
        filters.add(Filters.ne("countryCode", ""));

        //TODO add limit 100
        List agg = Arrays.asList(
                Aggregates.match(Filters.and(filters)),
                Aggregates.group( new BasicDBObject(dbObjIdMap), groups),
                Aggregates.group("$_id.status", groups2),
                Aggregates.sort(Sorts.descending("country.count"))

        );
        return this.mongoCollection().aggregate(agg, CountryLeaderboardRecord.class);
    }


    public Uni<NoNSubmission> replaceBySubmission(NoNSubmission submission) {
        return this.mongoCollection().findOneAndReplace(Filters.eq("ip", submission.get_ip()), submission);
    }
}