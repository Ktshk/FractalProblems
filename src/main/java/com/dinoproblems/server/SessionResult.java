package com.dinoproblems.server;

import java.util.Collection;

//не делаем import так как в одном package
public class SessionResult {

    private final int problemSolved;
    private final int totalProblems;//кол-во полей clearAnswer
    private final int hints;//кол-во полей hintUsed

    //    private final int score;//кол-во баллов за сессию
    public SessionResult(Collection<Problem> problems) {
        int problemsSolved = 0;//счётчик решённых задач
        int problemsGiven = 0;//счётчик выданных задач
        int hintsGiven = 0;//счётчик выданных подсказок
        int points;//счётчик заработанных баллов
        for (Problem problem : problems) {

            if (problem.getState() == Problem.State.SOLVED) {
                problemsSolved++;
            }
            if (problem.getState() == Problem.State.SOLVED_WITH_HINT) {//в будущем поменять на SOLVED_WITH_HINTS
                hintsGiven++;
            }
            if (problem.getState() == Problem.State.SOLVED ||
                    problem.getState() == Problem.State.ANSWER_GIVEN) {
                problemsGiven++;
            }

        }
        problemSolved = problemsSolved;
        hints = hintsGiven;
        totalProblems = problemsGiven;


    }

    public int getProblemSolved() {//getter~Alt+Ins
        return problemSolved;
    }

    @Override
    public String toString() {//Ctrl+o//окончания+остальные поля
        switch (totalProblems) {
            case 1:
                if (problemSolved != 0)
                        if(hints!=0)
                            return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи, " + hints + " из которых с нашей помощью. Ждём нас снова!";
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
