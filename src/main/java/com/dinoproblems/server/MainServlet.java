package com.dinoproblems.server;

import com.dinoproblems.server.utils.GeneratorUtils;
import com.dinoproblems.server.utils.ProblemTextBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;


/**
 * Created by Katushka on 07.01.2019.
 */
@WebServlet("/DemoServlet")
public class MainServlet extends HttpServlet {

    private Map<String, Session> currentSessions = new HashMap<>();
    private Map<String, UserInfo> userInfos = new HashMap<>();
    private Set<String> userNames = new HashSet<>();

    private Set<String> yesAnswers = Sets.newHashSet("да", "давай", "давайте", "ну давай", "хочу", "валяй",
            "можно", "ага", "угу", "конечно", "хорошо", "окей", "правильно");
    private Set<String> continueAnswers = Sets.newHashSet("продолжим", "продолжаем", "давай задачу", "решаем",
            "дальше", "еще одну", "ещё одну", "еще 1", "давай решим", "давай решим еще одну", "решаем дальше", "говори",
            "говори следующую задачу", "хочу решить задачу", "жги");
    private Set<String> noAnswers = Sets.newHashSet("нет", "не", "неправильно");
    private Set<String> enoughAnswers = Sets.newHashSet("нет", "не хочу", "хватит", "не надо", "не", "все не хочу");
    private Set<String> endSessionAnswers = Sets.newHashSet("хватит", "больше не хочу", "давай закончим", "надоело", "закончить", "заканчивай", "кончай");
    private Set<String> askAnswer = Sets.newHashSet("ответ", "сдаюсь", "сказать ответ", "скажи ответ");
    private Set<String> askHint = Sets.newHashSet("подсказка", "подсказку", "сказать подсказку", "дать подсказку", "дай", "давай", "дай подсказку", "подскажи");
    private Set<String> askToRepeat = Sets.newHashSet("повтори", "повторить", "повтори задачу", "повтори условие");
    private String[] praises = {"Молодец!", "Это правильный ответ.", "Ну конечно! Так и есть.",
            "У вас отлично получается.", "Я не сомневалась, что у вас получится.", "Правильно."};
    private String[] soundPraises = {"<speaker audio=\"alice-sounds-game-win-1.opus\">",
            "<speaker audio=\"alice-sounds-game-win-2.opus\">",
            "<speaker audio=\"alice-sounds-game-win-3.opus\">"};//Попытка добавления звуков
    private String[] oneMoreQuestion = {"Хотите ещё задачу?", "Решаем дальше?", "Давайте решим еще одну!", "Предлагаю решить ещё одну"};
    private String[] wrongAnswer = {"Нет, это неверно.", "Неверно.", "Это неправильный ответ.", "Нет.", "Нет, это точно неправильно."};
    private String[] notAnAnswer = {"Хотите скажу подсказку или повторю задачу?", "Я могу повторить задачу.", "Могу дать вам подсказку.",
            "Задача не из простых, но я могу помочь", "Я могу подсказать."};
    private String[] almost = {"Почти.", "Почти верно.", "Близко, но нет."};
    private String[] didNotUnderstand = {"Не поняла вас. ", "Не понимаю. ", "Мне кажется, это не было ответом на мой вопрос. ",
            "Не уверена, что поняла вас правильно. "};
    private String[] wantAProblemQuestion = {"Хотите решить задачу?", "Хотите решить ещё одну задачу?", "Продолжаем решать задачи?"};
    private String[] allHintAreGiven = {"Я уже давала вам подсказку. ", "У меня закончились подсказки, но могу повторить ещё раз. ",
            "Больше подсказок нет, но могу повторить. ", "Подсказка была такая. "};
    private String[] nextHint = {"Следующая подсказка. ", "У меня есть ещё одна подсказка. ",
            "Одну подсказку я вам уже давала, но могу подсказать ещё. "};
    private String meetOnceMore[] = {"Рада снова слышать вас, ", "Здравствуйте ", "Очень приятно снова слышать вас, "};
    private String niceToMeet[] = {"Приятно познакомиться, ", "Очень приятно, ", "Рада знакомству, "};

    public MainServlet() {
        userInfos = DataBaseService.INSTANCE.getUserInfoFromDB();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final DataBaseService dataBaseService = DataBaseService.INSTANCE;

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            StringBuilder sb = new StringBuilder();
            String s;
            request.setCharacterEncoding("UTF-8");
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }

            final JsonParser parser = new JsonParser();
            final JsonObject bodyJson = parser.parse(sb.toString()).getAsJsonObject();
            final String version = bodyJson.get("version").getAsString();
            final String clientId = bodyJson.get("meta").getAsJsonObject().get("client_id").getAsString();
            final JsonObject requestJson = bodyJson.getAsJsonObject("request");
            final String command = requestJson.get("command").getAsString();
            final boolean newSession = bodyJson.get("session").getAsJsonObject().get("new").getAsBoolean();
            final String sessionId = bodyJson.get("session").getAsJsonObject().get("session_id").getAsString();
            final String userId = bodyJson.get("session").getAsJsonObject().get("user_id").getAsString();
            final JsonArray entitiesArray = requestJson.get("nlu").getAsJsonObject().get("entities").getAsJsonArray();

            if (!"ping".equals(command)) {
                System.out.println("Request [" + new Date().toString() + "]: " + sb.toString());
            }

            final JsonObject result = new JsonObject();
            final JsonObject responseJson = new JsonObject();
            result.addProperty("version", version);
            result.add("session", bodyJson.get("session"));

            final Session session;
            if (newSession || !currentSessions.containsKey(sessionId)) {
                if (!userInfos.containsKey(userId)) {
                    session = new Session(sessionId);
                    final ProblemTextBuilder helloText = new ProblemTextBuilder().append("Это закрытый навык. ")
                            .append("Предлагаю вам порешать олимпиадные задачи по математике от ").append("кружка", "кружк+а")
                            .append(" Фрактал. Как я могу вас называть?");
                    session.setLastServerResponse(helloText.getText());
                    responseJson.addProperty("text", helloText.getText());
                    requestJson.addProperty("tts", helloText.getTTS());

                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                } else {
                    final UserInfo userInfo = userInfos.get(userId);
                    session = new Session(userInfo, sessionId);
                    final String helloText = "Это закрытый навык. " + chooseRandomElement(meetOnceMore) + userInfo.getName() + "! ";
                    if (userInfo.getCurrentProblem() != null) {
                        final Problem problem = userInfo.getCurrentProblem();
                        session.setCurrentDifficulty(problem.getDifficulty());
                        session.setCurrentProblem(problem);
                        responseJson.addProperty("text", helloText + problem.getText());
                        session.setLastServerResponse(helloText + problem.getText());
                        addProblemButtons(responseJson, problem);
                        if (problem.getTTS() != null) {
                            responseJson.addProperty("tts", helloText + problem.getTTS());
                        }
                    } else {
                        final ProblemTextBuilder text = new ProblemTextBuilder().append(helloText)
                                .append("Я предлагаю вам решить олимпиадную задачу по математике от ").append("кружка", "кружка+")
                                .append(" Фрактал. Какую хотите: простую, среднюю или сложную?");
                        responseJson.addProperty("text", text.getText());
                        responseJson.addProperty("tts", text.getTTS());
                        session.setLastServerResponse(text.getText());
                        responseJson.add("buttons", createDifficultyButtons(session));
                    }

                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                }
                currentSessions.put(sessionId, session);
            } else {
                session = currentSessions.get(sessionId);

                final SessionResult score = session.getSessionResult();

                if (session.getUserInfo() == null && !(Objects.equals(command, "end session") ||
                        checkAnswer(command, endSessionAnswers))) {
                    if (session.getUserName() == null) {
//                        if (!userNames.contains(command)) {
                        final String text = chooseRandomElement(niceToMeet) + command + "! " +
                                "Я правильно произнесла ваше имя?";
                        responseJson.addProperty("text", text);
                        session.setUserName(command);
                        responseJson.addProperty("text", text);
                        session.setLastServerResponse(text);
//                        } else {
//                            final String text = "К сожалению, уже есть пользователь с таким именем. Придумайте, пожалуйста, другое";
//                            responseJson.addProperty("text", text);
//                        }
                    } else if (checkAnswer(command, yesAnswers)) {
                        final UserInfo userInfo = new UserInfo(userId, session.getUserName(), HashMultimap.create());
                        session.setUserInfo(userInfo);
                        final String text = "Какую задачу будем решать: простую, среднюю или сложную?";
                        userInfos.put(userId, userInfo);
                        responseJson.addProperty("text", text);
                        session.setLastServerResponse(text);
                        responseJson.add("buttons", createDifficultyButtons(session));
                    } else if (checkAnswer(command, noAnswers)) {
                        session.setUserName(null);
                        final String text = "Прошу прощения! Повторите пожалуйста свое имя?";
                        responseJson.addProperty("text", text);
                        session.setLastServerResponse(text);
                    } else {
                        final String text = chooseRandomElement(didNotUnderstand) + " Вас зовут " + session.getUserName() + "?";
                        responseJson.addProperty("text", text);
                        session.setLastServerResponse(text);
                        dataBaseService.updateMiscAnswersTable(command, "", session.getLastServerResponse());
                    }
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                } else if (Objects.equals(command, "end session") ||
                        checkAnswer(command, endSessionAnswers) ||
                        (session.getCurrentProblem() == null && checkAnswer(command, enoughAnswers))) {
                    responseJson.addProperty("text", score.getResult().getText());//итоговое сообщение пользователю
                    responseJson.addProperty("tts", score.getResult().getTTS());//корректное произношение итогов сессии
                    responseJson.addProperty("end_session", true);
                    result.add("response", responseJson);
                } else if (session.getNextProblem() == null || session.getCurrentDifficulty() == null) {
                    final Problem.Difficulty currentDifficulty = parseDifficulty(command);
                    if (currentDifficulty == null) {
                        dataBaseService.updateMiscAnswersTable(command, "", session.getLastServerResponse());

                        final String responseText = chooseRandomElement(didNotUnderstand) + "Выберите пожалуйста сложность задач";
                        session.setLastServerResponse(responseText);
                        responseJson.addProperty("text", responseText);
                        responseJson.add("buttons", createDifficultyButtons(session));
                        responseJson.addProperty("end_session", false);
                        result.add("response", responseJson);
                    } else {
                        session.setCurrentDifficulty(currentDifficulty);
                        addProblemTextToResponse(responseJson, session);
                        result.add("response", responseJson);
                    }
                } else if (session.getCurrentProblem() == null && (checkAnswer(command, yesAnswers) || checkAnswer(command, continueAnswers, yesAnswers))) {
                    addProblemTextToResponse(responseJson, session);
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);

                } else {
                    final Problem problem = session.getCurrentProblem();

                    if (problem == null) {
                        dataBaseService.updateMiscAnswersTable(command, "", session.getLastServerResponse());

                        final String responseText = chooseRandomElement(didNotUnderstand) + chooseRandomElement(wantAProblemQuestion);
                        session.setLastServerResponse(responseText);
                        responseJson.addProperty("text", responseText);
                        result.add("response", responseJson);
                    } else if (checkAnswer(command, askHint, yesAnswers)) {
                        if (!problem.hasHint()) {
                            giveHint(result, responseJson, session, problem, chooseRandomElement(allHintAreGiven), problem.getLastHint());
                        } else {
                            if (problem.wasHintGiven()) {
                                giveHint(result, responseJson, session, problem, chooseRandomElement(nextHint), problem.getNextHint());
                            } else {
                                giveHint(result, responseJson, session, problem, "", problem.getNextHint());
                            }
                        }
                    } else if (checkAnswer(command, askToRepeat, yesAnswers)) {
                        responseJson.addProperty("text", problem.getText());
                        if (problem.getTTS() != null) {
                            responseJson.addProperty("tts", problem.getTTS());
                        }
                        session.setLastServerResponse(problem.getText());
                        result.add("response", responseJson);
                        addProblemButtons(responseJson, problem);
                    } else if (checkAnswer(command, askAnswer, yesAnswers)) {
                        if (problem.hasHint()) {
                            giveHint(result, responseJson, session, problem, "Давайте дам вам подсказку. ", problem.getNextHint());
                        } else {
                            finishWithProblem(result, responseJson, session, problem, Problem.State.ANSWER_GIVEN, clientId);
                        }
                    } else {
                        checkCorrectAnswer(command, entitiesArray, responseJson, session, result, clientId);
                    }

                    responseJson.addProperty("end_session", false);
                }
            }

            PrintWriter out = response.getWriter();
            if (!"ping".equals(command)) {
                System.out.println("Response: " + result.toString());
            }
            out.print(result.toString());
        } catch (Exception ex) {
            // TODO: send error JSON
            ex.printStackTrace();
            response.getOutputStream().print("Error");
            response.getOutputStream().flush();
        }

    }

    private void finishWithProblem(JsonObject result, JsonObject responseJson, Session session, Problem problem, Problem.State problemState, String clientId) {
        problem.setState(problemState);
        final int points = session.updateScore(problem);
        session.setCurrentProblem(null);

        final String prefix = problemState == Problem.State.ANSWER_GIVEN ? "Правильный ответ " + problem.getTextAnswer() + ". "
                : chooseRandomElement(praises) + " ";

        final String responseText = prefix +
                (session.getNextProblem() != null ? chooseRandomElement(oneMoreQuestion) : "Поздравляю! Вы решили все задачи на этом уровне сложности. Порешаем другие задачи?");
        if (session.getNextProblem() == null) {
            responseJson.add("buttons", createDifficultyButtons(session));
        }
        responseJson.addProperty("text", responseText);
        if (problemState != Problem.State.ANSWER_GIVEN) {
            responseJson.addProperty("tts", chooseRandomElement(soundPraises) + " " + responseJson.get("text"));
        }
        session.setLastServerResponse(responseText);
        result.add("response", responseJson);

        DataBaseService.INSTANCE.insertSessionInfo(session.getUserInfo().getDeviceId(), clientId, problem.getText(),
                problem.getDifficulty().toString(), session.getUserInfo().getName(), problem.getProblemScenario().getScenarioId(),
                problem.getTheme(), points, problem.wasHintGiven());
    }

    private void giveHint(JsonObject result, JsonObject responseJson, Session session, Problem problem, String prefix, String hint) {
        responseJson.addProperty("text", prefix + hint);
        session.setLastServerResponse(prefix + hint);
        result.add("response", responseJson);
        addProblemButtons(responseJson, problem);
    }

    private void checkCorrectAnswer(String command, JsonArray entitiesArray, JsonObject responseJson, Session session, JsonObject result, String clientId) {
        final Problem problem = session.getCurrentProblem();

        boolean correctAnswer = problem.checkAnswer(command);
        boolean almostCorrect = false;

        if (!correctAnswer && problem.isNumericAnswer()) {
            boolean numberFound = false;

            for (JsonElement jsonElement : entitiesArray) {
                final String type = jsonElement.getAsJsonObject().get("type").getAsString();
                if (type.equalsIgnoreCase("YANDEX.NUMBER")) {
                    final int value = jsonElement.getAsJsonObject().get("value").getAsInt();
                    if (problem.getNumericAnswer() == value) {
                        correctAnswer = true;
                    }
                    if (Math.abs(value - problem.getNumericAnswer()) <= Math.ceil(problem.getNumericAnswer() * 0.05)) {
                        almostCorrect = true;
                    }
                    numberFound = true;
                }
            }
            if (!numberFound) {
                responseJson.addProperty("text", chooseRandomElement(notAnAnswer));
            }
        }

        if (correctAnswer) {
            finishWithProblem(result, responseJson, session, problem, problem.wasHintGiven() ? Problem.State.SOLVED_WITH_HINT : Problem.State.SOLVED, clientId);
        } else {
            final String text = chooseRandomElement(almostCorrect ? almost : wrongAnswer) + " " + chooseRandomElement(notAnAnswer);
            responseJson.addProperty("text", text);
            addProblemButtons(responseJson, problem);
            result.add("response", responseJson);
            session.setLastServerResponse(text);
            // score.getProblemAnswerGiven();
        }
    }

    private String chooseRandomElement(String[] array) {
        return array[randomInt(0, array.length)];
    }

    private void addProblemTextToResponse(JsonObject responseJson, Session session) {
        final Problem problem = session.getNextProblem();
        session.setCurrentProblem(problem);
        if (problem == null) {
            final String text = "Извините, но вы уже решили все мои задачи на этом уровне сложности. Может быть, порешаем задачи другой сложности?";
            responseJson.addProperty("text", text);
            responseJson.add("buttons", createDifficultyButtons(session));
            session.setLastServerResponse(text);
        } else {
            final String text = (problem.getComment() != null ? (problem.getComment() + " ") : "") + problem.getText();
            responseJson.addProperty("text", text);
            addProblemButtons(responseJson, problem);
            if (problem.getTTS() != null) {
                responseJson.addProperty("tts", (problem.getComment() != null ? (problem.getComment() + " ") : "") + problem.getTTS());
            }
            session.setLastServerResponse(text);
        }
    }

    private void addProblemButtons(JsonObject responseJson, Problem problem) {
        final JsonArray buttons = new JsonArray();
        buttons.add(createButton("Повторить"));
        buttons.add(createButton("Подсказка"));
        if (!problem.hasHint()) {
            buttons.add(createButton("Сказать ответ"));
        }
        responseJson.add("buttons", buttons);
    }

    private Problem.Difficulty parseDifficulty(String command) {
        if (checkAnswer(command, Sets.newHashSet("простая", "простую"), yesAnswers)) {
            return Problem.Difficulty.EASY;
        } else if (checkAnswer(command, Sets.newHashSet("средняя", "среднюю"), yesAnswers)) {
            return Problem.Difficulty.MEDIUM;
        } else if (checkAnswer(command, Sets.newHashSet("сложная", "сложную"), yesAnswers)) {
            return Problem.Difficulty.HARD;
        } else if (checkAnswer(command, Sets.newHashSet("эксперт", "экспертная"), yesAnswers)) {
            return Problem.Difficulty.EXPERT;
        } else {
            return null;
        }
    }

    private JsonArray createDifficultyButtons(Session session) {
        final JsonArray buttons = new JsonArray();
        final Problem.Difficulty currentDifficulty = session.getCurrentDifficulty();
        if (currentDifficulty != Problem.Difficulty.EASY) {
            buttons.add(createButton("простая"));
        }
        if (currentDifficulty != Problem.Difficulty.MEDIUM) {
            buttons.add(createButton("средняя"));
        }
        if (currentDifficulty != Problem.Difficulty.HARD) {
            buttons.add(createButton("сложная"));
        }
        if (currentDifficulty != Problem.Difficulty.EXPERT) {
            buttons.add(createButton("эксперт"));
        }
        if (currentDifficulty != null) {
            buttons.add(createButton("закончить"));
        }
        return buttons;
    }

    private JsonObject createButton(String title) {
        final JsonObject buttonJson = new JsonObject();
        buttonJson.addProperty("title", title);
        buttonJson.addProperty("hide", true);
        return buttonJson;
    }

    private boolean checkAnswer(String answer, Set<String> answerCollection) {
        return checkAnswer(answer, answerCollection, answerCollection);
    }

    private boolean checkAnswer(String answer, Set<String> answerCollection, Set<String> preAnswerCollection) {
        if (answer == null) {
            return false;
        }
        answer = answer.trim();
        StringBuilder answerMod = new StringBuilder().append(answer.charAt(0));
        for (int i = 1; i < answer.length(); i++) {
            if (answer.charAt(i - 1) == ' ' && answer.charAt(i) == ' ') {
                continue;
            }
            answerMod.append(answer.charAt(i));
        }
        answer = answerMod.toString();
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


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
