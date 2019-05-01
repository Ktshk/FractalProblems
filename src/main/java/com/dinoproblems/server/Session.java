package com.dinoproblems.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Katushka on 10.02.2019.
 */
public class Session {
    private final String sessionId;
    private final SessionResult sessionResult = new SessionResult();
    private Problem currentProblem;

    private Multimap<String, Problem> solvedProblemsByTheme = HashMultimap.create();
    private List<Problem> variousProblems;
    private Problem.Difficulty currentDifficulty = null;
    private String lastServerResponse;


    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    public void setCurrentProblem(Problem currentProblem) {
        this.currentProblem = currentProblem;
        if (currentProblem != null) {
            solvedProblemsByTheme.put(currentProblem.getTheme(), currentProblem);
        }
    }

    public Problem.Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(Problem.Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public String getLastServerResponse() {
        return lastServerResponse;
    }

    public void setLastServerResponse(String lastServerResponse) {
        this.lastServerResponse = lastServerResponse;
    }

    public Multimap<String, Problem> getSolvedProblems() {
        return solvedProblemsByTheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return sessionId != null ? sessionId.equals(session.sessionId) : session.sessionId == null;
    }

    @Override
    public int hashCode() {
        return sessionId != null ? sessionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", currentProblem=" + currentProblem +
                '}';
    }

    @Nonnull
    public SessionResult getSessionResult(){
        return sessionResult;
    }

    @Nullable
    public Problem getRandomVariousProblem(Collection<Problem> allVariousProblems) {
        if (variousProblems == null) {
            variousProblems = new ArrayList<>(allVariousProblems);
            Collections.shuffle(variousProblems);
        }
        if (variousProblems.isEmpty()) {
            return null;
        }
        return variousProblems.remove(variousProblems.size() - 1);
    }

    public boolean hasVariousProblems(Collection<Problem> allVariousProblems) {
        if (variousProblems == null) {
            variousProblems = new ArrayList<>(allVariousProblems);
            Collections.shuffle(variousProblems);
        }
        return !variousProblems.isEmpty();
    }
}
