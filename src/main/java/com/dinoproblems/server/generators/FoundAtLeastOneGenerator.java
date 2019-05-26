package com.dinoproblems.server.generators;

import com.dinoproblems.server.*;
import com.dinoproblems.server.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;

import static com.dinoproblems.server.ProblemCollection.AT_LEAST_ONE_FOUND;
import static com.dinoproblems.server.utils.GeneratorUtils.*;

/**
 * Created by Katushka on 27.02.2019.
 */
public class FoundAtLeastOneGenerator implements ProblemGenerator {

    private final static ProblemScenario DEFAULT_SCENARIO = new ProblemScenarioImpl(ProblemCollection.AT_LEAST_ONE_FOUND);

    @Nonnull
    @Override
    public Problem generateProblem(Problem.Difficulty difficulty, ProblemAvailability problemAvailability) {
        int total = randomInt(20, 31);

        int count1 = randomInt(2, total - 2);
        int count2 = total - count1;
        int question = randomInt(0, 2);
        String text = "";
        int answer = question == 0 ? count1 : count2;

        int theme = randomInt(0, 5);
        Noun[] things = {Dictionary.FLOWER, Dictionary.FRUIT, Dictionary.CANDY, Dictionary.BALL, Dictionary.CHILD};
        AbstractNoun[][] particularThings = {{Dictionary.TULIP, Dictionary.ROSE, Dictionary.DAFFODIL, Dictionary.CAMOMILE},
                Dictionary.FRUITS,
                Dictionary.CANDIES,
                {new AdjectiveWithNoun(Dictionary.RED, Dictionary.BALL), new AdjectiveWithNoun(Dictionary.GREEN, Dictionary.BALL), new AdjectiveWithNoun(Dictionary.BLUE, Dictionary.BALL), new AdjectiveWithNoun(Dictionary.YELLOW, Dictionary.BALL)},
                {Dictionary.BOY, Dictionary.GIRL}};
        AbstractNoun[] chosenParticularThings = chooseRandom(particularThings[theme], 2, AbstractNoun[]::new);

        String tts = "";
        switch (theme) {
            case 0:
                text += "В вазе стоит ";
                tts += "В вазе сто+ит ";
                break;
            case 1:
                text += "В корзине лежит ";
                tts += "В корзине лежит ";
                break;
            case 2:
                text += "В коробке ";
                tts += "В коробке ";
                break;
            case 3:
                text += "Клоун надул ";
                tts += "Клоун надул ";
                break;
            case 4:
                text += "В классе ";
                tts += "В классе ";
                break;
        }

        text += getNumWithString(total, things[theme]) + ". Известно, что среди любых ";
        tts += getNumWithString(total, things[theme])+ ". Известно, что среди любых ";

        text += (count1 + 1);
        tts += NumberWord.getStringForNumber(count1 + 1, things[theme].getGender(), Case.GENITIVE);

        text += " найдется хотя бы " + getNumWithString(1, chosenParticularThings[1]) + ", а среди любых "
                + (count2 + 1);
        tts += " найдется хотя бы " + getNumWithString(1, chosenParticularThings[1]) + ", а среди любых "
                + NumberWord.getStringForNumber(count2 + 1, things[theme].getGender(), Case.GENITIVE);;

        String textEnd = " найдется хотя бы " + getNumWithString(1, chosenParticularThings[0])
                + ". Сколько " + chosenParticularThings[question].getCountingForm() + getWhere(theme);
        text += textEnd + "?";
        tts += textEnd + "?";

        final HashSet<String> possibleTextAnswers = Sets.newHashSet(getNumWithString(answer, chosenParticularThings[question]));
        final String hint = "Подумайте, может ли " + chosenParticularThings[0].getCountingForm() + " быть больше или равно " +
                NumberWord.getStringForNumber(count1 + 1, chosenParticularThings[0].getGender(), Case.GENITIVE);
        return new ProblemWithPossibleTextAnswers.Builder().text(text).tts(tts).answer(answer).theme(AT_LEAST_ONE_FOUND).possibleTextAnswers(possibleTextAnswers).hint(hint).scenario(problemAvailability.getScenario()).difficulty(difficulty).create();
    }

    private String getWhere(int scenario) {
        switch (scenario) {
            case 0:
                return " в вазе";
            case 1:
                return " в корзине";
            case 2:
                return " в коробке";
            case 3:
                return " надул клоун";
            case 4:
                return " в классе";
        }
        return "";
    }

    @Override
    public ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Problem.Difficulty difficulty) {
        if (difficulty != Problem.Difficulty.HARD) {
            return null;
        }

        return GeneratorUtils.findAvailableScenario(difficulty, alreadySolvedProblems,
                Lists.newArrayList(DEFAULT_SCENARIO), new HashSet<>());
    }
}
