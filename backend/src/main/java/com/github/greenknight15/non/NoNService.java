package com.github.greenknight15.non;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import io.quarkus.mongodb.ReactiveMongoClient;
import io.quarkus.mongodb.ReactiveMongoCollection;
import org.bson.Document;
import org.eclipse.microprofile.reactive.streams.operators.CompletionRunner;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.inc;

@ApplicationScoped
public class NoNService {

    @Inject ReactiveMongoClient mongoClient;

    public CompletionStage<Count> getLatest() {

        Optional<Document> doc = getCollection().find().findFirst().run().toCompletableFuture().join();
        return CompletableFuture.supplyAsync(() -> {
            return doc.map(document -> {
                return new Count(document.getDouble("nice"), document.getDouble("naughty"));
            }).get();
        });
    }

    public CompletionStage<UpdateResult> IncrementNiceCount(){
        return getCollection().updateOne(gte("nice", 0), inc("nice", 1));
    }

    public CompletionStage<UpdateResult> IncrementNaughtyCount(){
        return getCollection().updateOne(gte("naughty", 0), inc("naughty", 1));
    }

    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("non").getCollection("count");
    }

}