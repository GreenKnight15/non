package com.github.greenknight15.non.models;

import com.github.greenknight15.non.repositories.entities.NoNSubmission;

import java.util.Optional;

public class ListStatus {

    public Long NiceCount;
    public Long NaughtyCount;
    public Optional<NoNSubmission> submission;

    public ListStatus(Long niceCount, Long naughtyCount, Optional<NoNSubmission> submission) {
        this.NiceCount = niceCount;
        this.NaughtyCount = naughtyCount;
        this.submission = submission;
    }
}