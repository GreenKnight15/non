package com.github.greenknight15.non;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoNService {

    private int niceCount = 0;
    private int naughtyCount = 0;

    public CompletionStage<Count> GetCount() {
        return CompletableFuture.supplyAsync(() ->
            new Count(niceCount,naughtyCount)
        );
    }

    public CompletionStage<Count> IncrementNiceCount() {
        niceCount++;
        return CompletableFuture.supplyAsync(() ->
            new Count(niceCount,naughtyCount)
        );
    }

    public CompletionStage<Count> IncrementNaughtyCount() {
        naughtyCount++;
        return CompletableFuture.supplyAsync(() ->
            new Count(niceCount,naughtyCount)
        );
    }
}