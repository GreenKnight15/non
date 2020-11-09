package com.github.greenknight15.non;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.inc;
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
    NoNRepository nonRepository;

    @Inject ReactiveMongoClient mongoClient;

    private static final Logger LOG = Logger.getLogger(NoNService.class);

//    public CompletionStage<Count> getCount() {
//
//        return getCollection().find().collectItems().first().map(document ->{
//                    return new Count(document.getDouble("nice"), document.getDouble("naughty"));
//                }).subscribeAsCompletionStage();
//    }

    public CompletionStage<UpdateResult> IncrementNiceCount(){
        return getCollection().updateOne(gte("nice", 0), inc("nice", 1)).subscribeAsCompletionStage();
    }

    public CompletionStage<UpdateResult> IncrementNaughtyCount(){
        return getCollection().updateOne(gte("naughty", 0), inc("naughty", 1)).subscribeAsCompletionStage();
    }

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


    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("non").getCollection("count");
    }
}