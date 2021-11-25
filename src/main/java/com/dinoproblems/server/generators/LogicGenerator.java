package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.NumberWord;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.dinoproblems.server.utils.Dictionary.KNIGHT;
import static com.dinoproblems.server.utils.Dictionary.LIAR;
import static com.dinoproblems.server.utils.Dictionary.PEOPLE;
import static com.dinoproblems.server.utils.GeneratorUtils.*;

/**
 * Created by Katushka on 29.05.2019.
 */
public class LogicGenerator implements ProblemGenerator {
    private static final ProblemScenario NUMBER_FOUR_CHILDREN_SCENARIO = new ProblemScenarioImpl(ProblemCollection.LOGIC + "_" + "NUMBER_FOUR_CHILDREN", true);
    private static final ProblemScenario MUSHROOMS = new ProblemScenarioImpl(ProblemCollection.LOGIC + "_" + "MUSHROOMS");
    private static final ProblemScenario YOU_ALL_LIARS = new ProblemScenarioImpl(ProblemCollection.LOGIC + "_" + "YOU_ALL_LIARS", true);
    private static final ProblemScenario TOTAL_EVERYTHING_IS_TRUE = new ProblemScenarioImpl(ProblemCollection.LOGIC + "_" + "TOTAL_EVERYTHING_IS_TRUE", true);

    private static final Problem NUMBER_FOUR_CHILDREN;

    static {
        TextWithTTSBuilder text = new TextWithTTSBuilder();
        text.append("На доске было написано число, и четверых ребят спросили, что это за число. ")
                .append("Миша сказал: \"Это число 15\". Рома сказал: \"Это простое число\". Катя сказала: \"Это чётное число\".")
                .append(" Наташа сказала: \"Это число делится на 6\". Один мальчик и одна девочка ответили верно, ")
                .append("а двое остальных ошиблись. Какое число было написано на доске?");
        String hint = "Подумайте, мог ли Миша сказать правду. ";
        NUMBER_FOUR_CHILDREN = new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS())
                .answer(2).theme(ProblemCollection.LOGIC)
                .possibleTextAnswers(Sets.newHashSet("2 - единственное чётное простое число"))
                .hint(hint).scenario(NUMBER_FOUR_CHILDREN_SCENARIO).difficulty(Problem.Difficulty.EXPERT).create();
    }

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        final ProblemScenario scenario = problemAvailability.getScenario();
        if (scenario.equals(NUMBER_FOUR_CHILDREN_SCENARIO)) {
            return NUMBER_FOUR_CHILDREN;
        } else if (scenario.equals(YOU_ALL_LIARS)) {
            TextWithTTSBuilder text = new TextWithTTSBuilder();
            int num = randomInt(5, 11);
            text.append("В комнате собрались ").append(getNumWithString(num, PEOPLE)).append(" с острова Рыцарей и Лжецов. ")
                    .append("(Рыцари всегда говорят правду, а лжецы всегда врут.) " +
                    "Каждый сказал остальным: «Вы все лжецы!». Сколько лжецов среди них?");
            String hint1 = "Допустим, что все собравшиеся лжецы. Могло ли такое быть? ";
            int answer = num - 1;
            return new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS())
                    .answer(answer).theme(ProblemCollection.LOGIC)
                    .possibleTextAnswers(Sets.newHashSet(getNumWithString(answer, LIAR)))
                    .hint(hint1).scenario(YOU_ALL_LIARS).difficulty(Problem.Difficulty.MEDIUM).create();
        } else if (scenario.equals(MUSHROOMS)) {
            final TextWithTTSBuilder text = new TextWithTTSBuilder();
            final String[] heroes = new String[]{"Миша", "Костя", "Влад", "Ричард", "Саша", "Андрей", "Леша"};
            final int heroesCount = randomInt(3, 5);
            final int startCount = randomInt(25, 50);
            final int trueSentences = randomInt(1, heroesCount);
            final boolean greaterSentences = randomInt(0, 2) == 0;
            final String[] chosenHeroes = chooseRandomString(heroes, heroesCount);
            final List<String> sortedHeroes = Lists.newArrayList(chosenHeroes);
            Collections.sort(sortedHeroes);

            for (int i = 0; i < chosenHeroes.length; i++) {
                text.append(chosenHeroes[i]);
                if (i < chosenHeroes.length - 2) {
                    text.append(", ");
                } else if (i < chosenHeroes.length - 1) {
                    text.append(" и ");
                }
            }
            text.append(" собирали грибы. Когда у них спросили: «Сколько грибов вы собрали?», ");
            for (String chosenHero : chosenHeroes) {
                text.append(chosenHero).append(" сказал: «");
                text.append(greaterSentences ? "Больше " : "Меньше ");
                final int index = sortedHeroes.indexOf(chosenHero);
                final int num = index + startCount;
                text.append(Integer.toString(num), NumberWord.getStringForNumber(num, Gender.MASCULINE, Case.GENITIVE));
                text.append("». ");
            }

            text.append("Сколько грибов они собрали, если ").append(trueSentences > 1 ? "правы были" : "прав был")
                    .append(" в точности ").append(getNumChildren(trueSentences)).append("?");

            String hint1;
            if (greaterSentences) {
                hint1 = "Если прав " + sortedHeroes.get(heroesCount - 1) + ", то прав и " + sortedHeroes.get(heroesCount - 2);
            } else {
                hint1 = "Если прав " + sortedHeroes.get(0) + ", то прав и " + sortedHeroes.get(1);
            }
            String hint2;
            if (greaterSentences) {
                hint2 = "Если " + sortedHeroes.get(1) + " сказал правду, то и " + sortedHeroes.get(0) + " сказал правду";
            } else {
                hint2 = "Если " + sortedHeroes.get(heroesCount - 2) + " сказал правду, то и " + sortedHeroes.get(heroesCount - 1) + " сказал правду";
            }
            int answer = greaterSentences ? (startCount + trueSentences) : (startCount + heroesCount - 1 - trueSentences);
            String textAnswer = "" + answer + (trueSentences == 1 ? " (прав был " : " (правы были ");
            for (int i = 0; i < trueSentences; i++) {
                textAnswer += greaterSentences ? sortedHeroes.get(i) : sortedHeroes.get(heroesCount - 1 - i);
                if (i < trueSentences - 2) {
                    textAnswer += ", ";
                } else if (i < trueSentences - 1) {
                    textAnswer += " и ";
                }
            }
            textAnswer += ")";
            return new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS())
                    .answer(answer).theme(ProblemCollection.LOGIC)
                    .possibleTextAnswers(Sets.newHashSet(textAnswer))
                    .hints(Lists.newArrayList(hint1, hint2)).scenario(MUSHROOMS).difficulty(Problem.Difficulty.HARD).create();
        } else if (scenario.equals(TOTAL_EVERYTHING_IS_TRUE)) {
            TextWithTTSBuilder text = new TextWithTTSBuilder();
            int liars = randomInt(5, 16);
            int knights = randomInt(5, 16);

            text.append(getNumWithString(knights, KNIGHT)).append(" и ").append(getNumWithString(liars, LIAR))
                    .append(" написали по статье каждый. ")
                    .append("(Рыцари всегда говорят и пишут правду, а лжецы всегда врут.)")
                    .append(" В конце статьи некоторые из них написали: Все, что здесь написано - правда. ")
                    .append("Остальные написали: Все, что здесь написано - ложь. ")
                    .append("Сколько человек написали первую фразу?");
            String hint1 = "Подумайте, какую фразу мог написать рыцарь, а какую лжец. ";
            int answer = liars + knights;
            return new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS())
                    .answer(answer).theme(ProblemCollection.LOGIC)
                    .possibleTextAnswers(Sets.newHashSet(getNumWithString(answer, PEOPLE)))
                    .hint(hint1).scenario(TOTAL_EVERYTHING_IS_TRUE).difficulty(Problem.Difficulty.EASY).create();
        }
        throw new IllegalArgumentException();
    }

    private String getNumChildren(int trueSentences) {
        switch (trueSentences) {
            case 1:
                return "один из ребят";
            case 2:
                return "двое";
            case 3:
                return "трое";
            case 4:
                return "четверо";
        }
        throw new IllegalArgumentException();
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty, UserInfo userInfo) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(TOTAL_EVERYTHING_IS_TRUE), new HashSet<>(), userInfo);
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(YOU_ALL_LIARS), new HashSet<>(), userInfo);
            case HARD:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(MUSHROOMS), new HashSet<>(), userInfo);
            case EXPERT:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(NUMBER_FOUR_CHILDREN_SCENARIO), new HashSet<>(), userInfo);

        }

        throw new IllegalArgumentException();
    }
}
