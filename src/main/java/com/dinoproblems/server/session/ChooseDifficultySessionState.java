package com.dinoproblems.server.session;

import com.dinoproblems.server.DataBaseService;
import com.dinoproblems.server.Problem;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.util.HashSet;

import static com.dinoproblems.server.session.Session.*;
import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;

/**
 * Created by Katushka on 04.06.2019.
 */
public class ChooseDifficultySessionState implements SessionState {
    private final static HashSet<String> EASY = Sets.newHashSet("простая", "простую", "простой", "простое", "простую задачу");
    private final static HashSet<String> MEDIUM = Sets.newHashSet("средняя", "среднюю", "средний", "средне", "среднюю задачу");
    private final static HashSet<String> HARD = Sets.newHashSet("сложная", "сложную", "сложный", "сложно", "сложную задачу");
//    private final static HashSet<String> EXPERT = Sets.newHashSet("эксперт", "экспертная", "задачу для экспертов");

    private final String text;

    public ChooseDifficultySessionState(String text) {
        this.text = text;
    }

    public ChooseDifficultySessionState() {
        this(null);
    }


    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session, String timeZone) {
        final Problem.Difficulty currentDifficulty = parseDifficulty(command);
        if (currentDifficulty == null) {
            DataBaseService.INSTANCE.updateMiscAnswersTable(command, "", session.getLastServerResponse());
            return new ChooseDifficultySessionState(chooseRandomElement(DID_NOT_UNDERSTAND));
        } else {
            session.setCurrentDifficulty(currentDifficulty);
            if (session.getNextProblem() == null) {
                final String text = "Извините, но вы уже решили все мои задачи на этом уровне сложности. Может быть, порешаем задачи другой сложности?";
                return new ChooseDifficultySessionState(text);
            }
            return new SolvingProblemSessionState(true, session);
        }
    }

    @Override
    public void processRequest(JsonObject responseJson, Session session, String timeZone) {
        final String text;
        if (this.text != null) {
            text = this.text + "Выберите пожалуйста сложность задач";
        } else {
            text = "Какую задачу будем решать: простую, среднюю или сложную?";
        }
        responseJson.addProperty("text", text);
        session.setLastServerResponse(text);
        responseJson.add("buttons", createDifficultyButtons(session));
    }

    @Nonnull
    @Override
    public SessionState addTextPrefix(String text) {
        if (this.text == null) {
            return new ChooseDifficultySessionState(text);
        }
        return new ChooseDifficultySessionState(text + this.text);
    }

    private JsonArray createDifficultyButtons(Session session) {
        final JsonArray buttons = new JsonArray();
        final Problem.Difficulty currentDifficulty = session.getCurrentDifficulty();

        if (currentDifficulty != Problem.Difficulty.EASY) {
            buttons.add(createButton("простая", false));
        }
        if (currentDifficulty != Problem.Difficulty.MEDIUM) {
            buttons.add(createButton("средняя", false));
        }
        if (currentDifficulty != Problem.Difficulty.HARD) {
            buttons.add(createButton("сложная", false));
        }
        buttons.add(createLeaderboardButton(session.getUserInfo(), false, false));
        if (currentDifficulty != null) {
            buttons.add(createButton("меню", false));
        }
        return buttons;
    }

    private Problem.Difficulty parseDifficulty(String command) {
        if (checkAnswer(command, EASY, YES_ANSWERS)) {
            return Problem.Difficulty.EASY;
        } else if (checkAnswer(command, MEDIUM, YES_ANSWERS)) {
            return Problem.Difficulty.MEDIUM;
        } else if (checkAnswer(command, HARD, YES_ANSWERS)) {
            return Problem.Difficulty.HARD;
//        } else if (checkAnswer(command, EXPERT, YES_ANSWERS)) {
//            return Problem.Difficulty.EXPERT;
        } else {
            return null;
        }
    }
}
