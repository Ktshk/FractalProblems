package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dinoproblems.server.Problem.Difficulty.HARD;
import static com.dinoproblems.server.Problem.Difficulty.MEDIUM;
import static com.dinoproblems.server.ProblemCollection.EILER_CIRCLES;
import static com.dinoproblems.server.utils.Dictionary.*;
import static com.dinoproblems.server.utils.GeneratorUtils.*;

/**
 * Created by Katushka
 * on 07.03.2019.
 */
public class EilerCirclesGenerator implements ProblemGenerator {

    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String TOTAL = "total";
    private static final String X_Y = "x + y";
    private static final String Y_Z = "y + z";

    private static final List<ProblemScenario> SCENARIOS = new ArrayList<>();
    private static final List<ProblemScenario> EASY_SCENARIOS = new ArrayList<>();

    private static final String[] VARIABLES = {X, Y, Z, X_Y, Y_Z, TOTAL};
    private static final String[] EASY_VARIABLES = {Y, X_Y, Y_Z, TOTAL};

    private static final Verb LIKE = new Verb("увлекается", "увлекаются");
    private static final Verb BE_INTERESTED = new Verb("интересуется", "интересуются");
    private static final Verb BUSY_ONESELF = new Verb("занимается", "занимаются");
    private static final Verb GET = new Verb("получил", "получили");
    private static final Verb EAT = new Verb("съел", "съели");
    private static final Verb TREAT = new Verb("угостился", "угостились");

    private static final String CLASS = "в классе";
    private static final String CAMP = "в лагере";
    private static final String PARTY = "день рождения";
    private static final String[] THEMES = new String[]{CLASS, CAMP, PARTY};

    private static final Map<String, AbstractNoun[]> THEME_OBJECTS = new HashMap<>();
    private static final Map<AbstractNoun[], Verb[]> VERBS = new HashMap<>();

    private static final Map<Verb, Function<AbstractNoun, String>> GOVERN_FORM = new HashMap<>();

    private static final ProblemScenario CANDIES_STORE_SCENARIO = new ProblemScenarioImpl(ProblemCollection.EILER_CIRCLES + "_" + "CANDIES_STORE");
    private static final ProblemScenario CUBES_SCENARIO = new ProblemScenarioImpl(ProblemCollection.EILER_CIRCLES + "_" + "CUBES");
    private static final ProblemScenario EXPERT_CANDIES_SCENARIO = new ProblemScenarioImpl(ProblemCollection.EILER_CIRCLES + "_" + "EXPERT_CANDIES", true);

    static {
        final Set<Set<String>> incorrectCombinations = Sets.newHashSet(Sets.newHashSet(X, Y_Z, TOTAL),
                Sets.newHashSet(Z, X_Y, TOTAL),
                Sets.newHashSet(X, Y, Z),
                Sets.newHashSet(X, Y, X_Y),
                Sets.newHashSet(Y, Z, Y_Z));

        populateScenarios(incorrectCombinations, VARIABLES, SCENARIOS);
        populateScenarios(incorrectCombinations, EASY_VARIABLES, EASY_SCENARIOS);

        EASY_SCENARIOS.add(CUBES_SCENARIO);

        THEME_OBJECTS.put(CLASS, SUBJECTS);
        THEME_OBJECTS.put(CAMP, SUBJECTS);
        THEME_OBJECTS.put(PARTY, CANDIES);

        VERBS.put(SUBJECTS, new Verb[]{BE_INTERESTED, BUSY_ONESELF, LIKE});
        VERBS.put(CANDIES, new Verb[]{GET, EAT, TREAT});

        GOVERN_FORM.put(LIKE, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(BUSY_ONESELF, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(BE_INTERESTED, AbstractNoun::getInstrumentalForm);
        GOVERN_FORM.put(EAT, AbstractNoun::getAccusativeForm);
        GOVERN_FORM.put(GET, AbstractNoun::getAccusativeForm);
        GOVERN_FORM.put(TREAT, AbstractNoun::getInstrumentalForm);
    }

    private static void populateScenarios(Set<Set<String>> incorrectCombinations, String[] variables, List<ProblemScenario> scenarios) {
        for (int i = 0; i < variables.length - 2; i++) {
            for (int j = i + 1; j < variables.length - 1; j++) {
                for (int k = j + 1; k < variables.length; k++) {
                    final HashSet<String> vars = Sets.newHashSet(variables[i], variables[j], variables[k]);
                    if (!incorrectCombinations.contains(vars)) {
                        scenarios.add(new EilerCirclesScenario(variables[i], variables[j], variables[k]));
                    }
                }
            }
        }
    }


    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        if (problemAvailability.getScenario().equals(CANDIES_STORE_SCENARIO)) {
            int cakes = randomInt(40, 61);
            int candies = randomInt(30, 51);
            int intersection = randomInt(10, 20);
            int answer = cakes + candies - intersection;
            String text = "В кондитерском отделе супермаркета посетители обычно покупают либо один торт, " +
                    "либо одну коробку конфет, либо один торт и одну коробку конфет. В один из дней было продано " +
                    getNumWithString(cakes, CAKE) + " и " + getNumWithString(candies, CANDY_BOX)+ ". Сколько было покупателей, если " +
                    getNumWithString(intersection, PEOPLE) + " купили и торт, и коробку конфет?";
            String hint = "Нарисуйте схему из двух кругов. В один круг поместите людей, которые купили торты, а в другой тех, " +
                    "кто купил коробки конфет. Не забудьте, что " + getNumWithString(intersection, PEOPLE) + " купили и торт, и коробку конфет.";
            return new ProblemWithPossibleTextAnswers.Builder().text(text).answer(answer).theme(ProblemCollection.EILER_CIRCLES).possibleTextAnswers(Sets.newHashSet(getNumWithString(answer, BUYER), getNumWithString(answer, PEOPLE))).hint(hint).scenario(CANDIES_STORE_SCENARIO).difficulty(difficulty).create();
        } else if (problemAvailability.getScenario().equals(EXPERT_CANDIES_SCENARIO)) {
            String text = "В школе на новогодней елке дети получили сладкие подарки: троечники - леденец, хорошисты - шоколадку, а отличники - леденец и шоколаднку. " +
                    "Известно, что хорошистов в два раза больше, чем тех, кто получил леденец, а троечников на 12 меньше, чем тех, кто получил шоколадку. " +
                    "Сколько в классе отличников, если всего в классе 24 ученика.";
            String hint = "Нарисуйте схему из двух кругов. В один круг поместите тех, кто получил леденец, а во второй - тех, кто получил шоколадку. " +
                    "В пересечении кругов будут находиться отличники. " +
                    "Если хорошистов в два раза больше, чем тех, кто получил леденец, то можно общее число школьников разделить на три равные части: " +
                    "две части - это хорошисты, а одна часть - это те, кто получил леденец.";
            return new ProblemWithPossibleTextAnswers.Builder().text(text).answer(2).theme(ProblemCollection.EILER_CIRCLES).possibleTextAnswers(Sets.newHashSet("2 отличника")).hint(hint).scenario(EXPERT_CANDIES_SCENARIO).difficulty(difficulty).create();
        } else if (problemAvailability.getScenario().equals(CUBES_SCENARIO)) {
            int red = randomInt(4, 10);
            int blue = randomInt(4, 10);
            int intersection = randomInt(2, Math.min(red, blue) - 1);
            int total = red + blue - intersection;
            String text = "У Лизы есть " + getNumWithString(total, BRICK) + " с красными и синими буквами. " +
                    blue + " из них c синими буквами, а " + red + " – с красными. На скольких кубиках написаны буквы двух цветов?";
            String hint = "Нарисуйте " + getNumWithString(total, BRICK) + " и попробуйте отметить, на каких синие буквы, а на каких красные, " +
                    "но только так, чтобы на каждом кубике была хотя бы одна красная или синяя буква.";
            return new ProblemWithPossibleTextAnswers.Builder().text(text).answer(intersection).theme(ProblemCollection.EILER_CIRCLES).possibleTextAnswers(Sets.newHashSet("на " + NumberWord.getStringForNumber(intersection, Gender.MASCULINE, Case.GENITIVE) + " кубиках")).hint(hint).scenario(CUBES_SCENARIO).difficulty(difficulty).create();
        }

        final EilerCirclesScenario scenario = (EilerCirclesScenario) problemAvailability.getScenario();

        int x = difficulty == MEDIUM ? randomInt(8, 13) :
                difficulty == HARD ? randomInt(12, 20) : randomInt(1, 6);
        int z = difficulty == MEDIUM ? randomInt(8, 13) :
                difficulty == HARD ? randomInt(12, 20) : randomInt(1, 6);
        int y = difficulty == MEDIUM ? randomInt(5, Math.min(13, 30 - x - z)) :
                difficulty == HARD ? randomInt(10, Math.max(16, 45 - x - z)) : randomInt(Math.max(1, 8 - x - z), Math.min(8, 13 - x - z));

        int total = x + y + z;

        String[][] heroes = new String[][]{{"Петя", "Петином", "Пете"},
                {"Вася", "Васином", "Васе"},
                {"Дима", "Димином", "Диме"},
                {"Саша", "Сашином", "Саше"}};
        boolean excludeHero = randomInt(0, 2) == 0;
        String[] hero = heroes[randomInt(0, heroes.length)];

        int theme = randomInt(0, THEMES.length);

        final AbstractNoun[] chosenSubjects = chooseRandom(THEME_OBJECTS.get(THEMES[theme]), 2, AbstractNoun[]::new);

        final HashMap<String, Condition> conditions = Maps.newHashMap();
        conditions.put(X, new Condition(x, "не", chosenSubjects[1], chosenSubjects[0], false));
        conditions.put(Z, new Condition(z, "не", chosenSubjects[0], chosenSubjects[1], false));
        conditions.put(Y, new Condition(y, "", chosenSubjects[0], chosenSubjects[1], true));
        conditions.put(X_Y, new Condition(x + y, "", chosenSubjects[0]));
        conditions.put(Y_Z, new Condition(y + z, "", chosenSubjects[1]));
        conditions.put(TOTAL, new Condition(total, getScenarioVerb(THEMES[theme], excludeHero)));

        final Set<String> chosenVariables = scenario.getVars();
        final Set<String> scenarioVariables = new HashSet<>(chosenVariables);

        TextWithTTSBuilder text = new TextWithTTSBuilder();

        boolean startSentence = false;
        if (chosenVariables.contains(TOTAL)) {
            switch (THEMES[theme]) {
                case CLASS:
                    text.append("В ").append(hero[1]).append(" классе ");
                    if (!excludeHero) {
                        text.append(getNumWithString(total, STUDENT)).append(" и все ");
                    } else {
                        text.append(getNumWithString(total + 1, STUDENT));
                    }
                    startSentence = true;
                    break;
                case CAMP:
                    text.append("В лагерь приехали ");
                    if (!excludeHero) {
                        text.append(getNumWithString(total, CHILD)).append(" и ");
                    } else {
                        text.append(getNumWithString(total + 1, CHILD));
                    }
                    startSentence = true;
                    break;
                case PARTY:
                    text.append("На день рождения к ").append(hero[2]).append(" пришли ");
                    if (excludeHero) {
                        text.append(getNumWithString(total, CHILD)).append(", и ").append(hero[0]).append(" угостил всех конфетами");
                    } else {
                        text.append(getNumWithString(total - 1, CHILD)).append(" и на праздничном столе все дети ели конфеты");
                    }
                    startSentence = true;
                    break;
            }
            chosenVariables.remove(TOTAL);
        } else {
            switch (THEMES[theme]) {
                case CLASS:
                    text.append("В ").append(hero[1]).append(" классе ");
                    if (!excludeHero) {
                        text.append("все ");
                    }
                    startSentence = !excludeHero;
                    break;
                case CAMP:
                    text.append("В лагере ");
                    startSentence = !excludeHero;
                    break;
                case PARTY:
                    if (!excludeHero) {
                        text.append("На ").append(hero[1]).append(" дне рождения ").append(" все дети ели конфеты");
                    } else {
                        text.append("На своём дне рождения ").append(hero[0]).append(" угостил всех гостей конфетами");
                    }
                    startSentence = true;
                    break;
            }
        }
        if (!excludeHero) {
            if (THEMES[theme].equals(CLASS)) {
                text.append("занимаются ")
                        .append(chosenSubjects[0].getInstrumentalForm())
                        .append(" или ").append(chosenSubjects[1].getInstrumentalForm());
            } else if (THEMES[theme].equals(CAMP)) {
                text.append("оказалось, что все дети занимаются ").append(chosenSubjects[0].getInstrumentalForm())
                        .append(" или ").append(chosenSubjects[1].getInstrumentalForm());
            }
        }
        if (startSentence) {
            text.append(". ");
        }

        int verbCount = 0;
        if (chosenVariables.contains(X_Y)) {
            final Verb verb = selectVerb(THEMES[theme], verbCount++);
            if (chosenVariables.contains(Y_Z)) {
                text.append(conditions.get(X_Y).getTextWithCount(verb));
                text.append(", а " + conditions.get(Y_Z).getTextWithCountWithoutVerb(GOVERN_FORM.get(verb)));
                chosenVariables.remove(Y_Z);
            } else if (chosenVariables.contains(X)) {
                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(x + y), NumberWord.getStringForNumber(x + y, Gender.MASCULINE, Case.GENITIVE))
                        .append((x + y) % 10 == 1 ? " ребенка" : " детей").append(", которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[0]))
                        .append(", ").append(conditions.get(X).getTextWithCount(selectVerb(THEMES[theme], verbCount++)));
                chosenVariables.remove(X);
//            } else if (chosenVariables.contains(Y)) {
//                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(x + y), NumberWord.getStringForNumber(x + y, Gender.MASCULINE, Case.GENITIVE))
//                        .append(" детей, которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[0]))
//                        .append(", ").append(conditions.get(Y_Z).getTextWithCount(y, selectVerb(THEMES[theme], verbCount++)));
//                chosenVariables.remove(Y);
            } else {
                text.append(conditions.get(X_Y).getTextWithCount(selectVerb(THEMES[theme], verbCount++)));
            }
            chosenVariables.remove(X_Y);
            text.append(". ");
            startSentence = true;
        }

        if (chosenVariables.contains(Y_Z)) {
            final Verb verb = selectVerb(THEMES[theme], verbCount++);
            if (chosenVariables.contains(Z)) {
                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(y + z), NumberWord.getStringForNumber(y + z, Gender.MASCULINE, Case.GENITIVE))
                        .append((z + y) % 10 == 1 ? " ребенка" : " детей").append(", которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[1]))
                        .append(", ").append(conditions.get(Z).getTextWithCount(selectVerb(THEMES[theme], verbCount++)));
                chosenVariables.remove(Z);
//            } else if (chosenVariables.contains(Y)) {
//                text.append(startSentence ? "Среди " : "среди ").append(Integer.toString(y + z), NumberWord.getStringForNumber(y + z, Gender.MASCULINE, Case.GENITIVE))
//                        .append(" детей, которые ").append(verb.getPlural()).append(" ").append(GOVERN_FORM.get(verb).apply(chosenSubjects[1]))
//                        .append(", ").append(conditions.get(X_Y).getTextWithCount(y, selectVerb(THEMES[theme], verbCount++)));
//                chosenVariables.remove(Y);
            } else {
                text.append(conditions.get(Y_Z).getTextWithCount(selectVerb(THEMES[theme], verbCount++)));
            }
            chosenVariables.remove(Y_Z);
            text.append(". ");
        }


        for (String chosenVariable : chosenVariables) {
            if (excludeHero) {
                text.append(conditions.get(chosenVariable).getFullTextWithCount(selectVerb(THEMES[theme], verbCount++), selectVerb(THEMES[theme], verbCount++)));
            } else {
                text.append(conditions.get(chosenVariable).getTextWithCount(selectVerb(THEMES[theme], verbCount++)));
            }
            text.append(". ");
        }

        final String question = chooseQuestion(scenarioVariables);

        int answer = conditions.get(question).count;
        text.append("Сколько детей ");
        if (excludeHero) {
            text.append(conditions.get(question).getFullQuestion(selectVerb(THEMES[theme], verbCount++), selectVerb(THEMES[theme], verbCount)));
            if (THEMES[theme].equals(CLASS) || THEMES[theme].equals(CAMP)) {
                text.append(", если только ").append(hero[0]).append(" не занимается ни в одном кружке");
            }
        } else {
            text.append(conditions.get(question).getQuestion(selectVerb(THEMES[theme], verbCount)));
        }
        text.append("?");

        final HashSet<String> possibleTextAnswers = Sets.newHashSet(getNumWithString(answer, CHILD));
        for (Verb verb : VERBS.get(THEME_OBJECTS.get(THEMES[theme]))) {
            possibleTextAnswers.add(conditions.get(question).getTextWithCount(verb));
        }

        final String hint = "Нарисуйте схему. В один круг поместите всех детей, которые любят " + chosenSubjects[0].getAccusativePluralForm() +
                ", а в в другой всех детей, которые любят " + chosenSubjects[1].getAccusativePluralForm();
        return new ProblemWithPossibleTextAnswers.Builder().text(text.getText()).tts(text.getTTS()).answer(answer).theme(EILER_CIRCLES).possibleTextAnswers(possibleTextAnswers).hint(hint).scenario(scenario).difficulty(difficulty).create();//пример TTS

    }

    private String chooseQuestion(Set<String> scenarioVariables) {
        return chooseRandomElement(
                Sets.newHashSet(VARIABLES).stream()
                        .filter(o -> {
                            if (scenarioVariables.contains(o)) {
                                return false;
                            }
                            final ArrayList<HashSet<String>> incorrectCombinations =
                                    Lists.newArrayList(Sets.newHashSet(X, Y_Z, TOTAL),
                                            Sets.newHashSet(Z, X_Y, TOTAL)
                                            /*, Sets.newHashSet("x", "y", "x + y"),
                                            Sets.newHashSet("y", "z", "y + z")*/);
                            for (HashSet<String> incorrectCombination : incorrectCombinations) {
                                if (incorrectCombination.contains(o)) {
                                    incorrectCombination.remove(o);
                                    if (scenarioVariables.containsAll(incorrectCombination)) {
                                        return false;
                                    }
                                }
                            }
                            return true;
                        })
                        .collect(Collectors.toSet()));
    }

    private Verb selectVerb(String scenario, int i) {
        final Verb[] verbs = VERBS.get(THEME_OBJECTS.get(scenario));
        return verbs[i % verbs.length];
    }


    private static class Condition {
        int count;
        private final String text;
        private final AbstractNoun object1;
        private final AbstractNoun object2;
        private final boolean bothObjects;

        Condition(int count, String text, AbstractNoun object) {
            this(count, text, object, null, false);
        }

        Condition(int count, String text, AbstractNoun object1, AbstractNoun object2, boolean bothObjects) {
            this.count = count;
            this.text = text;
            this.object1 = object1;
            this.object2 = object2;
            this.bothObjects = bothObjects;
        }

        Condition(int count, String text) {
            this(count, text, null, null, false);
        }

        String getTextWithCount(Verb verb) {
            if (object1 == null) {
                return getNumWithString(count, CHILD) + " " + (text.isEmpty() ? "" : (text + " "));
            } else {
                return getNumWithString(count, CHILD) + " "
                        + (text.isEmpty() ? "" : (text + " "))
                        + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                        + (bothObjects ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (bothObjects ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getTextWithCount(int count, Verb verb) {
            if (object1 == null) {
                return getNumWithString(count, CHILD) + " " + (text.isEmpty() ? "" : (text + " "));
            } else {
                return getNumWithString(count, CHILD) + " "
                        + (text.isEmpty() ? "" : (text + " "))
                        + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                        + (bothObjects ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (bothObjects ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getFullTextWithCount(Verb verb, Verb verb2) {
            if (object1 == null || object2 == null || bothObjects) {
                return getTextWithCount(verb);
            }
            return getNumWithString(count, CHILD) + " "
                    + (count % 10 == 1 ? verb.getSingular() : verb.getPlural()) + " "
                    + GOVERN_FORM.get(verb).apply(object2)
                    + ", но " + (text.isEmpty() ? "" : (text + " "))
                    + (count % 10 == 1 ? verb2.getSingular() : verb2.getPlural()) + " "
                    + GOVERN_FORM.get(verb2).apply(object1);

        }


        String getTextWithCountWithoutVerb(Function<AbstractNoun, String> governForm) {
            return getNumWithString(count, CHILD) + " " + text + " - "
                    + (bothObjects ? "и " : "")
                    + governForm.apply(object1)
                    + (bothObjects ? (" и " + governForm.apply(object2)) : "");
        }

        String getQuestion(Verb verb) {
            if (object1 == null) {
                return text;
            } else {
                return (text.isEmpty() ? "" : (text + " "))
                        + verb.getPlural() + " "
                        + (bothObjects ? "и " : "")
                        + GOVERN_FORM.get(verb).apply(object1)
                        + (bothObjects ? (", и " + GOVERN_FORM.get(verb).apply(object2)) : "");
            }
        }

        String getFullQuestion(Verb verb, Verb verb2) {
            if (object1 == null || object2 == null || bothObjects) {
                return getQuestion(verb);
            }
            return verb.getPlural() + " "
                    + GOVERN_FORM.get(verb).apply(object2)
                    + ", но " + (text.isEmpty() ? "" : (text + " "))
                    + verb2.getPlural() + " "
                    + GOVERN_FORM.get(verb2).apply(object1);

        }
    }

    private String getScenarioVerb(String scenario, boolean excludeHero) {
        if (excludeHero) {
            if (scenario.equals(CLASS) || scenario.equals(CAMP)) {
                return randomInt(0, 2) == 0 ? "ходят в кружки" : "ходят хотя бы в один кружок";
            } else if (scenario.equals(PARTY)) {
                return randomInt(0, 2) == 0 ? "пришли в гости" : "cъели хотя бы одну конфету";
            }
        }
        switch (scenario) {
            case CLASS:
                return "учится в классе";
            case CAMP:
                return "приехали в лагерь";
            case PARTY:
                return "были на дне рождения";
        }
        throw new IllegalArgumentException();
    }

    private String chooseRandomElement(Collection<String> collection) {
        final ArrayList<String> list = Lists.newArrayList(collection);
        return list.get(randomInt(0, list.size()));
    }

    @Nullable
    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return findAvailableScenario(difficulty, alreadySolvedProblems, EASY_SCENARIOS, new HashSet<>());
            case MEDIUM:
                return findAvailableScenario(difficulty, alreadySolvedProblems, SCENARIOS, Sets.newHashSet(EASY_SCENARIOS));
            case HARD:
                final List<ProblemScenario> scenarios = new ArrayList<>(SCENARIOS);
                scenarios.add(CANDIES_STORE_SCENARIO);
                return findAvailableScenario(difficulty, alreadySolvedProblems, scenarios, Sets.newHashSet(SCENARIOS));
            case EXPERT:
                return findAvailableScenario(difficulty, alreadySolvedProblems, Lists.newArrayList(EXPERT_CANDIES_SCENARIO), Sets.newHashSet(SCENARIOS));

        }

        throw new IllegalArgumentException();
    }

    private static class EilerCirclesScenario extends ProblemScenarioImpl {

        private final String var1;
        private final String var2;
        private final String var3;

        EilerCirclesScenario(String var1, String var2, String var3) {
            super(ProblemCollection.EILER_CIRCLES + "_" + var1 + "_" + var2 + "_" + var3);
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = var3;
        }

        public String getVar1() {
            return var1;
        }

        public String getVar2() {
            return var2;
        }

        public String getVar3() {
            return var3;
        }

        Set<String> getVars() {
            return Sets.newHashSet(var1, var2, var3);
        }
    }
}
