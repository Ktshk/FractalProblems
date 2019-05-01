package com.dinoproblems.server;

import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.ProblemTextBuilder;

import java.util.Collection;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.*;

//First stable ver got by Simar 22.04.2019
//не делаем import так как в одном package

public class SessionResult {

    private int problemSolved = 0;//кол-во решённых задач
    private int totalProblems = 0;//кол-во полей clearAnswer
    private int hints = 0;//кол-во полей hintUsed
    private int total = 0;//суммарное кол-во баллов

    public SessionResult() {
    }

    public void updateScore(Problem problem) {
        totalProblems++;
        if (problem.getState() == Problem.State.SOLVED) {
            if (problem.getDifficulty() == Problem.Difficulty.EASY) total += 2;
            else if (problem.getDifficulty() == Problem.Difficulty.MEDIUM) total += 4;
            else if (problem.getDifficulty() == Problem.Difficulty.HARD) total += 6;
            else total += 10;
            problemSolved++;
//            if (problemSolved % 3 == 0) total++;
        }
        if (problem.getState() == Problem.State.SOLVED_WITH_HINT) {
            if (problem.getDifficulty() == Problem.Difficulty.EASY) total += 1;
            else if (problem.getDifficulty() == Problem.Difficulty.MEDIUM) total += 2;
            else if (problem.getDifficulty() == Problem.Difficulty.HARD) total += 3;
            else total -= 5;
            hints++;
        }

    }

    public ProblemTextBuilder getResult() {
        ProblemTextBuilder text = new ProblemTextBuilder();//Ctrl+o//окончания+остальные поля
        switch (totalProblems) {
            case 1:
                if (problemSolved != 0)
                    if (hints != 0) {
                        text.append("Вы решили ");
                        text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенной задачи, ");
                        text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из которых с нашей помощью. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, NEUTER, GeneratorUtils.Case.GENITIVE));
                        text.append(" баллов. Ждём вас снова!");//добавить генератор рандомных конечный фраз
                        //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи, " + hints + " из которых с нашей помощью. Ждём Вас снова!";}
                        return text;
                    }//как грамотно вернуть полученный итог?
                    else {
                        text.append("Вы решили ");
                        text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенной задачи");
                        //  text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" и обошлись без нашей помощи. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, NEUTER, GeneratorUtils.Case.GENITIVE));
                        text.append(" баллов. Ждём вас снова!");
                        return text;
                    }
                    //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи и обошлись без нашей помощи. Ждём Вас снова!";
                else {
                    text.append("Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!");
                    return text;
                }
                //  return "Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!";
            default:
                if (problemSolved != 0)
                    if (hints != 0) {
                        text.append("Вы решили ");
                        text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенных задач, ");
                        text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                        text.append(", из которых с нашей помощью. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, NEUTER, GeneratorUtils.Case.GENITIVE));
                        text.append(" баллов. Ждём вас снова!");
                        return text;
                    }
                    //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенных задач, " + hints + " из которых с нашей помощью. Ждём Вас снова!";
                    else {
                        text.append("Вы решили ");
                        text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенной задачи, ");
                        // text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" и обошлись без нашей помощи. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, NEUTER, GeneratorUtils.Case.GENITIVE));
                        text.append(" баллов. Ждём вас снова!");
                        return text;
                    }//return "Вы решили " + problemSolved + " из " + totalProblems + " предложенных задач и обошлись без нашей помощи. Ждём Вас снова!";
                else
                // return "Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!";
                {
                    text.append("Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!");
                    return text;
                }

        }
    }
}
