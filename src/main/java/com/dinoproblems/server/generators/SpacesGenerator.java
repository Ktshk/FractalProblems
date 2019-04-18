package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.AbstractNoun;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.*;

import static com.dinoproblems.server.utils.Dictionary.*;
import static com.dinoproblems.server.utils.GeneratorUtils.Case.ACCUSATIVE;
import static com.dinoproblems.server.utils.GeneratorUtils.Gender.MASCULINE;
import static com.dinoproblems.server.utils.GeneratorUtils.*;

/**
 * Created by Katushka
 * on 24.02.2019.
 */
public class SpacesGenerator implements ProblemGenerator {
    private static final String LOG = "LOG";
    private static final String DRAWING = "DRAWING";
    private static final String FENCE = "FENCE";
    private static final String[] THEMES = {LOG, DRAWING, FENCE};
    private static final List<ProblemScenario> SCENARIOS;

    static {
        SCENARIOS = new ArrayList<>();
        for (String theme : THEMES) {
            SCENARIOS.add(new SpacesScenario(theme, true));
            SCENARIOS.add(new SpacesScenario(theme, false));
        }
    }

    @Override
    @Nonnull
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        int spaces = difficulty == Problem.Difficulty.EASY ? GeneratorUtils.randomInt(7, 15) : GeneratorUtils.randomInt(10, 25);
        int blocks = difficulty == Problem.Difficulty.EASY ? 1 :
                difficulty == Problem.Difficulty.MEDIUM ? GeneratorUtils.randomInt(1, 5)
                : GeneratorUtils.randomInt(3, 7);
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

                if (difficulty == Problem.Difficulty.EASY) {
                    String piecesWithText = getNumWithString(pieces, "кусок", "куска", "кусков", MASCULINE);
                    if (scenario.isDirectTask()) {
                        text = "Бобер пилил бревна. На одном бревне он сделал "
                                + spaceWithText
                                + ". Сколько кусков бревна у него получилось?";
                        possibleTextAnswers = Sets.newHashSet(piecesWithText);
                        answer = pieces;
                    } else {
                        text = "Бобер пилил бревна. Из одного бревна у него получилось "
                                + piecesWithText
                                + ". Сколько распилов он сделал?";
                        possibleTextAnswers = Sets.newHashSet(spaceWithText);
                        answer = spaces;
                    }
                } else {
                    pieces = spaces + blocks;
                    String piecesWithText = getNumWithString(pieces, "кусок", "куска", "кусков", MASCULINE);
                    if (scenario.isDirectTask()) {
                        text = "Бобер пилил бревна. На " + getBlocksStringNa(blocks) + " он сделал "
                                + spaceWithText
                                + ". Сколько кусков бревна у него получилось?";
                        possibleTextAnswers = Sets.newHashSet(piecesWithText);
                        answer = pieces;
                    } else {
                        text = "Бобер пилил бревна. Из " + getBlocksStringIz(blocks) + " у него получилось "
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
                AbstractNoun[] things = {STICK, STAR, BUTTON, COIN};
                int thing = GeneratorUtils.randomInt(0, things.length);
                if (difficulty == Problem.Difficulty.EASY || (difficulty == Problem.Difficulty.MEDIUM && !scenario.isDirectTask())) {
                    String[] chosenHeroes = chooseRandomString(heroes, 2);

                    if (scenario.isDirectTask()) {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + getNumWithString(pieces, things[thing], ACCUSATIVE)
                                + ". " + chosenHeroes[1] + " между каждыми двумя "
                                + (thing < 2 ? " нарисовал " : " выложил ") + " ещё по одной. "
                                + "Сколько " + things[thing].getCountingForm() + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[1] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing], ACCUSATIVE));
                        answer = spaces;
                    } else {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + "несколько " + things[thing].getCountingForm()
                                + ". " + chosenHeroes[1] + " между каждыми двумя "
                                + (thing < 2 ? " нарисовал " : " выложил ") + " ещё по одной. "
                                + "Всего получилось " + getNumWithString(total, things[thing])
                                + ".  Сколько " + things[thing].getCountingForm() + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[0] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing], ACCUSATIVE));
                        answer = pieces;
                    }
                    hint = "Подумайте, насколько меньше " + things[thing].getCountingForm() + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[1] + ". ";
                } else {
                    int spaces2 = total - 1;
                    int total2 = total + spaces2;

                    String[] chosenHeroes = chooseRandomString(heroes, 3);

                    if (!scenario.isDirectTask()) {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + "несколько " + things[thing].getCountingForm()
                                + ". ";
                    } else {
                        text = chosenHeroes[0] + (thing < 2 ? " нарисовал на доске " : " выложил в ряд ")
                                + getNumWithString(pieces, things[thing], ACCUSATIVE)
                                + ". ";
                    }

                    text += chosenHeroes[1] + " между каждыми двумя "
                            + (thing < 2 ? " нарисовал " : " выложил ") + " ещё по одной. "
                            + "Потом пришёл " + chosenHeroes[2] + " и сделал то же самое. ";

                    if (!scenario.isDirectTask()) {
                        text += "Всего получилось " + getNumWithString(total2, things[thing])
                                + ". Сколько " + things[thing].getCountingForm() + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[0] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(pieces, things[thing], ACCUSATIVE));
                        answer = pieces;
                    } else {
                        text += "Сколько " + things[thing].getCountingForm() + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[2] + "?";
                        possibleTextAnswers = Sets.newHashSet(getNumWithString(spaces, things[thing], ACCUSATIVE));
                        answer = spaces2;
                    }
                    hint = "Подумайте, насколько меньше " + things[thing].getCountingForm() + (thing < 2 ? " нарисовал " : " выложил ") + chosenHeroes[2] + ", чем до этого было.";
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
        switch (difficulty) {
            case EASY:
                final ArrayList<ProblemScenario> easyScenarios = new ArrayList<>(SCENARIOS);
                easyScenarios.remove(new SpacesScenario(DRAWING, false));
                return findAvailableScenario(difficulty, alreadySolvedProblems, easyScenarios, new HashSet<>());
            case MEDIUM: {
                final ArrayList<ProblemScenario> mediumScenarios = new ArrayList<>(SCENARIOS);
                mediumScenarios.remove(new SpacesScenario(LOG, false));
                return findAvailableScenario(difficulty, alreadySolvedProblems, mediumScenarios, Sets.newHashSet(SCENARIOS));
            }
            case DIFFICULT:
                final Set<ProblemScenario> mediumScenarios = new HashSet<>(SCENARIOS);
                mediumScenarios.remove(new SpacesScenario(LOG, false));
                return findAvailableScenario(difficulty, alreadySolvedProblems,
                        Lists.newArrayList(new SpacesScenario(DRAWING, false), new SpacesScenario(LOG, false)), mediumScenarios);
            case EXPERT:
                return null;

        }

        throw new IllegalArgumentException();
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