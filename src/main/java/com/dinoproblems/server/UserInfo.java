package com.dinoproblems.server;

import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.generators.QuestProblems;
import com.dinoproblems.server.generators.VariousProblems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Katushka
 * on 02.05.2019.
 */
public class UserInfo {
    public enum ProblemState {
        ANSWER_GIVEN, SOLVED, SOLVED_WITH_HINT
    }

    private String deviceId;
    private String name;
    private final String clientId;

    private Multimap<String, Problem> solvedProblemsByTheme = HashMultimap.create();

    private final Map<Difficulty, List<Problem>> variousProblems = new HashMap<>();
    private Set<String> solvedVariousProblems = new HashSet<>(); // for the initialization only

    private Map<Difficulty, Problem> currentProblemByDifficulty = new HashMap<>();

    private Problem currentProblem = null;
    private Problem expertProblem = null;
    private Calendar expertProblemDate = null;

    private Map<Problem, ProblemState> problemStates = new HashMap<>();
    private Map<Problem, Integer> problemCurrentHint = new HashMap<>();

    private int points;
    private int expertPoints;
    private Map<String, Integer> questPoints = new HashMap<>();

    public UserInfo(String deviceId, String name, String clientId) {
        this.deviceId = deviceId;
        this.name = name;
        this.clientId = clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    Collection<Problem> getSolvedProblemsByTheme(String theme) {
        return solvedProblemsByTheme.get(theme);
    }

    public Problem getCurrentProblem(Difficulty difficulty) {
        return currentProblemByDifficulty.get(difficulty);
    }

    public void setCurrentProblem(Problem currentProblem, Difficulty difficulty) {
        if (currentProblem == null) {
            this.currentProblemByDifficulty.remove(difficulty);
        } else {
            this.currentProblemByDifficulty.put(difficulty, currentProblem);
        }
        this.currentProblem = currentProblem;
    }

    public void setProblemState(Problem problem, ProblemState state) {
        problemStates.put(problem, state);
    }

    @Nullable
    public ProblemState getProblemState(Problem problem) {
        return problemStates.getOrDefault(problem, null);
    }

    public String getNextHint(Problem problem) {
        Integer currentHint = problemCurrentHint.getOrDefault(problem, 0);
        String nextHint = problem.getNextHint(currentHint);
        problemCurrentHint.put(problem, currentHint + 1);
        return nextHint;
    }

    public String getLastHint(Problem problem) {
        Integer currentHint = problemCurrentHint.getOrDefault(problem, 0);
        return problem.getLastHint(currentHint);
    }

    public boolean hasHint(Problem problem) {
        Integer currentHint = problemCurrentHint.getOrDefault(problem, 0);
        return problem.hasHint(currentHint);
    }

    public boolean wasHintGiven(Problem problem) {
        Integer currentHint = problemCurrentHint.getOrDefault(problem, 0);
        return problem.wasHintGiven(currentHint);
    }


    private void addPoints(int pointsToAdd, Problem problem, @Nullable QuestProblems currentQuest) {
        if (currentQuest != null && !currentQuest.getProblem(0).getProblemScenario().getScenarioId().equals(problem.getProblemScenario().getScenarioId())) {
            currentQuest = null;
        }
        if (problem.getDifficulty() == Difficulty.EXPERT) {
            this.expertPoints += pointsToAdd;
            if (currentQuest != null) {
                if (!questPoints.containsKey(currentQuest.getName())) {
                    questPoints.put(currentQuest.getName(), pointsToAdd);
                } else {
                    questPoints.put(currentQuest.getName(), questPoints.get(currentQuest.getName()) + pointsToAdd);
                }
            }
        } else {
            this.points += pointsToAdd;
        }
    }

    public int getTotalScore() {
        return points;
    }

    public int getExpertScore() {
        return expertPoints;
    }

    public int getQuestPoints(String questName) {
        return questPoints.getOrDefault(questName, 0);
    }

    public boolean hasProblemOfTheDay(String timeZone) {
        return !(expertProblemDate == null || isDayProblemExpired(timeZone));
    }

    public Problem getProblemOfTheDay(String timeZone) {
        if (expertProblemDate == null || isDayProblemExpired(timeZone)) {
            System.out.println("Problem of the day is expired!");
            System.out.println("expertProblemDate = " + expertProblemDate);
            expertProblemDate = Calendar.getInstance(TimeZone.getTimeZone(timeZone));

            if (expertProblem != null) {
                System.out.println("Last expert problem: " + expertProblem);
                if (expertProblem.getTheme().equals(VariousProblems.THEME)) {
                    if (!variousProblems.containsKey(Difficulty.EXPERT)) {
                        initVariousProblems(Difficulty.EXPERT);
                    }
                    variousProblems.get(Difficulty.EXPERT).add(0, expertProblem);
                    for (Problem problem : variousProblems.get(Difficulty.EXPERT)) {
                        System.out.println("problem.getProblemScenario().getScenarioId() = " + problem.getProblemScenario().getScenarioId());
                    }
                }
            }

            expertProblem = ProblemCollection.INSTANCE.generateProblem(this, Difficulty.EXPERT, expertProblemDate);

            DataBaseService.INSTANCE.saveProblemOfTheDay(this, expertProblem, expertProblemDate);
        }
        return expertProblem;
    }

    private boolean isDayProblemExpired(String timeZone) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));

        final boolean result = calendar.get(Calendar.YEAR) != expertProblemDate.get(Calendar.YEAR)
                || calendar.get(Calendar.MONTH) != expertProblemDate.get(Calendar.MONTH)
                || calendar.get(Calendar.DAY_OF_MONTH) != expertProblemDate.get(Calendar.DAY_OF_MONTH);
        System.out.println("Problem of the day is expired: " + result);
        return result;
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
        System.out.println("initVariousProblems " + name);
        System.out.println("solvedVariousProblems = " + solvedVariousProblems);
        final ArrayList<Problem> problems = VariousProblems.INSTANCE.getProblems(difficulty)
                .stream()
                .filter(problem -> !solvedVariousProblems.contains(problem.getProblemScenario().getScenarioId()))
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

    public void addSolvedProblem(String theme, Problem problem, int points, @Nullable QuestProblems currentQuest) {
        solvedProblemsByTheme.put(theme, problem);
        String scenarioId = problem.getProblemScenario().getScenarioId();
        if (VariousProblems.THEME.equals(theme)) {
            if (!variousProblems.containsKey(problem.getDifficulty())) {
                solvedVariousProblems.add(scenarioId);
            } else {
                final List<Problem> problems = variousProblems.get(problem.getDifficulty());
                for (int i = 0; i < problems.size(); i++) {
                    Problem problem1 = problems.get(i);
                    if (problem1.getProblemScenario().getScenarioId().equals(scenarioId)) {
                        problems.remove(i);
                        break;
                    }
                }
            }
        }
        if (currentQuest != null && !currentQuest.getProblem(0).getProblemScenario().getScenarioId().equals(scenarioId)) {
            currentQuest = null;
        }
        addPoints(points, problem, currentQuest);
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

    public String getClientId() {
        return clientId;
    }

    public void setExpertProblem(Problem problem, Calendar calendar) {
        System.out.println("Set expert problem: expertProblem = " + expertProblem + "; expertProblemDate = " + expertProblemDate);
        this.expertProblem = problem;
        this.expertProblemDate = calendar;
    }
}
