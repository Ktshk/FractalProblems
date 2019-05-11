package com.dinoproblems.server;

import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.ProblemTextBuilder;

import java.util.Collection;

import static com.dinoproblems.server.utils.GeneratorUtils.Gender.*;
import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

import com.dinoproblems.server.utils.*;


//First stable ver got by Simar 22.04.2019
//не делаем import так как в одном package

public class SessionResult {

    private int problemSolved = 0;//кол-во решённых задач
    private int totalProblems = 0;//кол-во выданных задач
    private int hints = 0;//кол-во задач решённых с подсказкой
    private int total = 0;//суммарное кол-во баллов
    int token1 = randomInt(0, 5);//выбор фразы начала
    int token2 = randomInt(0, 5);//выбор фразы окончания

    public SessionResult() {
    }

    public int updateScore(Problem problem) {
        totalProblems++;
        int points = 0;
        if (problem.getState() == Problem.State.SOLVED) {
            if (problem.getDifficulty() == Problem.Difficulty.EASY) points = 2;
            else if (problem.getDifficulty() == Problem.Difficulty.MEDIUM) points = 4;
            else if (problem.getDifficulty() == Problem.Difficulty.HARD) points = 6;
            else points = 10;
            problemSolved++;
//          if (problemSolved % 3 == 0) total++;
        }
        if (problem.getState() == Problem.State.SOLVED_WITH_HINT) {
            if (problem.getDifficulty() == Problem.Difficulty.EASY) points += 1;
            else if (problem.getDifficulty() == Problem.Difficulty.MEDIUM) points += 2;
            else if (problem.getDifficulty() == Problem.Difficulty.HARD) points += 3;
            else points += 5;
            hints++;
            problemSolved++;
        }
        if (problem.getState() == Problem.State.ANSWER_GIVEN) {
            if (total - 5 > 0) points = -5;
            else points = 0;
        }

        total += points;
        return points;
    }

    public ProblemTextBuilder getResult() {
        ProblemTextBuilder text = new ProblemTextBuilder();//Ctrl+o//окончания+остальные поля
        final String[] goodBeginning = new String[]{"Молодец! Вам удалось решить ",
                "Невероятно! Вы дали верные ответы на ",
                "Умница! Вы преуспели в решении ",
                "Это было впечатляюще! Вы решили ",
                "Вы превзошли все ожидания и решили "};
        final String[] normalBeginning = new String[]{"Вы решили ",
                "Вы смогли решить ",
                "Вы дали верные ответы на ",
                "Вам удалось решить ",
                "Вы нашли верное решение на "};
        final String[] badBeginning = new String[]{"К сожалению Вы не решили ни одной задачи. ",
                "Мне грустно это сообщать. Вы не смогли решить ни одной задачи. ",
                "Очень жаль. Вы не дали ни одного верного ответа. ",
                "Печально. Вам не удалось решишь ни одной задачи. ",
                "Я расстроена. Вы не нашли ни одного верного решения. "};
        final String[] goodEnding = new String[]{"Так держать! Жду Вас снова!",
                "Уже готовлю для Вас интересные задачи! Заходите ещё!",
                "У меня ещё есть чему Вас научить. Будет интересно, обязательно приходите!",
                "Ваши способности нужно развивать. С нетерпением жду Вас снова!",
                "Новые задачи уже на подходе! Буду рада снова видеть Вас среди своих учеников."};
        final String[] normalEnding = new String[]{"У Вас есть потенциал. Жду Вас снова!",
                "Вам есть к чему стремиться. Буду рада снова решать с Вами задачи!",
                "Вы можете лучше. Обязательно приходите ещё!",
                "Вы способны на большее. С нетерпением жду Вас снова!",
                "Я уверена в Вашем будущем успехе. Заходите ещё!"};
        switch (totalProblems) {

            case 1:
                if (problemSolved != 0)
                    if (hints != 0) {
                        text.append(normalBeginning[token1]);
                        text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.ACCUSATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенной задачи, ");
                        text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, FEMININE, GeneratorUtils.Case.ACCUSATIVE));
                        text.append(" из которых решили с нашей помощью. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, MASCULINE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" ");
                        if (total%10 == 1) text.append(Dictionary.SCORE.getNominative());
                        else if (total%10>1 && total%10 < 5) text.append(Dictionary.SCORE.getGenitive());
                        else text.append(Dictionary.SCORE.getCountingForm());
                        text.append(". ");
                        text.append(normalEnding[token2]);
                        //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи, " + hints + " из которых с нашей помощью. Ждём Вас снова!";}
                        return text;
                    }//как грамотно вернуть полученный итог?
                    else {
                        text.append(normalBeginning[token1]);
                        text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.ACCUSATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенной задачи");
                        //  text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                        text.append(". Вы обошлись без нашей помощи. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, MASCULINE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" ");
                        if (total%10 == 1) text.append(Dictionary.SCORE.getNominative());
                        else if (total%10>1 && total%10<5) text.append(Dictionary.SCORE.getGenitive());
                        else text.append(Dictionary.SCORE.getCountingForm());
                        text.append(". ");
                        text.append(normalEnding[token2]);
                        return text;
                    }
                    //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенной задачи и обошлись без нашей помощи. Ждём Вас снова!";
                else {
                    text.append(badBeginning[token1]);
                    text.append(normalEnding[token2]);
                    return text;
                }
                //  return "Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!";
            default:
                if (problemSolved != 0)
                    if (hints != 0) {
                        if (total >= 20) text.append(goodBeginning[token1]);
                        else text.append(normalBeginning[token1]);
                        if (problemSolved == 1)
                            text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.ACCUSATIVE));
                        else
                            text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенных задач, ");
                        text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, FEMININE, GeneratorUtils.Case.ACCUSATIVE));
                        text.append(" из которых решили с нашей помощью. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, MASCULINE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" ");
                        if (total%10 == 1) text.append(Dictionary.SCORE.getNominative());
                        else if (total%10>1 && total%10 < 5) text.append(Dictionary.SCORE.getGenitive());
                        else text.append(Dictionary.SCORE.getCountingForm());
                        text.append(". ");
                        if (total >= 20) text.append(goodEnding[token2]);
                        else text.append(normalEnding[token2]);
                        return text;
                    }
                    //return "Вы решили " + problemSolved + " из " + totalProblems + " предложенных задач, " + hints + " из которых с нашей помощью. Ждём Вас снова!";
                    else {
                        if (total >= 20) text.append(goodBeginning[token1]);
                        else text.append(normalBeginning[token1]);
                        if (problemSolved == 1)
                            text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.ACCUSATIVE));
                        else
                            text.append(String.valueOf(problemSolved), NumberWord.getStringForNumber(problemSolved, FEMININE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" из ");
                        text.append(String.valueOf(totalProblems), NumberWord.getStringForNumber(totalProblems, FEMININE, GeneratorUtils.Case.GENITIVE));
                        text.append(" предложенных задач");
                        // text.append(String.valueOf(hints), NumberWord.getStringForNumber(hints, NEUTER, GeneratorUtils.Case.NOMINATIVE));
                        text.append(". Вы обошлись без нашей помощи. Всего набрали ");
                        text.append(String.valueOf(total), NumberWord.getStringForNumber(total, MASCULINE, GeneratorUtils.Case.NOMINATIVE));
                        text.append(" ");
                        if (total%10 == 1) text.append(Dictionary.SCORE.getNominative());
                        else if (total%10>1 && total%10 < 5) text.append(Dictionary.SCORE.getGenitive());
                        else text.append(Dictionary.SCORE.getCountingForm());
                        text.append(". ");
                        if (total >= 20) text.append(goodEnding[token2]);
                        else text.append(normalEnding[token2]);
                        return text;
                    }//return "Вы решили " + problemSolved + " из " + totalProblems + " предложенных задач и обошлись без нашей помощи. Ждём Вас снова!";
                else
                // return "Вы не решили ни одной задачи. Старайтесь лучше и приходите ещё!";
                {
                    text.append(badBeginning[token1]);
                    text.append(normalEnding[token2]);
                    return text;
                }

        }
    }
}
