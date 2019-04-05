package com.dinoproblems.server;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Katushka on 10.02.2019.
 */
public class Session {
    private final String sessionId;
    private Problem currentProblem;
    private Set<Problem> solvedProblems = new HashSet<>();
    private Problem.Difficulty currentDifficulty = null;

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    public void setCurrentProblem(Problem currentProblem) {
        this.currentProblem = currentProblem;
        if (currentProblem != null) {
            solvedProblems.add(currentProblem);
        }
    }

    public Problem.Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(Problem.Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public Set<Problem> getSolvedProblems() {
        return solvedProblems;
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
    public SessionResult getSessionResult(){
        return new SessionResult(solvedProblems);//должно быть TaskResult
    }
}
