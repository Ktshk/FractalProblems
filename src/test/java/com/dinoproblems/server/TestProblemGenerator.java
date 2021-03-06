package com.dinoproblems.server;

import com.dinoproblems.server.generators.*;
import com.dinoproblems.server.session.Session;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Katushka
 * on 13.03.2019.
 */
public class TestProblemGenerator {

    private static final ProblemGenerator GENERATOR = new SpacesGenerator();

    public void testAllGenerators() {
        for (ProblemGenerator generator : ProblemCollection.INSTANCE.getGenerators()) {
            System.out.println("************* Generator: " + generator);
            testGenerator(generator, 3);
        }
    }

    @Ignore
    @Test
    public void testGenerateProblem() {
        testGenerator(GENERATOR, 10);
    }

    @Ignore
    @Test
    public void testGenerateExpertProblems() {
        int count = 0;
        Problem problem;
        final UserInfo userInfo = new UserInfo("test", "test", "test");
        while (count < 100 && ((problem = ProblemCollection.INSTANCE.generateProblem(userInfo, Problem.Difficulty.EXPERT, null)) != null)) {
            // TODO
//            problem.setState(Problem.State.SOLVED);
            userInfo.addSolvedProblem(problem.getTheme(), problem, 0, null);

            System.out.println("Problem: " + problem.getText());
            System.out.println("Scenario: " + problem.getProblemScenario());
            System.out.println("Difficulty: " + problem.getDifficulty());
            System.out.println("Answer: " + problem.getTextAnswer());
            System.out.println("*** ");

            count++;
        }

        System.out.println("count = " + count);
    }

    @Test
    public void testProblemCollectionGeneration() {
        final Session session = new Session(new UserInfo("test", "test", "test"), "test", "test");

        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            if (difficulty == Problem.Difficulty.EXPERT) {
                continue;
            }
            System.out.println("************* " + difficulty);
            session.setCurrentDifficulty(difficulty);

            for (int i = 0; i < 30; i++) {
//                solveProblem(problemCollection, session, Problem.State.SOLVED_WITH_HINT);
                solveProblem(session, UserInfo.ProblemState.SOLVED);
                solveProblem(session, UserInfo.ProblemState.ANSWER_GIVEN);
            }
        }
    }

    @Ignore
    @Test
    public void testDifferentProblemsCount() {
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            System.out.println("************* " + difficulty);
            Set<Problem> solvedProblems = new HashSet<>();
            int count = VariousProblems.INSTANCE.getProblems(difficulty).size();
            for (ProblemGenerator generator : ProblemCollection.INSTANCE.getGenerators()) {
//                System.out.println("************* Generator: " + generator);
                int i = 0;
                for (; i < 100; i++) {
                    final ProblemGenerator.ProblemAvailability problemAvailability = generator.hasProblem(solvedProblems, difficulty, null /* TODO */);
                    if (problemAvailability == null || problemAvailability.getType() == ProblemGenerator.ProblemAvailabilityType.minorScenarioChanges) {
                        break;
                    }
                    final Problem problem = generator.generateProblem(difficulty, problemAvailability);
                    // TODO
                    //                    problem.setState(Problem.State.SOLVED);
                    solvedProblems.add(problem);
                }
//                System.out.println("Count: " + i);
                count += i;
            }
            System.out.println("Total: " + count);
        }
    }


    private void solveProblem(Session session, UserInfo.ProblemState state) {
        final Problem problem = session.getNextProblem();
        if (problem == null) {
            System.out.println("NO MORE PROBLEMS");
            return;
        }
        session.setCurrentProblem(problem);

        System.out.println("Problem: " + problem.getText());
        System.out.println("Scenario: " + problem.getProblemScenario());
        System.out.println("Difficulty: " + problem.getDifficulty());
        System.out.println("Answer: " + problem.getTextAnswer());
        System.out.println("State: " + state);
        System.out.println("*** ");
//        problem.setState(state);
        session.updateScore(problem);
    }

    private void testGenerator(ProblemGenerator generator, int problemCount) {
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            System.out.println("************* " + difficulty);

            Set<Problem> solvedProblems = new HashSet<>();

            for (int i = 0; i < problemCount; i++) {
                final ProblemGenerator.ProblemAvailability problemAvailability = generator.hasProblem(solvedProblems, difficulty, null);
                if (problemAvailability == null) {
                    System.out.println("NO MORE PROBLEMS!!!");
                    break;
                }
                final Problem problem = generator.generateProblem(difficulty, problemAvailability);
                System.out.println("Problem " + (i + 1) + ": " + problem.getText());
//                System.out.println("Hint: " + problem.getNextHint());
                System.out.println("Answer: " + problem.getTextAnswer());
                System.out.println("*** ");

//                problem.setState(UserInfo.ProblemState.SOLVED);
                solvedProblems.add(problem);
            }
        }
    }
}
