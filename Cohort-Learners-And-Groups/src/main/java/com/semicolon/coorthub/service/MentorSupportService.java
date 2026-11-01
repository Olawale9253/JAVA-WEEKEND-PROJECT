package com.semicolon.coorthub.service;

import com.semicolon.coorthub.model.Learner;

import java.util.ArrayDeque;
import java.util.Queue;

public class MentorSupportService {

    private final Queue<Learner> supportQueue;

    public MentorSupportService() {
        this.supportQueue = new ArrayDeque<>();
    }

    public void requestSupport(Learner learner) {
        supportQueue.add(learner);
    }

    public Learner attendNextLearner() {
        return supportQueue.poll();
    }

    public void removeLearnerFromQueue(Learner learner) {
        supportQueue.removeIf(queued -> queued.equals(learner));
    }

    public int getWaitingCount() {
        return supportQueue.size();
    }

    public boolean isWaiting(Learner learner) {
        return supportQueue.contains(learner);
    }
}