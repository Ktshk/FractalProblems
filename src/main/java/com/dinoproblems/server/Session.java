package com.dinoproblems.server;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Katushka on 10.02.2019.
 */
public class Session {
    private final String sessionId;
    private Problem currentProblem;
    private Set<Problem> problemsSolved = new HashSet<>();

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    public void setCurrentProblem(Problem currentProblem) {
        this.currentProblem = currentProblem;
        if (currentProblem != null) {
            problemsSolved.add(currentProblem);
        }
    }

    public Set<Problem> getProblemsSolved() {
        return problemsSolved;
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
}
