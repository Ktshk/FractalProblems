package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.Problem.Difficulty;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Katushka on 21.04.2019.
 */
public class VariousProblems {
    public static final VariousProblems INSTANCE = new VariousProblems();
    public static final String THEME = "VARIOUS";

    private Multimap<Difficulty, Problem> problems = HashMultimap.create();

    private VariousProblems() {
        loadProblems();
    }

    private void loadProblems() {
        loadProblemsByDifficulty("easy.txt", Difficulty.EASY);
        loadProblemsByDifficulty("medium.txt", Difficulty.MEDIUM);
        loadProblemsByDifficulty("difficult.txt", Difficulty.DIFFICULT);
        loadProblemsByDifficulty("expert.txt", Difficulty.EXPERT);
    }

    private void loadProblemsByDifficulty(String fileName, Difficulty difficulty) {
        final BufferedReader difficultProblems = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
        try {
            String line;

            while ((line = difficultProblems.readLine()) != null) {
                final String[] tokens = line.split("\t");
                final String id = tokens[0];
                final String text = tokens[1];
                final String tts = tokens[2].isEmpty() ? null : tokens[2];
                final String hint = tokens[3];
                final int answer = Integer.valueOf(tokens[4]);
                final String textAnswer = tokens[5];
                final HashSet<String> possibleTextAnswers = Sets.newHashSet(textAnswer);
                for (int i = 6; i < tokens.length; i++) {
                    if (!tokens[i].isEmpty()) {
                        possibleTextAnswers.add(tokens[i]);
                    }
                }
                problems.put(difficulty, new ProblemWithPossibleTextAnswers(text, tts, answer, THEME,
                        possibleTextAnswers, hint, new ProblemScenarioImpl(THEME + "_" + id, true),
                        difficulty));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                difficultProblems.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<Problem> getProblems(Difficulty difficulty) {
        return problems.get(difficulty);
    }
}
