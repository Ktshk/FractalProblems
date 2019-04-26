package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static com.dinoproblems.server.utils.GeneratorUtils.findAvailableScenario;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/*
Created by Simar 16.03.19
Easy modified by Simar 14.04.19
Hard modified by Simar 18.04.19
  */
public class SequenceGenerator implements ProblemGenerator {

    private final static ProblemScenario DEFAULT_SCENARIO = new ProblemScenarioImpl(ProblemCollection.SEQUENCE);
    private final static ProblemScenario ARITHMETIC = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "ARITHMETIC");
    private final static ProblemScenario DOUBLEARITHMETIC = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "DOUBLEARITHMETIC");
    private final static ProblemScenario GEOMETRIC = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "GEOMETRIC");
    private final static ProblemScenario MUL_ON_TWO = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "MUL_ON_TWO");
    private final static ProblemScenario PARITY_IMPARITY = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "PARITY_IMPARITY");
    private final static ProblemScenario FIBONACCI = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "FIBONACCI");
    private final static ProblemScenario INTERESTING = new ProblemScenarioImpl(ProblemCollection.SEQUENCE + "_" + "INTERESTING");
    private static final ProblemScenario[] SCENARIOS = {ARITHMETIC, DOUBLEARITHMETIC, GEOMETRIC, MUL_ON_TWO, PARITY_IMPARITY, FIBONACCI, INTERESTING};

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {

        //int type=randomInt(1,3);//определение типа задач
        int type;//= randomInt(1, 3);//определение типа задач
        //1-арифметическиа дейстивия с константой, 2-взаимодействие с исходными элементами последовательности
        int[] sequence = new int[7];//данная последовательность из 7 натуральных чисел;
        int[] sequenceShow = new int[6];//выводимая в условие последовательность
        int first;// = difficulty == EASY ? randomInt(1, 5) : randomInt(5, 15);//определение первого элемента последовательности
        int i = 0;
        int operand1 = 0;// = difficulty == EASY ? randomInt(2, 10) : randomInt(10, 30);// малый операнд для одинарной арифметической прогрессии
        int operand2 = 0;// = difficulty == EASY ? randomInt(2, 4) : randomInt(2, 6);//большой операнд для двойной арифметической прогрессии
        int operand3 = 0;//операнд для геометрической прогрессии
        String text = null;//итоговый текст задачи
        int answer = 0;
        String hint = null;
        ProblemScenario scenario = problemAvailability.getScenario();
        switch (difficulty) {
            case EASY:
                operand1 = randomInt(3, 5);
                first = randomInt(1, 26);
                sequence[0] = first;
                if (scenario.equals(ARITHMETIC))
                    ArithmeticSequence(sequence, operand1);
                else if (scenario.equals(DOUBLEARITHMETIC)) {
                    if (sequence[0] % 2 == 0) {
                        for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] + 2;
                    } else {
                        for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] + 1;
                    }
                }
                break;
            case MEDIUM:
                operand1 = randomInt(3, 5);
                operand2 = randomInt(5, 10);
                first = randomInt(1, 51);
                sequence[0] = first;
                if (scenario.equals(DOUBLEARITHMETIC))
                    DoubleArithmeticSequence(sequence, operand1, operand2);
                else if (scenario.equals(ARITHMETIC))
                    ArithmeticSequence(sequence, operand2);
                break;
            case DIFFICULT:
                type = randomInt(1, 6);
                operand1 = randomInt(3, 5);
                operand2 = randomInt(5, 10);
                operand3 = randomInt(2, 4);
                first = randomInt(1, 76);
                sequence[0] = first;
                if (scenario.equals(DOUBLEARITHMETIC))
                    DoubleArithmeticSequence(sequence, operand2, operand1);
                else if (scenario.equals(GEOMETRIC)) {
                    if (sequence[0] != 6 * operand3) sequence[0] = 1;
                    GeometricSequence(sequence, operand3);
                } else if (scenario.equals(FIBONACCI)) {
                    sequence[1] = sequence[0];
                    for (i = 2; i <= 6; i++) sequence[i] = sequence[i - 1] + sequence[i - 2];
                } else if (scenario.equals(MUL_ON_TWO)) {
                    sequence[0] = 2;
                    for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] * 2;
                } else if (scenario.equals(PARITY_IMPARITY)) {
                    if (operand2 >= 6) {
                        for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] + operand2 - i;
                    } else for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] + operand2 + i;
                }
                break;
            case EXPERT:
                if (scenario.equals(INTERESTING)) {//TODO singleProblem???

                    type = randomInt(1, 3);
                    int[] simpleSequence = {2, 3, 5, 7, 11, 13, 17};
                    switch (type) {
                        case 1:
                            for (i = 0; i <= 6; i++)
                                sequence[i] = (i + 1) * (i + 2);
                            break;
                        case 2:
                            for (i = 0; i <= 6; i++)
                                sequence = Arrays.copyOf(simpleSequence, sequence.length);
                            break;

                    }
                }

                break;
        }

        sequenceShow = Arrays.copyOf(sequence, sequenceShow.length);
        answer = sequence[6];
        text = "Дана последовательность из шести натуральных чисел. " + Arrays.toString(sequenceShow) + " Какое число может быть следующим?";
        hint = "Посмотрите, как второе число отличается от первого и как третье от второго";


        String possibleAnswer = Integer.toString(answer);//?
        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(answer));//?
        possibleTextAnswers.add(possibleAnswer);//?
        return new

                ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.SEQUENCE, possibleTextAnswers, hint, scenario, difficulty);


    }

    private void DoubleArithmeticSequence(int[] sequence, int operand1, int operand2) {
        int i;
        if (sequence[0] > 6 * operand1) {
            for (i = 1; i <= 6; i++) {
                if (i % 2 == 1)
                    sequence[i] = sequence[i - 1] - operand1;
                else sequence[i] = sequence[i - 2] + operand2 - operand1;
            }
        } else {
            for (i = 1; i <= 6; i++) {
                if (i % 2 == 1)
                    sequence[i] = sequence[i - 1] + operand1;
                else sequence[i] = sequence[i - 2] + operand2 - operand1;
            }
        }
    }

    private void ArithmeticSequence(int[] sequence, int operand1) {
        int i;
        if (sequence[0] > 6 * operand1) {
            for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] - operand1;
        } else {
            for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] + operand1;
        }
    }

    private void GeometricSequence(int[] sequence, int operand3) {
        int i;
        if (sequence[0] == 1)
            for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] * operand3;
        else for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] / operand3;


    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {


        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(ARITHMETIC, DOUBLEARITHMETIC), new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(ARITHMETIC, DOUBLEARITHMETIC), new HashSet<>());
            case DIFFICULT:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(FIBONACCI, PARITY_IMPARITY,MUL_ON_TWO), Sets.newHashSet(GEOMETRIC,DOUBLEARITHMETIC));
            case EXPERT:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(INTERESTING), new HashSet<>());
        }
        throw new IllegalArgumentException();
    }
}


