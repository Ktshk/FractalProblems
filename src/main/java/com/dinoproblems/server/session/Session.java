package com.dinoproblems.server.session;

import com.dinoproblems.server.MainServlet;
import com.dinoproblems.server.Problem;
import com.dinoproblems.server.ProblemCollection;
import com.dinoproblems.server.UserInfo;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.dinoproblems.server.session.SessionResult.EXPERT_SOLVED_POINTS;
import static com.dinoproblems.server.session.SessionResult.EXPERT_SOLVED_WITH_HINT_POINTS;

/**
 * Created by Katushka
 * on 10.02.2019.
 */
public class Session {
    private final static String HELP = "Я предлагаю Вам решить несколько олимпиадных задач от системы кружков олимпиадной математики Фрактал. " +
            "Сначала вы выбираете сложность: простую, среднюю или сложную. " +
            "За каждую решенную задачу я буду начислять вам баллы. За нерешенные задачи, баллы будут сниматься. " +
            "Если задача кажется вам слишком сложной, я могу повторить условие или дать вам подсказку. " +
            "Но, учтите, что за задачу, решенную с подсказкой, я буду давать вам меньше баллов.";
    private final static Set<String> END_SESSION_ANSWERS = Sets.newHashSet("хватит", "больше не хочу", "давай закончим",
            "надоело", "закончить", "заканчивай", "кончай", "мне надоело");
    final static Set<String> ENOUGH_ANSWERS = Sets.newHashSet("нет", "нет спасибо", "не хочу", "хватит", "не надо",
            "не", "все не хочу", "больше не хочу", "не хочу больше", "не хочу еще задачу", "не хочу решить задачу",
            "нет спасибо", "заканчивай", "выйти");
    private final static Set<String> MEANINGLESS_WORDS = Sets.newHashSet("ну", "пожалуйста", "а", "спасибо");
    final static Set<String> YES_ANSWERS = Sets.newHashSet("да", "давай", "давайте", "ну давай", "хочу", "валяй",
            "можно", "ага", "угу", "конечно", "хорошо", "окей", "правильно", "я не против", "точно", "ок");
    final static String[] DID_NOT_UNDERSTAND = {"Не поняла вас. ", "Не понимаю. ", "Мне кажется, это не было ответом на мой вопрос. ",
            "Не уверена, что поняла вас правильно. ", "Даже не знаю, как на это реагировать. "};


    private final String sessionId;
    private final String clientId;
    private final String userId;
    private final SessionResult sessionResult = new SessionResult();
    private final Map<Problem.Difficulty, Problem> nextProblem = new HashMap<>();

    private Problem.Difficulty currentDifficulty = null;
    private String lastServerResponse;

    private UserInfo userInfo = null;

    private SessionState sessionState;

    public Session(String sessionId, String clientId, String userId) {
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.userId = userId;
    }

    public Session(UserInfo userInfo, String sessionId, String clientId) {
        this.userInfo = userInfo;
        this.sessionId = sessionId;
        this.clientId = clientId;
        userId = userInfo.getDeviceId();
    }

    public String getUserId() {
        return userInfo == null ? userId : userInfo.getDeviceId();
    }

    public String getClientId() {
        return clientId;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Problem getCurrentProblem() {
        return userInfo == null ? null : userInfo.getCurrentProblem(getCurrentDifficulty());
    }

    public void setCurrentProblem(Problem currentProblem) {
        if (userInfo != null) {
            userInfo.setCurrentProblem(currentProblem, getCurrentDifficulty());
        }
    }

    public Problem.Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(Problem.Difficulty currentDifficulty) {
        this.currentDifficulty = currentDifficulty;

        populateNextProblem();
    }

    private void generateNextProblem() {
        final Problem currentDifficultyProblem = ProblemCollection.INSTANCE.generateProblem(getUserInfo(), getCurrentDifficulty(), null);
        if (currentDifficultyProblem == null) {
            nextProblem.remove(getCurrentDifficulty());
        } else {
            nextProblem.put(getCurrentDifficulty(), currentDifficultyProblem);
        }
    }

    private void populateNextProblem() {
        for (Problem.Difficulty difficulty : Problem.Difficulty.values()) {
            if (difficulty == Problem.Difficulty.EXPERT) {
                continue;
            }
            if (!nextProblem.containsKey(difficulty)) {
                final Problem problem = ProblemCollection.INSTANCE.generateProblem(getUserInfo(), difficulty, null);
                if (problem != null) {
                    nextProblem.put(difficulty, problem);
                }
            }
        }
    }

    public String getLastServerResponse() {
        return lastServerResponse;
    }

    public void setLastServerResponse(String lastServerResponse) {
        this.lastServerResponse = lastServerResponse;
    }

    public int updateScore(Problem problem) {
        final int points;
        if (problem.getDifficulty() != Problem.Difficulty.EXPERT) {
            userInfo.setCurrentProblem(null, getCurrentDifficulty());
            points = sessionResult.updateScore(problem);
        }  else {
            points = problem.getState() == Problem.State.SOLVED ? EXPERT_SOLVED_POINTS :
                    problem.getState() == Problem.State.SOLVED_WITH_HINT ? EXPERT_SOLVED_WITH_HINT_POINTS : 0;
        }
        userInfo.addSolvedProblem(problem.getTheme(), problem, points);

        if (problem.getDifficulty() != Problem.Difficulty.EXPERT) {
            generateNextProblem();
        }
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return sessionId != null ? sessionId.equals(session.sessionId) : session.sessionId == null;
    }

    @Override
    public int hashCode() {
        return sessionId != null ? sessionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }

    @Nonnull
    public SessionResult getSessionResult() {
        return sessionResult;
    }

    @Nullable
    public Problem getNextProblem() {
        return nextProblem.getOrDefault(getCurrentDifficulty(), null);
    }

    public SessionState processRequest(@Nullable String command, JsonObject result, JsonArray entitiesArray, String timeZone) {
        final JsonObject responseJson = new JsonObject();
        responseJson.addProperty("end_session", false);

        if (sessionState == null) {
            if (userInfo == null) {
                sessionState = new RegistrationSessionState();
            } else {
                sessionState = new MenuSessionState(true);
            }
            sessionState.processRequest(responseJson, this, timeZone);
        } else if ("помощь".equalsIgnoreCase(command) || "что ты умеешь".equalsIgnoreCase(command) || "правила".equalsIgnoreCase(command)) {
            responseJson.addProperty("text", HELP);
        } else if (Objects.equals(command, "end session") ||
                checkAnswer(command, END_SESSION_ANSWERS)) {
            final TextWithTTSBuilder sessionResult = getSessionResult().getResult(getUserInfo() == null ? 0 : getUserInfo().getTotalScore());
            responseJson.addProperty("text", sessionResult.getText());//итоговое сообщение пользователю
            responseJson.addProperty("tts", sessionResult.getTTS());//корректное произношение итогов сессии
            responseJson.addProperty("end_session", true);
        } else if (checkAnswer(command, RegistrationSessionState.CHANGE_NAME, YES_ANSWERS)) {
            sessionState = new RegistrationSessionState(new MenuSessionState(false));
            sessionState.processRequest(responseJson, this, timeZone);
        } else {
            sessionState = sessionState.getNextState(command, entitiesArray, this, timeZone);
            sessionState.processRequest(responseJson, this, timeZone);
        }

        setLastServerResponse(responseJson.get("text").getAsString());

        result.add("response", responseJson);

        return sessionState;
    }

    static boolean checkAnswer(String answer, Set<String> answerCollection) {
        return checkAnswer(answer, answerCollection, answerCollection);
    }

    static boolean checkAnswer(String answer, Set<String> answerCollection, Set<String> preAnswerCollection) {
        if (answer == null) {
            return false;
        }
        if (answerCollection.contains(answer.toLowerCase())) {
            return true;
        }

        StringBuilder answerMod = new StringBuilder().append(answer.charAt(0));
        for (int i = 1; i < answer.length(); i++) {
            if (answer.charAt(i - 1) == ' ' && answer.charAt(i) == ' ') {
                continue;
            }
            if (answer.charAt(i) == '.' || answer.charAt(i) == ',' || answer.charAt(i) == '!' || answer.charAt(i) == '?') {
                continue;
            }
            answerMod.append(answer.charAt(i));
        }
        answer = answerMod.toString();

        final String[] words = answer.split(" ");
        answerMod = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!MEANINGLESS_WORDS.contains(word)) {
                answerMod.append(word);
                if (i < words.length - 1) {
                    answerMod.append(' ');
                }
            }
        }
        answer = answerMod.toString();
        answer = answer.trim();

        if (answerCollection.contains(answer.toLowerCase())) {
            return true;
        }
        for (String yesAnswer1 : preAnswerCollection) {
            for (String yesAnswer2 : answerCollection) {
                if ((yesAnswer1 + ' ' + yesAnswer2).equalsIgnoreCase(answer)) {
                    return true;
                }
            }
        }
        return false;
    }

    static JsonObject createButton(String title, boolean hideButton) {
        final JsonObject buttonJson = new JsonObject();
        buttonJson.addProperty("title", title);
        if (hideButton) {
            buttonJson.addProperty("hide", true);
        }
        return buttonJson;
    }

    static JsonObject createLeaderboardButton(UserInfo userInfo, boolean hideButton, boolean showExpertRecords) {
        final JsonObject buttonJson = createButton("Рекорды", true);
        String url = "http://" + MainServlet.URL + "/records";
        if (showExpertRecords) {
            url += "?button=1";
        } else {
            url += "?button=0";
        }
        if (userInfo != null) {
            url += "&user_id=" + userInfo.getDeviceId() + "#selected";
        }
        buttonJson.addProperty("url", url);
        if (hideButton) {
            buttonJson.addProperty("hide", true);
        }
        return buttonJson;
    }
}
