package com.dinoproblems.server;

import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.ProblemTextBuilder;

import java.util.Collection;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.*;

//First stable ver got by Simar 22.04.2019
//не делаем import так как в одном package

public class SessionResult {

    private final int problemSolved;//кол-во решённых задач
    private final int totalProblems;//кол-во полей clearAnswer
    private final int hints;//кол-во полей hintUsed
    private final int total;//суммарное кол-во баллов
    public static int [] problemsSolved =new int [2];//счётчик решённых задач
    public static int [] problemsGiven=new int [2];//счётчик выданных задач
    public static int  [] hintsGiven=new int [2];//счётчик выданных подсказок
    public static int [] points=new int [2];//счётчик заработанных баллов
    public SessionResult(Collection<Problem> values) {


       // for (Problem problem : problems) {

           /* if (problem.getState() == Problem.State.SOLVED || problem.getState() == Problem.State.SOLVED_WITH_HINT) {
                if (problem.getDifficulty() == Problem.Difficulty.EASY) points += 2;
                else if (problem.getDifficulty() == Problem.Difficulty.MEDIUM) points += 4;
                else if (problem.getDifficulty() == Problem.Difficulty.DIFFICULT) points += 6;
                else points += 10;
                problemsSolved++;
                if (problemsSolved % 3 == 0) points++;
            }
            if (problem.getState() == Problem.State.SOLVED_WITH_HINT) {
                if (problem.getDifficulty() == Problem.Difficulty.EASY) points -= 1;
                else if (problem.getDifficulty() == Problem.Difficulty.MEDIUM) points -= 2;
                else if (problem.getDifficulty() == Problem.Difficulty.DIFFICULT) points -= 3;
                else points -= 5;
                hintsGiven++;
            }
            if (problem.getState() == Problem.State.SOLVED ||
                    problem.getState() == Problem.State.ANSWER_GIVEN) {
                if(problem.getDifficulty()==Problem.Difficulty.EASY) points-=2;
                else if(problem.getDifficulty()==Problem.Difficulty.MEDIUM) points-=4;
                else if(problem.getDifficulty()==Problem.Difficulty.DIFFICULT) points-=6;
                else  points-=10;
                problemsGiven++;
            }*/


        //}

        problemSolved = problemsSolved[0];
        hints = hintsGiven[0];
        totalProblems = problemsGiven[0];
        total = problemsSolved[1]+problemsGiven[1]+hintsGiven[1];


    }

    public void getProblemSolved(Problem.Difficulty difficulty) {//getter~Alt+Ins

        problemsSolved[0]++;
        switch (difficulty){
            case EASY: problemsSolved[1]=2;
            case MEDIUM: problemsSolved[1]=4;
            case DIFFICULT: problemsSolved[1]=6;
            case EXPERT:problemsSolved[1]=10;
        }

    }
    public void getProblemSolvedWithHint(Problem.Difficulty difficulty){
        hintsGiven[0]++;
        switch (difficulty){
            case EASY: hintsGiven[1]=-1;
            case MEDIUM: hintsGiven[1]=-2;
            case DIFFICULT: hintsGiven[1]=-3;
            case EXPERT:hintsGiven[1]=-5;
        }

    }
    public void getProblemAnswerGiven(Problem.Difficulty difficulty){

        problemsGiven[0]++;
        switch (difficulty){
            case EASY: problemsGiven[1]=-2;
            case MEDIUM: problemsGiven[1]=-4;
            case DIFFICULT: problemsGiven[1]=-6;
            case EXPERT:problemsGiven[1]=-10;
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
