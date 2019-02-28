package com.dinoproblems.server.generators;

import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemGenerator;
import com.dinoproblems.server.ProblemWithPossibleTextAnswers;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import static com.dinoproblems.server.ProblemCollection.AT_LEAST_ONE_FOUND;
import static com.dinoproblems.server.generators.GeneratorUtils.*;

/**
 * Created by Katushka on 27.02.2019.
 */
public class FoundAtLeastOneGenerator implements ProblemGenerator {

    @Override
    public Problem generateProblem(Problem.Difficulty difficulty) {
        int total = randomInt(20, 31);

        int count1 = randomInt(2, total - 2);
        int count2 = total - count1;
        int question = randomInt(0, 2);
        String text = "";
        int answer = question == 0 ? count1 : count2;

        int scenario = randomInt(0, 5);
        Noun[] things = {Dictionary.FLOWER, Dictionary.FRUIT, Dictionary.CANDY, Dictionary.BALL, Dictionary.CHILD};
        AbstractNoun[][] particularThings = {{Dictionary.TULIP, Dictionary.ROSE, Dictionary.DAFFODIL, Dictionary.CAMOMILE},
                {Dictionary.APPLE, Dictionary.PEAR, Dictionary.ORANGE, Dictionary.BANANA},
                {Dictionary.CHOCOLATE_CANDY, Dictionary.JELLY_CANDY, Dictionary.LOLLIPOP},
                {new AdjectiveWithNoun(Dictionary.RED, Dictionary.BALL), new AdjectiveWithNoun(Dictionary.GREEN, Dictionary.BALL), new AdjectiveWithNoun(Dictionary.BLUE, Dictionary.BALL), new AdjectiveWithNoun(Dictionary.YELLOW, Dictionary.BALL)},
                {Dictionary.BOY, Dictionary.GIRL}};
        AbstractNoun[] chosenParticularThings = chooseRandom(particularThings[scenario], 2, AbstractNoun[]::new);

        String tts = "";
        switch (scenario) {
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
                tts += "В корзине лежит ";
                break;
            case 3:
                text += "Клоун надул ";
                tts += "В корзине лежит ";
                break;
            case 4:
                text += "В классе ";
                tts += "В корзине лежит ";
                break;
        }

        text += getNumWithString(total, things[scenario]) + ". Известно, что среди любых ";
        tts += getNumWithString(total, things[scenario])+ ". Известно, что среди любых ";

        text += (count1 + 1);
        tts += NumberWord.getStringForNumber(count1 + 1, chosenParticularThings[0].getGender(), Case.GENITIVE);

        text += " найдется хотя бы " + getNumWithString(1, chosenParticularThings[1]) + ", а среди любых "
                + (count2 + 1);
        tts += " найдется хотя бы " + getNumWithString(1, chosenParticularThings[1]) + ", а среди любых "
                + NumberWord.getStringForNumber(count2 + 1, chosenParticularThings[0].getGender(), Case.GENITIVE);;

        String textEnd = " найдется хотя бы " + getNumWithString(1, chosenParticularThings[0])
                + ". Сколько " + chosenParticularThings[question].getCountingForm();
        switch (scenario) {
            case 0:
                textEnd += " в вазе?";
                break;
            case 1:
                textEnd += " в корзине?";
                break;
            case 2:
                textEnd += " в коробке?";
                break;
            case 3:
                textEnd += " надул клоун?";
                break;
            case 4:
                textEnd += " в классе?";
                break;
        }
        text += textEnd;
        tts += textEnd;

        final HashSet<String> possibleTextAnswers = Sets.newHashSet(getNumWithString(answer, chosenParticularThings[question]));
        return new ProblemWithPossibleTextAnswers(text, tts, answer, AT_LEAST_ONE_FOUND, possibleTextAnswers);
    }

    @Override
    public Set<Problem.Difficulty> getAvailableDifficulties() {
        return Sets.newHashSet(Problem.Difficulty.MEDIUM);
    }
}
