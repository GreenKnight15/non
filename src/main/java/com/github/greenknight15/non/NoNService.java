package com.github.greenknight15.non;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.eclipse.microprofile.reactive.streams.operators.CompletionRunner;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.inc;
@ApplicationScoped
public class NoNService {

    @Inject ReactiveMongoClient mongoClient;

    public CompletionStage<Count> getCount() {

        return getCollection().find().collectItems().first().map(document ->{
                    return new Count(document.getDouble("nice"), document.getDouble("naughty"));
                }).subscribeAsCompletionStage();
    }

    public CompletionStage<UpdateResult> IncrementNiceCount(){
        return getCollection().updateOne(gte("nice", 0), inc("nice", 1)).subscribeAsCompletionStage();
    }

    public CompletionStage<UpdateResult> IncrementNaughtyCount(){
        return getCollection().updateOne(gte("naughty", 0), inc("naughty", 1)).subscribeAsCompletionStage();
    }


    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("non").getCollection("count");
    }
}