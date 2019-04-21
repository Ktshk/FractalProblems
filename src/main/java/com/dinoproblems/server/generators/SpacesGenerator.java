package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.AbstractNoun;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.OrdinalNumber;
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
    private static final String CAKES = "CAKES";
    private static final String ELEVATOR = "ELEVATOR";
    private static final String PILLS = "PILLS";

    private static final String[] THEMES = {LOG, DRAWING, FENCE};

    private static final List<ProblemScenario> SCENARIOS;
    private static  final ArrayList<ProblemScenario> EASY_SCENARIOS;
    private static final ArrayList<ProblemScenario> MEDIUM_SCENARIOS;

    private static final String[] HEROES = {"Миша", "Дима", "Коля", "Вася", "Рома", "Сережа"};

    static {
        SCENARIOS = new ArrayList<>();
        for (String theme : THEMES) {
            SCENARIOS.add(new SpacesScenario(theme, true));
            SCENARIOS.add(new SpacesScenario(theme, false));
        }

        EASY_SCENARIOS = new ArrayList<>(SCENARIOS);
        EASY_SCENARIOS.remove(new SpacesScenario(DRAWING, false));
        EASY_SCENARIOS.add(new SpacesScenario(CAKES, true));
        EASY_SCENARIOS.add(new SpacesScenario(ELEVATOR, true));
        EASY_SCENARIOS.add(new SpacesScenario(PILLS, true));

        MEDIUM_SCENARIOS = new ArrayList<>(SCENARIOS);
        MEDIUM_SCENARIOS.remove(new SpacesScenario(LOG, false));
        MEDIUM_SCENARIOS.add(new SpacesScenario(ELEVATOR, true));
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
            case CAKES:
                String hero = HEROES[randomInt(0, HEROES.length)];
                text = hero + " выложил в круг на столе " + pieces + " кремовых пирожных. " +
                        "Пришла мама и положила между каждыми двумя соседними пирожными по одной корзиночке с фруктами. " +
                        "Сколько всего сладостей стало на столе?";
                answer = pieces * 2;
                hint = "Нарисуйте картинку и посчитатйе, сколько корзиночек положила мама.";
                possibleTextAnswers = Sets.newHashSet(getNumWithString(answer, SWEETS));
                break;
            case PILLS:
                int pills = randomInt(4, 10);
                text = "Совунья прописала Крошу " + pills + " таблеток, которые нужно принимать по одной каждый час. Сколько времени займёт лечение?";
                answer = pills - 1;
                hint = "Подумайте, сколько пройдет времени, когда Крош примет вторую таблетку? А когда третью?";
                possibleTextAnswers = Sets.newHashSet(answer + " часов");
                break;
            case ELEVATOR:
                int firstFloor = difficulty == Problem.Difficulty.EASY ? 1 : randomInt(2, 10);
                int lastFloor = difficulty == Problem.Difficulty.EASY ? randomInt(5, 8) : randomInt(firstFloor + 3, firstFloor + 7);
                text = "Лифт поднимается с 1 на 3 этаж за 6 секунд. За какое время лифт поднимется с " +
                        OrdinalNumber.number(firstFloor).getGenitiveMasculine() + " на " + OrdinalNumber.number(lastFloor).getAccusativeForm(MASCULINE) + " этаж?";
                answer = 3 * (lastFloor - firstFloor);
                hint = "Подумайте хорошенько, за сколько лифт поднимается с одного этажа на другой, например, с первого на второй.";
                possibleTextAnswers = Sets.newHashSet("За " + getNumWithString(answer, SECOND), getNumWithString(answer, SECOND));
                break;
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
                AbstractNoun[] things = {STICK, STAR, BUTTON, COIN};
                int thing = GeneratorUtils.randomInt(0, things.length);
                if (difficulty == Problem.Difficulty.EASY || (difficulty == Problem.Difficulty.MEDIUM && !scenario.isDirectTask())) {
                    String[] chosenHeroes = chooseRandomString(HEROES, 2);

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

                    String[] chosenHeroes = chooseRandomString(HEROES, 3);

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
            case 5:
                return "пяти брёвнах";
            case 6:
                return "шести брёвнах";
        }
        throw new IllegalArgumentException(Integer.toString(count));
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
            case 5:
                return "пяти бревен";
            case 6:
                return "шести бревен";
        }
        throw new IllegalArgumentException(Integer.toString(count));
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {


        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, EASY_SCENARIOS, new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, MEDIUM_SCENARIOS, Sets.newHashSet(EASY_SCENARIOS));
            case DIFFICULT:
                return findAvailableScenario(difficulty, alreadySolvedProblems,
                        Lists.newArrayList(new SpacesScenario(DRAWING, false), new SpacesScenario(LOG, false)),
                        Sets.newHashSet(MEDIUM_SCENARIOS));
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