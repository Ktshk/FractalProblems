package com.dinoproblems.server;

import com.dinoproblems.server.generators.VariousProblems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Katushka on 02.05.2019.
 */
public class UserInfo {
    private String deviceId;
    private String name;
    private Multimap<String, Problem> solvedProblemsByTheme = HashMultimap.create();
    private List<Problem> variousProblems;
    private Problem currentProblem;
    private int points;

    public UserInfo(String deviceId, String name, Multimap<String, Problem> solvedProblemsByTheme) {
        this.deviceId = deviceId;
        this.name = name;
        this.solvedProblemsByTheme = solvedProblemsByTheme;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Multimap<String, Problem> getSolvedProblemsByTheme() {
        return solvedProblemsByTheme;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    public void setCurrentProblem(Problem currentProblem) {
        this.currentProblem = currentProblem;
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public Problem getRandomVariousProblem(Problem.Difficulty difficulty) {
        if (variousProblems == null) {
            variousProblems = new ArrayList<>(VariousProblems.INSTANCE.getProblems(difficulty));
            Collections.shuffle(variousProblems);
        }
        if (variousProblems.isEmpty()) {
            return null;
        }
        return variousProblems.remove(variousProblems.size() - 1);
    }

    public boolean hasVariousProblems(Problem.Difficulty difficulty) {
        if (variousProblems == null) {
            variousProblems = new ArrayList<>(VariousProblems.INSTANCE.getProblems(difficulty));
            Collections.shuffle(variousProblems);
        }
        return !variousProblems.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (deviceId != null ? !deviceId.equals(userInfo.deviceId) : userInfo.deviceId != null) return false;
        return name != null ? name.equals(userInfo.name) : userInfo.name == null;
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
