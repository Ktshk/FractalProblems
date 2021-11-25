package com.dinoproblems.server.session;

import com.dinoproblems.server.MainServlet;
import com.dinoproblems.server.UserInfo;
import com.dinoproblems.server.generators.QuestProblems;
import com.dinoproblems.server.generators.QuestProblemsLoader;
import com.dinoproblems.server.utils.Dictionary;
import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import static com.dinoproblems.server.session.Session.checkAnswer;
import static com.dinoproblems.server.session.Session.YES_ANSWERS;
import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;
import static com.dinoproblems.server.utils.GeneratorUtils.getNumWithString;
import static com.dinoproblems.server.utils.GeneratorUtils.upperCaseFirstLetter;

/**
 * Created by Katushka on 04.06.2019.
 */
public class MenuSessionState implements SessionState {
    private final static String[] MEET_ONCE_MORE = {"Рада снова слышать вас, ", "Здравствуйте, ", "Очень приятно снова слышать вас, "};
    private final static String[] TOTAL_SCORE = {"У вас на счету ", "У вас уже ", "У меня записано, что вы набрали уже ", "Ваш счет: ", "Вы набрали уже "};
    private final static Set<String> VARIOUS_PROBLEMS = Sets.newHashSet("разные", "разные задачи", "разнобой",
            "решать разные задачи", "решать разнобой", "порешаем разные задачи");
    private final static Set<String> PROBLEM_OF_THE_DAY = Sets.newHashSet("задачу дня", "задача дня", "решать задачу дня",
            "решать задачу дня", "решим задачу дня");

    private final static String MENU_TEXT = "Чтобы решать олимпиадные задачи различной сложности, скажите: Разные задачи. " +
            "Чтобы решить задачу дня, скажите: Задача дня. Если хотите посмотреть таблицу рекордов, нажмите на кнопку: Рекорды";
    private final static String QUEST_MENU_TEXT = "Чтобы решать олимпиадные задачи различной сложности, скажите: Разные задачи. " +
            "Чтобы решить задачу квеста, скажите: <quest name>. Если хотите посмотреть таблицу рекордов, нажмите на кнопку: Рекорды";


    private final boolean initial;
    private final TextWithTTSBuilder prefixText;

    public MenuSessionState(boolean initial) {
        this.initial = initial;
        prefixText = null;
    }

    public MenuSessionState(TextWithTTSBuilder prefixText) {
        this.initial = false;
        this.prefixText = prefixText;
    }

    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session, String timeZone) {
        if (checkAnswer(command, VARIOUS_PROBLEMS, YES_ANSWERS)) {
            return new ChooseDifficultySessionState(null);
        } else {
            QuestProblems questProblems = QuestProblemsLoader.INSTANCE.getCurrentQuestProblems(Calendar.getInstance(TimeZone.getTimeZone(timeZone)));
            if (checkAnswer(command, PROBLEM_OF_THE_DAY, YES_ANSWERS)
                    || (questProblems != null && checkAnswer(command, Sets.newHashSet(questProblems.getName().toLowerCase()), YES_ANSWERS))) {
                return new ProblemOfTheDaySessionState(true);
            } else {
                return new MenuSessionState(null);
            }
        }
    }

    @Nonnull
    @Override
    public SessionState addTextPrefix(String text) {
        return new MenuSessionState(prefixText == null ? new TextWithTTSBuilder().append(text) : prefixText.append(text));
    }

    @Override
    public void processRequest(JsonObject responseJson, Session session, String timeZone) {
        final UserInfo userInfo = session.getUserInfo();
        QuestProblems questProblems = QuestProblemsLoader.INSTANCE.getCurrentQuestProblems(Calendar.getInstance(TimeZone.getTimeZone(timeZone)));

        final TextWithTTSBuilder text;
        if (initial) {
            String helloText = chooseRandomElement(MEET_ONCE_MORE) + upperCaseFirstLetter(userInfo.getName()) + "! ";
            if (userInfo.getTotalScore() > 0) {
                helloText += chooseRandomElement(TOTAL_SCORE) + getNumWithString(userInfo.getTotalScore(), Dictionary.SCORE, GeneratorUtils.Case.NOMINATIVE) + ". ";
            }
            text = new TextWithTTSBuilder().append(helloText)
                    .append("Чем займёмся сегодня? ");
        } else if (prefixText != null) {
            text = new TextWithTTSBuilder().append(prefixText.getText(), prefixText.getTTS());
        } else {
            text = new TextWithTTSBuilder();
        }
        if (questProblems == null) {
            text.append(MENU_TEXT);
        } else{
            String questCommand = QUEST_MENU_TEXT.replace("<quest name>", questProblems.getName());
            text.append(questCommand, questCommand.replace("квеста", "квэста"));
        }
        responseJson.addProperty("text", text.getText());
        responseJson.addProperty("tts", text.getTTS() == null ? text.getText() : text.getTTS());
        session.setLastServerResponse(text.getText());

        createMenu(session, responseJson, questProblems,
                initial ? "Добро пожаловать, " + upperCaseFirstLetter(userInfo.getName()) + "!"
                        : "Чем займёмся, " + upperCaseFirstLetter(userInfo.getName()) + "?", timeZone);
    }

    private void createMenu(Session session, JsonObject responseJson, @Nullable QuestProblems questProblems, String text, String timeZone) {
        final JsonObject cardObject = new JsonObject();
        cardObject.addProperty("type", "ItemsList");
        final JsonArray itemsArray = new JsonArray();

        final JsonObject variousItem = new JsonObject();
        variousItem.addProperty("image_id", "1521359/6b338a728a63aaad1149");
        variousItem.addProperty("title", "Разные задачи");
        variousItem.addProperty("description", "Олимпиадные задачи по математике разной сложности");

        final JsonObject variousButton = new JsonObject();
        variousButton.addProperty("text", "разные задачи");
        final JsonObject variousPayload = new JsonObject();
        variousPayload.addProperty("command", "разные задачи");
        variousButton.add("payload", variousPayload);
        variousItem.add("button", variousButton);

//        final JsonObject themedProblems = new JsonObject();
//        themedProblems.addProperty("image_id", "1652229/ea3ede6efe1f40787c4e"); // TODO: change icon id with various
//        themedProblems.addProperty("title", "Тематические задачи");
//        themedProblems.addProperty("description", "Подборка задач на определенную тему");

        final JsonObject problemOfTheDay = new JsonObject();
        problemOfTheDay.addProperty("image_id", "997614/90e63015f03de2a5b6b3");

        if (questProblems == null) {
            problemOfTheDay.addProperty("title", "Задача дня");
            problemOfTheDay.addProperty("description", "Каждый день новая задача, над которой придется поломать голову");
        } else {
            problemOfTheDay.addProperty("title", questProblems.getName());
            problemOfTheDay.addProperty("description", questProblems.getDescription());
        }

        final JsonObject problemOfTheDayButton = new JsonObject();
        problemOfTheDayButton.addProperty("text", "задача дня");
        final JsonObject problemOfTheDayPayload = new JsonObject();
        problemOfTheDayPayload.addProperty("command", "задача дня");
        problemOfTheDayButton.add("payload", problemOfTheDayPayload);
        problemOfTheDay.add("button", problemOfTheDayButton);

        final JsonObject recordsItem = new JsonObject();
        recordsItem.addProperty("image_id", "1656841/590709f9fb9b7cd0b60e");
        recordsItem.addProperty("title", "Рекорды");
        recordsItem.addProperty("description", "Таблица рекордов");

        final JsonObject recordsButton = new JsonObject();
        String url = "http://" + MainServlet.URL + "/records";
        if (session.getUserInfo() != null) {
            url += "?user_id=" + session.getUserInfo().getDeviceId() + "#selected";
        }
        recordsButton.addProperty("url", url);
        recordsItem.add("button", recordsButton);

        itemsArray.add(variousItem);
        itemsArray.add(problemOfTheDay);
//        itemsArray.add(themedProblems);
        itemsArray.add(recordsItem);

        cardObject.add("items", itemsArray);

        final JsonObject headerObject = new JsonObject();
        headerObject.addProperty("text", text);
        cardObject.add("header", headerObject);

        responseJson.add("card", cardObject);
    }
}
