package com.dinoproblems.server;

import com.dinoproblems.server.generators.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Katushka
 * on 13.03.2019.
 */
public class TestProblemGenerator {

    private static final ProblemGenerator GENERATOR = new CombinatoricsGenerator();

    @Test
    public void testAllGenerators() {
        for (ProblemGenerator generator : ProblemCollection.INSTANCE.getGenerators()) {
            System.out.println("************* Generator: " + generator);
            testGenerator(generator, 3);
        }
    }

    @Test
    public void testGenerateProblem() {
        testGenerator(GENERATOR, 10);
    }

    @Test
    public void testProblemCollectionGeneration() {
        final ProblemCollection problemCollection = ProblemCollection.INSTANCE;
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            System.out.println("************* " + difficulty);
            final Session session = new Session("test");
            session.setCurrentDifficulty(difficulty);

            for (int i = 0; i < 15; i++) {
//                solveProblem(problemCollection, session, Problem.State.SOLVED_WITH_HINT);
                solveProblem(problemCollection, session, Problem.State.SOLVED);
//                solveProblem(problemCollection, session, Problem.State.ANSWER_GIVEN);
            }
        }
    }

    @Test
    public void testDifferentProblemsCount() {
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            System.out.println("************* " + difficulty);
            Set<Problem> solvedProblems = new HashSet<>();
            int count = 0;
            for (ProblemGenerator generator : ProblemCollection.INSTANCE.getGenerators()) {
//                System.out.println("************* Generator: " + generator);
                int i = 0;
                for (; i < 100; i++) {
                    final ProblemGenerator.ProblemAvailability problemAvailability = generator.hasProblem(solvedProblems, difficulty);
                    if (problemAvailability == null || problemAvailability.getType() == ProblemGenerator.ProblemAvailabilityType.minorScenarioChanges) {
                        break;
                    }
                    final Problem problem = generator.generateProblem(difficulty, problemAvailability);
                    problem.setState(Problem.State.SOLVED);
                    solvedProblems.add(problem);
                }
//                System.out.println("Count: " + i);
                count += i;
            }
            System.out.println("Total: " + count);
        }
    }


    private void solveProblem(ProblemCollection problemCollection, Session session, Problem.State state) {
        final Problem problem = problemCollection.generateProblem(session);
        session.setCurrentProblem(problem);

        System.out.println("Problem: " + problem.getText());
        final String hint = problem.getHint();
        if (state == Problem.State.SOLVED_WITH_HINT) {
            System.out.println("Hint: " + hint);
        }
        System.out.println("Answer: " + problem.getTextAnswer());
        System.out.println("*** ");
        problem.setState(state);
    }

    private void testGenerator(ProblemGenerator generator, int problemCount) {
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            System.out.println("************* " + difficulty);

            Set<Problem> solvedProblems = new HashSet<>();

            for (int i = 0; i < problemCount; i++) {
                final ProblemGenerator.ProblemAvailability problemAvailability = generator.hasProblem(solvedProblems, difficulty);
                if (problemAvailability == null) {
                    System.out.println("NO MORE PROBLEMS!!!");
                    break;
                }
                final Problem problem = generator.generateProblem(difficulty, problemAvailability);
                System.out.println("Problem " + (i + 1) + ": " + problem.getText());
                System.out.println("Hint: " + problem.getHint());
                System.out.println("Answer: " + problem.getTextAnswer());
                System.out.println("*** ");

                problem.setState(Problem.State.SOLVED);
                solvedProblems.add(problem);
            }
        }
    }
}
