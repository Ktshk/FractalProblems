package com.dinoproblems.server;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.dinoproblems.server.utils.GeneratorUtils.randomInt;

/**
 * Created by Katushka on 07.01.2019.
 */
@WebServlet("/DemoServlet")
public class MainServlet extends HttpServlet {

    private Map<String, Session> currentProblems = new HashMap<>();
    private Set<String> yesAnswers = Sets.newHashSet("да", "давай", "ну давай", "хочу", "валяй", "можно", "ага", "угу");
    private Set<String> noAnswers = Sets.newHashSet("нет", "не хочу", "хватит", "не надо");
    private Set<String> endSessionAnswers = Sets.newHashSet("хватит", "больше не хочу", "давай закончим", "надоело");
    private Set<String> askAnswer = Sets.newHashSet("ответ", "сдаюсь", "сказать ответ", "скажи ответ");
    private Set<String> askHint = Sets.newHashSet("подсказка", "подсказку", "сказать подсказку", "дать подсказку", "дай", "дай подсказку");
    private Set<String> askToRepeat = Sets.newHashSet("повтори", "повторить", "повтори задачу", "повтори условие");
    private String[] praises = {"Молодец!", "Это правильный ответ.", "Ну конечно! Так и есть.",
            "У вас отлично получается решать задачи.", "Я не сомневалась, что у вас получится.", "Правильно."};
    private String[] oneMoreQuestion = {"Хотите ещё задачу?", "Ещё задачу?", "Решаем дальше?",
            "Ещё одну?", "Давайте решим еще одну?"};
    private String[] wrongAnswer = {"Нет, это неверно.", "Неверно.", "Это неправильный ответ.", "Нет.", "Нет, это точно неправильно."};
    private String[] notAnAnswer = {"Хотите скажу подсказку или повторю задачу?", "Я могу повторить задачу.", "Могу дать вам подсказку.",
            "Задача не из простых, но я могу помочь", "Я могу подсказать."};
    private String[] almost = {"Почти.", "Почти верно.", "Близко, но нет."};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            StringBuilder sb = new StringBuilder();
            String s;
            System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());
            request.setCharacterEncoding("UTF-8");
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }

            System.out.println("Request: " + sb.toString());

            final JsonParser parser = new JsonParser();
            final JsonObject bodyJson = parser.parse(sb.toString()).getAsJsonObject();
            final String version = bodyJson.get("version").getAsString();
            final JsonObject requestJson = bodyJson.getAsJsonObject("request");
            final String command = requestJson.get("command").getAsString();
            final boolean newSession = bodyJson.get("session").getAsJsonObject().get("new").getAsBoolean();
            final String sessionId = bodyJson.get("session").getAsJsonObject().get("session_id").getAsString();
            final JsonArray entitiesArray = requestJson.get("nlu").getAsJsonObject().get("entities").getAsJsonArray();

            final JsonObject result = new JsonObject();
            final JsonObject responseJson = new JsonObject();
            result.addProperty("version", version);
            result.add("session", bodyJson.get("session"));

            final Session session = currentProblems.computeIfAbsent(sessionId, Session::new);

            if (newSession) {
                responseJson.addProperty("text", "Это закрытый навык. Я предлагаю вам решить логическую задачу. Какую хотите: простую, среднюю или сложную?");
                responseJson.add("buttons", createDifficultyButtons());

                responseJson.addProperty("end_session", false);
                result.add("response", responseJson);
            } else if (Objects.equals(command, "end session") ||
                    checkAnswer(command, endSessionAnswers) ||
                    (session.getCurrentProblem() == null && checkAnswer(command, noAnswers))) {
                responseJson.addProperty("text", "Заходите еще");
                responseJson.addProperty("end_session", true);
                result.add("response", responseJson);
            } else if (session.getCurrentProblem() == null && session.getCurrentDifficulty() == null) {
                final Problem.Difficulty currentDifficulty = parseDifficulty(command);
                if (currentDifficulty == null) {
                    responseJson.addProperty("text", "Не поняла вас. Выберите пожалуйста сложность задач");
                    responseJson.add("buttons", createDifficultyButtons());
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                } else {
                    session.setCurrentDifficulty(currentDifficulty);
                    addProblemTextToResponse(responseJson, session);
                    result.add("response", responseJson);
                }
            } else if (session.getCurrentProblem() == null && checkAnswer(command, yesAnswers)) {
                addProblemTextToResponse(responseJson, session);
                responseJson.addProperty("end_session", false);
                result.add("response", responseJson);

            } else {
                final Problem problem = session.getCurrentProblem();

                if (problem == null) {
                    responseJson.addProperty("text", "Не поняла вас. Хотите решить задачу?");
                    result.add("response", responseJson);
                } else if (checkAnswer(command, askHint, yesAnswers)) {
                    if (problem.getState() == Problem.State.HINT_GIVEN) {
                        responseJson.addProperty("text", "Я уже давала вам подсказку. " + problem.getHint());
                    } else {
                        responseJson.addProperty("text", problem.getHint());
                    }
                    result.add("response", responseJson);
                    problem.setState(Problem.State.HINT_GIVEN);
                    addProblemButtons(responseJson);
                } else if (checkAnswer(command, askToRepeat, yesAnswers)) {
                    responseJson.addProperty("text", problem.getText());
                    result.add("response", responseJson);
                    addProblemButtons(responseJson);
                } else if (checkAnswer(command, askAnswer, yesAnswers)) {
                    responseJson.addProperty("text", "Правильный ответ " + problem.getTextAnswer() + ". Хотите еще задачу?");
                    result.add("response", responseJson);
                    problem.setState(Problem.State.ANSWER_GIVEN);
                    session.setCurrentProblem(null);
                } else {
                    checkCorrectAnswer(command, entitiesArray, responseJson, session);
                    result.add("response", responseJson);
                }

                responseJson.addProperty("end_session", false);
            }

            PrintWriter out = response.getWriter();
            System.out.println("Response: " + result.toString());
            out.print(result.toString());
        } catch (Exception ex) {
            // TODO: send error JSON
            ex.printStackTrace();
            response.getOutputStream().print("Error");
            response.getOutputStream().flush();
        }

    }

    private void checkCorrectAnswer(String command, JsonArray entitiesArray, JsonObject responseJson, Session session) {
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
                    if (Math.abs(value - problem.getNumericAnswer()) <= problem.getNumericAnswer() + 1) {
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
            responseJson.addProperty("text", chooseRandomElement(praises) + " " + chooseRandomElement(oneMoreQuestion));
            problem.setState(Problem.State.SOLVED);
            session.setCurrentProblem(null);
        } else {
            responseJson.addProperty("text", chooseRandomElement(almostCorrect ? almost : wrongAnswer) + " " + chooseRandomElement(notAnAnswer));
            addProblemButtons(responseJson);
        }
    }

    private String chooseRandomElement(String[] array) {
        return array[randomInt(0, array.length)];
    }

    private void addProblemTextToResponse(JsonObject responseJson, Session session) {
        final Problem problem = ProblemCollection.INSTANCE.generateProblem(session);
        session.setCurrentProblem(problem);
        responseJson.addProperty("text", problem.getText());
        addProblemButtons(responseJson);
        if (problem.getTTS() != null) {
            responseJson.addProperty("tts", problem.getTTS());
        }
    }

    private void addProblemButtons(JsonObject responseJson) {
        final JsonArray buttons = new JsonArray();
        buttons.add(createButton("Повторить"));
        buttons.add(createButton("Подсказка"));
        buttons.add(createButton("Сказать ответ"));
        responseJson.add("buttons", buttons);
    }

    private Problem.Difficulty parseDifficulty(String command) {
        if (checkAnswer(command, Sets.newHashSet("простая", "простую"), yesAnswers)) {
            return Problem.Difficulty.EASY;
        } else if (checkAnswer(command, Sets.newHashSet("средняя", "среднюю"), yesAnswers)) {
            return Problem.Difficulty.MEDIUM;
        } else if (checkAnswer(command, Sets.newHashSet("сложная", "сложную"), yesAnswers)) {
            return Problem.Difficulty.HARD;
        } else {
            return null;
        }
    }

    private JsonArray createDifficultyButtons() {
        final JsonArray buttons = new JsonArray();
        buttons.add(createButton("простая"));
        buttons.add(createButton("средняя"));
        buttons.add(createButton("сложная"));
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
