package com.dinoproblems.server;

import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.ProblemTextBuilder;

import java.util.Collection;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.*;
//First stable ver got by Simar 22.04.2019
//не делаем import так как в одном package
public class SessionResult {

    private final int problemSolved;
    private final int totalProblems;//кол-во полей clearAnswer
    private final int hints;//кол-во полей hintUsed
    private final int total;

    //    private final int score;//кол-во баллов за сессию
    public SessionResult(Collection<Problem> problems) {
        int problemsSolved = 0;//счётчик решённых задач
        int problemsGiven = 0;//счётчик выданных задач
        int hintsGiven = 0;//счётчик выданных подсказок
        int points=0;//счётчик заработанных баллов

        for (Problem problem : problems) {

            if (problem.getState() == Problem.State.SOLVED) {
                if(problem.getDifficulty()==Problem.Difficulty.EASY) points+=2;
                else if(problem.getDifficulty()==Problem.Difficulty.MEDIUM) points+=4;
                else if(problem.getDifficulty()==Problem.Difficulty.HARD) points+=6;
                else  points+=10;
                problemsSolved++;
                if(problemsSolved%3==0) points++;
            }
            if (problem.getState() == Problem.State.SOLVED_WITH_HINT) {//в будущем поменять на SOLVED_WITH_HINTS
                if(problem.getDifficulty()==Problem.Difficulty.EASY) points-=1;
                else if(problem.getDifficulty()==Problem.Difficulty.MEDIUM) points-=2;
                else if(problem.getDifficulty()==Problem.Difficulty.HARD) points-=3;
                else  points-=5;
                hintsGiven++;
            }
            if (problem.getState() == Problem.State.SOLVED ||
                    problem.getState() == Problem.State.ANSWER_GIVEN) {
                if(problem.getDifficulty()==Problem.Difficulty.EASY) points-=2;
                else if(problem.getDifficulty()==Problem.Difficulty.MEDIUM) points-=4;
                else if(problem.getDifficulty()==Problem.Difficulty.HARD) points-=6;
                else  points-=10;
                problemsGiven++;
            }

        }
        problemSolved = problemsSolved;
        hints = hintsGiven;
        totalProblems = problemsGiven;
        total=points;


    }

    public int getProblemSolved() {//getter~Alt+Ins
        return problemSolved;
    }

    @Override
    public String toString() {
        ProblemTextBuilder text = new ProblemTextBuilder();//Ctrl+o//окончания+остальные поля
        switch (totalProblems) {
            case 1:
                if (problemSolved != 0)
                        if(hints!=0){
                           text.append("Вы решили ");//верно разбивать на множественные text.append или всё в один?
                           text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                           text.append(" из ");
                           text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                           text.append(" предложенной задачи, ");
                           text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                           text.append(" из которых с нашей помощью. Всего набрали ");
                           text.append(String.valueOf(total), NumberWord.getStringForNumber(total, NEUTER, GeneratorUtils.Case.GENITIVE));
                           text.append(" баллов. Ждём вас снова!");//добавить генератор рандомных конечный фраз
                            //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи, " + hints + " из которых с нашей помощью. Ждём Вас снова!";}
                            return text.getText();}//как грамотно вернуть полученный итог?
                        else
                            return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи и обошлись без нашей помощи. Ждём Вас снова!";
                else
                    return "Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!";
                default:
                    if (problemSolved != 0)
                        if(hints!=0)
                            return "Вы решили " + problemSolved + " из " + totalProblems + " предложенных задач, " + hints + " из которых с нашей помощью. Ждём Вас снова!";
                        else
                            return "Вы решили " + problemSolved + " из " + totalProblems + " предложенных задач и обошлись без нашей помощи. Ждём Вас снова!";
                    else
                        return "Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!";

        }
    }
}
