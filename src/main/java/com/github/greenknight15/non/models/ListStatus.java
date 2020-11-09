package com.github.greenknight15.non.models;

import com.github.greenknight15.non.models.Status;
import com.github.greenknight15.non.repositories.entities.NoNSubmission;

public class ListStatus {

    public Long NiceCount;
    public Long NaughtyCount;
    public NoNSubmission submission;

    public ListStatus(Long niceCount, Long naughtyCount, NoNSubmission submission) {
        this.NiceCount = niceCount;
        this.NaughtyCount = naughtyCount;
        this.submission = submission;
    }
}