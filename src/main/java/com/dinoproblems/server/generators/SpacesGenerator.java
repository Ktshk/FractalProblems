package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;

import static com.dinoproblems.server.utils.GeneratorUtils.*;
import static com.dinoproblems.server.utils.GeneratorUtils.Case.ACCUSATIVE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.FEMININE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;

/**
 * Created by Katushka on 24.02.2019.
 */
public class SpacesGenerator implements ProblemGenerator {
    private static final String LOG = "LOG";
    private static final String DRAWING = "DRAWING";
    private static final String FENCE = "FENCE";
    private static final String[] THEMES = {LOG, DRAWING, FENCE};
    private static final List<ProblemScenario> SCENARIOS;

    static {
        SCENARIOS  = new ArrayList<>();
        for (String theme : THEMES) {
            SCENARIOS.add(new SpacesScenario(theme, true));
            SCENARIOS.add(new SpacesScenario(theme, false));
        }
    }

    @Override
    @Nonnull
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        int spaces = difficulty == Problem.Difficulty.EASY ? GeneratorUtils.randomInt(7, 15) : GeneratorUtils.randomInt(10, 25);
        int blocks = difficulty == Problem.Difficulty.EASY ? 1 : GeneratorUtils.randomInt(1, 5);
        int pieces = spaces + 1;
        int total = spaces + pieces;

        SpacesScenario scenario = (SpacesScenario) problemAvailability.getScenario();

        String text;
        int answer;
        Set<String> possibleTextAnswers;

        String hint;
        switch (scenario.getTheme()) {
            case LOG:
                final String spaceWithText = getNumWithString(spaces, "распил", "распила", "распилов", MASCULINE);
                final String piecesWithText = getNumWithString(pieces, "кусок", "куска", "кусков", MASCULINE);

                if (difficulty == Problem.Difficulty.EASY) {
                    if (scenario.isDirectTask()) {
                        text = "Бобер пилил бревна. На одном бревне он сделал "
                                + spaceWithText
                                + ". Сколько кусков бревна у него получилось?";
                        possibleTextAnswers = Sets.newHashSet(piecesWithText);
                        answer = pieces;
                    } else {
                        text = "Бобер пилил бревна. Из одного бревна у него получилось "
                                + piecesWithText
                                + ". Сколько разрубов он сделал?";
                        possibleTextAnswers = Sets.newHashSet(spaceWithText);
                        answer = spaces;
                    }
                } else {
                    pieces = spaces + blocks;
                    if (scenario.isDirectTask()) {
                        text = "Бобер пилил бревна. На " + getBlocksStringNa(blocks) + " он сделал "
                                + spaceWithText
                                + ". Сколько кусков бревна у него получилось?";
                        possibleTextAnswers = Sets.newHashSet(piecesWithText);
                        answer = pieces;
                    } else {
                        text = "Бобер пилил бревна. Из " + getBlocksStringIz(blocks) + " бревна у него получилось "
                                + piecesWithText
                                + ". Сколько распилов он сделал?";
                        possibleTextAnswers = Sets.newHashSet(spaceWithText);
                        answer = spaces;
                    }
                }
                hint = "Подумайте, сколько стало кусков после первого распила, и насколько меняется число кусков после каждого следующего распила";
                break;
            case DRAWING:
                String[] heroes = new String[]{"Миша", "Дима", "Коля", "Вася", "Рома", "Сережа"};
                String[][] things = {{"палочку", "палочки", "палочек"},
                        {"звёздочку", "звёздочки", "звёздочек"},
                        {"пуговицу", "пуговицы", "пуговиц"},
                        {"монету", "монеты", "монет"}};
                int thing = GeneratorUtils.randomInt(0, things.length);
                if (difficulty == Problem.Difficulty.EASY) {
                    String[] chosenHeroes = chooseRandomString(heroes, 2);

                    if (scenario.isDirectTask()) {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + getNumWithString(pieces, things[thing][0], things[thing][1], things[thing][2], FEMININE, ACCUSATIVE)
                                + ". " + chosenHeroes[1] + " между каждыми двумя "
                                + (thing < 2 ? " нарисовал " : " выложил ") + " ещё по одной. "
                                + "Сколько " + things[thing][2] + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[1] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing][0], things[thing][1], things[thing][2], FEMININE, ACCUSATIVE));
                        answer = spaces;
                    } else {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + "несколько " + things[thing][2]
                                + ". " + chosenHeroes[1] + " между каждыми двумя "
                                + (thing < 2 ? " нарисовал " : " выложил ") + " ещё по одной. "
                                + "Всего получилось " + getNumWithString(total, things[thing], FEMININE)
                                + ".  Сколько " + things[thing][2] + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[0] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing][0], things[thing][1], things[thing][2], FEMININE, ACCUSATIVE));
                        answer = pieces;
                    }
                    hint = "Подумайте, насколько меньше " + things[thing][2] + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[1] + ". ";
                } else {
                    int spaces2 = total - 1;
                    int total2 = total + spaces2;

                    String[] chosenHeroes = chooseRandomString(heroes, 3);

                    if (!scenario.isDirectTask()) {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + "несколько " + things[thing][2]
                                + ". ";
                    } else {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + getNumWithString(pieces, things[thing][0], things[thing][1], things[thing][2], FEMININE, ACCUSATIVE)
                                + ". ";
                    }

                    text += chosenHeroes[1] + " между каждыми двумя "
                            + (thing < 2 ? " нарисовал " : " выложил ") + " ещё по одной. "
                            + "Потом пришёл " + chosenHeroes[2] + " и сделал то же самое. ";

                    if (!scenario.isDirectTask()) {
                        text += "Всего получилось " + getNumWithString(total2, things[thing], FEMININE)
                                + ". Сколько " + things[thing][2] + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[0] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing][0], things[thing][1], things[thing][2], FEMININE, ACCUSATIVE));
                        answer = pieces;
                    } else {
                        text += "Сколько " + things[thing][2] + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[2] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing][0], things[thing][1], things[thing][2], FEMININE, ACCUSATIVE));
                        answer = spaces2;
                    }
                    hint = "Подумайте, насколько меньше " + things[thing][2] + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[2] + ", чем до этого было.";
                }
                break;
            case FENCE:
                final String postsString = GeneratorUtils.getNumWithString(pieces, "столб", "столба", "столбов", MASCULINE);

                if (difficulty == Problem.Difficulty.EASY) {
                    if (!scenario.isDirectTask()) {
                        text = "Забор крепится на столбы через каждый метр. " +
                                "Сколько нужно врыть столбов, чтобы установить забор длиной " +
                                GeneratorUtils.getMetersString(spaces) + "?";
                        answer = pieces;
                        possibleTextAnswers = Sets.newHashSet(postsString);
                    } else {
                        text = "Забор крепится на столбы через каждый метр. " +
                                "Какой длины забор, если в нем " + postsString + "?";
                        answer = spaces;
                        possibleTextAnswers = Sets.newHashSet(GeneratorUtils.getMetersString(spaces));
                    }
                    hint = "Нарисуйте картинку.";
                } else {
                    int meters = randomInt(2, 5);
                    if (!scenario.isDirectTask()) {
                        text = "Забор крепится на столбы через каждые " + getMetersString(meters) + ". " +
                                "Сколько нужно врыть столбов, чтобы установить забор длиной " +
                                getMetersString(spaces * meters) + "?";
                        answer = pieces;
                        possibleTextAnswers = Sets.newHashSet(postsString);
                    } else {
                        text = "Забор крепится на столбы через каждые " + getMetersString(meters) + ". " +
                                "Какой длины забор, если в нем " + postsString + "?";
                        answer = spaces * meters;
                        possibleTextAnswers = Sets.newHashSet(getMetersString(spaces * meters));
                    }
                    hint = "Нарисуйте картинку. Сколько промежутков между столбами? Помните, каждый промежуток - это " + getMetersString(meters) + ".";
                }
                break;
            default:
                throw new IllegalArgumentException();
        }

        return new ProblemWithPossibleTextAnswers(text, answer, ProblemCollection.SPACES, possibleTextAnswers, hint, scenario, difficulty);
    }

    private String getBlocksStringNa(int count) {
        switch (count) {
            case 1:
                return "одном бревне";
            case 2:
                return "двух брёвнах";
            case 3:
                return "трёх брёвнах";
            case 4:
                return "четырёх брёвнах";
        }
        throw new IllegalArgumentException();
    }

    private String getBlocksStringIz(int count) {
        switch (count) {
            case 1:
                return "одного бревна";
            case 2:
                return "двух брёвен";
            case 3:
                return "трёх бревен";
            case 4:
                return "четырёх бревен";
        }
        throw new IllegalArgumentException();
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        if (difficulty == Problem.Difficulty.HARD) {
            return null;
        }
        return findAvailableScenario(difficulty, alreadySolvedProblems, SCENARIOS,
                difficulty == Problem.Difficulty.EASY ? new HashSet<>() : SCENARIOS);
    }

    private static class SpacesScenario extends ProblemScenarioImpl {

        private final String theme;
        private final boolean directTask;

        SpacesScenario(String theme, boolean directTask) {
            super(ProblemCollection.SPACES + "_" + theme + (directTask ? "" : "_INDIRECT"));
            this.theme = theme;
            this.directTask = directTask;
        }

        boolean isDirectTask() {
            return directTask;
        }

        String getTheme() {
            return theme;
        }
    }
}