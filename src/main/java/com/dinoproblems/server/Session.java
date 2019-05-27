package com.dinoproblems.server;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Katushka on 10.02.2019.
 */
public class Session {
    private final String sessionId;
    private final SessionResult sessionResult = new SessionResult();
    private final Map<Problem.Difficulty, Problem> nextProblem = new HashMap<>();

    private Problem.Difficulty currentDifficulty = null;
    private String lastServerResponse;

    private UserInfo userInfo = null;
    private String userName;

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public Session(UserInfo userInfo, String sessionId) {
        this.userInfo = userInfo;
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Problem getCurrentProblem() {
        return userInfo.getCurrentProblem(getCurrentDifficulty());
    }

    public void setCurrentProblem(Problem currentProblem) {
        userInfo.setCurrentProblem(currentProblem, getCurrentDifficulty());
    }

    public Problem.Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(Problem.Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;

        populateNextProblem();
    }

    private void generateNextProblem() {
        final Problem currentDifficultyProblem = ProblemCollection.INSTANCE.generateProblem(this, getCurrentDifficulty());
        if (currentDifficultyProblem == null) {
            nextProblem.remove(getCurrentDifficulty());
        } else {
            nextProblem.put(getCurrentDifficulty(), currentDifficultyProblem);
        }
    }

    private void populateNextProblem() {
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            if (!nextProblem.containsKey(difficulty)) {
                final Problem problem = ProblemCollection.INSTANCE.generateProblem(this, difficulty);
                if (problem != null) {
                    nextProblem.put(difficulty, problem);
                }
            }
        }
    }

    public String getLastServerResponse() {
        return lastServerResponse;
    }

    public void setLastServerResponse(String lastServerResponse) {
        this.lastServerResponse = lastServerResponse;
    }

    public Collection<Problem> getSolvedProblems(String theme) {
        return userInfo.getSolvedProblemsByTheme(theme);
    }

    public int updateScore(Problem problem) {
        userInfo.setCurrentProblem(null, getCurrentDifficulty());
        final int points = sessionResult.updateScore(problem);
        userInfo.addSolvedProblem(problem.getTheme(), problem, points);

        generateNextProblem();
        return points;
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
                '}';
    }

    @Nonnull
    public SessionResult getSessionResult() {
        return sessionResult;
    }

    @Nullable
    public Problem getRandomVariousProblem() {
        return userInfo.getRandomVariousProblem(getCurrentDifficulty());
    }

    public boolean hasVariousProblems() {
        return userInfo.hasVariousProblems(getCurrentDifficulty());
    }

    @Nullable
    public Problem getNextProblem() {
        return nextProblem.getOrDefault(getCurrentDifficulty(), null);
    }



}
