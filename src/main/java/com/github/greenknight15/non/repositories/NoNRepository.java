package com.github.greenknight15.non.repositories;

import com.github.greenknight15.non.models.StateLeaderboardRecord;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.*;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import com.github.greenknight15.non.repositories.entities.NoNSubmission;
import com.github.greenknight15.non.models.Status;

import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class NoNRepository implements ReactivePanacheMongoRepository<NoNSubmission> {

//    public Uni<Optional<NoNSubmission>> findByIp(String ip){
//        return find("ip", ip).firstResult();
//    }

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
        //List agg = Arrays.asList();
        //Addr = Status
        //books = states
        //// book = state
        //// count = count of status in state

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

        List agg = Arrays.asList(
                Aggregates.match(Filters.ne("stateCode", null)),
                Aggregates.group( new BasicDBObject(dbObjIdMap), groups),
                Aggregates.group("$_id.status", groups2),
                Aggregates.sort(Sorts.descending("states.count"))

                );

        return this.mongoCollection().aggregate(agg, StateLeaderboardRecord.class);
    }


    public Uni<NoNSubmission> replaceBySubmission(NoNSubmission submission) {
        return this.mongoCollection().findOneAndReplace(Filters.eq("ip", submission.get_ip()), submission);
    }
}