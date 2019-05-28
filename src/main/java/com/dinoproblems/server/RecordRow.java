package com.dinoproblems.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Katushka
 * on 27.05.2019.
 */
public class RecordRow {
    private final String userName;
    private final String userId;

    private int totalPoints = 0;
    private int totalProblemCount = 0;
    private Map<Problem.Difficulty, Integer> problemCountByDifficulty = new HashMap<>();

    private int position;

    public RecordRow(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return userId;
    }

    public String getUserId() {
        return userId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalProblemCount() {
        return totalProblemCount;
    }

    public int getSolvedProblems(Problem.Difficulty difficulty) {
        return problemCountByDifficulty.getOrDefault(difficulty, 0);
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setUserName(String userName) {
    }

    public void setId(String id) {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "RecordRow{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", totalPoints=" + totalPoints +
                ", totalProblemCount=" + totalProblemCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordRow recordRow = (RecordRow) o;

        if (userName != null ? !userName.equals(recordRow.userName) : recordRow.userName != null) return false;
        return userId != null ? userId.equals(recordRow.userId) : recordRow.userId == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    public void addPoints(int points, Problem.Difficulty difficulty) {
        totalPoints += points;
        if (totalPoints > 0) {
            totalProblemCount++;
            problemCountByDifficulty.put(difficulty, problemCountByDifficulty.getOrDefault(difficulty, 0) + 1);
        }
    }
}
