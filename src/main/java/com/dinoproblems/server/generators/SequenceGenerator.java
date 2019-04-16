package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemCollection;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;
import static com.dinoproblems.server.Problem.Difficulty.EASY;

/*
Created by Simar 16.03.19
Modified by Simar 14.04.19
  */
public class SequenceGenerator implements ProblemGenerator {
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int type=randomInt(1,3);//определение типа задач
        //1-арифметическиа дейстивия с константой, 2-взаимодействие с исходными элементами последовательности
        int[] sequence = new int[7];//данная последовательность из 7 натуральных чисел;
        int[] sequenceShow = new int[6];//выводимая в условие последовательность
        int first =difficulty==EASY ? randomInt(1, 5):randomInt(5,15);//определение первого элемента последовательности
        int i = 0;
        int operand1 = difficulty==EASY ? randomInt(2, 10):randomInt(10,30);//операнд для сложения/вычитания
        int operand2 = difficulty==EASY ? randomInt(2, 4):randomInt(2,6);//операнд для произвдения/второй операнд для типа 2
        sequence[0] = first;
        String text = null;//итоговый текст задачи
        int answer = 0;
        String hint = null;
        switch (type) {
            case 1:
                if (sequence[0]==1) {
                    for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] * operand2;
                }

                if (sequence[0] == 64) {
                    for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] / 2;

                } else if (sequence[0] > 6 * operand1) {
                    for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] - operand1;
                } else {
                    for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] + operand1;
                }
                sequenceShow = Arrays.copyOf(sequence, sequenceShow.length);
                answer = sequence[6];
                text = "Дана последовательность из шести натуральных чисел. " + Arrays.toString(sequenceShow) + " Какое число может быть следующим?";
                hint = "Посмотрите, как второе число отличается от первого и как третье от второго";
                break;
            case 2:
                if (sequence[0]==2) {
                    for (i = 1; i <= 6; i++) sequence[i] = sequence[i - 1] * sequence[0];
                } else if (sequence[0] < 20) {
                    sequence[1] = sequence[0];
                    for (i = 2; i <= 6; i++) sequence[i] = sequence[i - 1] + sequence[i - 2];
                } else {
                    sequence[1] = sequence[0] + operand2;
                    for (i = 2; i <= 6; i++)
                        if (i % 2 == 0) sequence[i] = sequence[i - 2] + operand1;
                        else sequence[i] = sequence[i - 2] + operand2;
                }
                sequenceShow = Arrays.copyOf(sequence, sequenceShow.length);
                answer = sequence[6];
                text = "Дана последовательность из шести натуральных чисел. " + Arrays.toString(sequenceShow) + " Какое число может быть следующим?";
                hint = "Обратите внимание на связь между первым, третьим и пятым элементом последовательности, а также между вторым, четвёртым и шестым";
                break;
        }

        String possibleAnswer = Integer.toString(answer);//?
        final HashSet<String> possibleTextAnswers = Sets.newHashSet(Integer.toString(answer));//?
        possibleTextAnswers.add(possibleAnswer);//?
        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.SEQUENCE, possibleTextAnswers, hint);

    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(EASY, Problem.Difficulty.MEDIUM);
    }

}
