package com.dinoproblems.server;

import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.generators.VariousProblems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Katushka
 * on 02.05.2019.
 */
public class UserInfo {
    private String deviceId;
    private String name;
    private Multimap<String, Problem> solvedProblemsByTheme = HashMultimap.create();

    private Map<Difficulty, List<Problem>> variousProblems = new HashMap<>();
    private Map<Difficulty, Set<String>> solvedVariousProblems = new HashMap<>(); // for the initialization only

    private Map<Difficulty, Problem> currentProblemByDifficulty = new HashMap<>();
    private Problem currentProblem = null;

    private int points;

    UserInfo(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
    }

    String getDeviceId() {
        return deviceId;
    }

    String getName() {
        return name;
    }

    Collection<Problem> getSolvedProblemsByTheme(String theme) {
        return solvedProblemsByTheme.get(theme);
    }

    Problem getCurrentProblem(Difficulty difficulty) {
        return currentProblemByDifficulty.get(difficulty);
    }

    void setCurrentProblem(Problem currentProblem, Difficulty difficulty) {
        if (currentProblem == null) {
            this.currentProblemByDifficulty.remove(difficulty);
        } else {
            this.currentProblemByDifficulty.put(difficulty, currentProblem);
        }
        this.currentProblem = currentProblem;
    }

    void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public int getTotalScore() {
        return points;
    }

    Problem getRandomVariousProblem(Difficulty difficulty) {
        if (!variousProblems.containsKey(difficulty)) {
            initVariousProblems(difficulty);
        }
        if (variousProblems.get(difficulty).isEmpty()) {
            return null;
        }
        return variousProblems.get(difficulty).remove(variousProblems.get(difficulty).size() - 1);
    }

    private void initVariousProblems(Difficulty difficulty) {
        System.out.println("initVariousProblems");
        System.out.println("solvedVariousProblems = " + solvedVariousProblems);
        final ArrayList<Problem> problems = VariousProblems.INSTANCE.getProblems(difficulty)
                .stream()
                .filter(problem -> !solvedVariousProblems.containsKey(difficulty) ||
                        !solvedVariousProblems.get(difficulty).contains(problem.getProblemScenario().getScenarioId()))
                .collect(Collectors.toCollection(ArrayList::new));
        variousProblems.put(difficulty, problems);
        Collections.shuffle(problems);
    }

    boolean hasVariousProblems(Difficulty difficulty) {
        if (!variousProblems.containsKey(difficulty)) {
            initVariousProblems(difficulty);
        }
        return !variousProblems.get(difficulty).isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        return (deviceId != null ? deviceId.equals(userInfo.deviceId) : userInfo.deviceId == null)
                && (name != null ? name.equals(userInfo.name) : userInfo.name == null);
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    void addSolvedProblem(String theme, Problem problem, int points) {
        solvedProblemsByTheme.put(theme, problem);
        if (VariousProblems.THEME.equals(theme)) {
            if (!variousProblems.containsKey(problem.getDifficulty())) {
                if (!solvedVariousProblems.containsKey(problem.getDifficulty())) {
                    solvedVariousProblems.put(problem.getDifficulty(), new HashSet<>());
                }
                solvedVariousProblems.get(problem.getDifficulty()).add(problem.getProblemScenario().getScenarioId());
            } else {
                // very slow but in reality we should never get here
                final List<Problem> problems = variousProblems.get(problem.getDifficulty());
                for (int i = 0; i < problems.size(); i++) {
                    Problem problem1 =  problems.get(i);
                    if (problem1.getProblemScenario().getScenarioId().equals(problem.getProblemScenario().getScenarioId())) {
                        problems.remove(i);
                        break;
                    }
                }
            }
        }
        addPoints(points);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }
}
