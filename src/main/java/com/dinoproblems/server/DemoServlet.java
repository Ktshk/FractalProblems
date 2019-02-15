package com.dinoproblems.server;

import com.google.common.collect.Sets;
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

/**
 * Created by Katushka on 07.01.2019.
 */
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {

    private Map<String, Session> currentProblems = new HashMap<>();
    private Set<String> yesAnswers = Sets.newHashSet("да", "давай", "хочу", "валяй", "можно", "ага", "угу");
    private Set<String> noAnswers = Sets.newHashSet("нет", "не хочу", "хватит", "не надо");
    private Set<String> endSessionAnswers = Sets.newHashSet("хватит", "больше не хочу", "давай закончим", "надоело");

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

            final JsonObject result = new JsonObject();
            final JsonObject responseJson = new JsonObject();
            result.addProperty("version", version);
            result.add("session", bodyJson.get("session"));

            final Session session = currentProblems.computeIfAbsent(sessionId, Session::new);

            if (newSession) {
                responseJson.addProperty("text", "У меня есть для вас интересная задачка. Хотите попробовать решить?");
                responseJson.addProperty("end_session", false);
                result.add("response", responseJson);
            } else if (Objects.equals(command, "end session") ||
                    checkAnswer(command, endSessionAnswers) ||
                    (session.getCurrentProblem() == null && checkAnswer(command, noAnswers))) {
                responseJson.addProperty("text", "Заходите еще");
                responseJson.addProperty("end_session", true);
                result.add("response", responseJson);
            } else if (session.getCurrentProblem() == null && checkAnswer(command, yesAnswers)) {
                final Problem problem = ProblemCollection.INSTANCE.generateProblem(session, Problem.Difficulty.EASY);
                session.setCurrentProblem(problem);
                responseJson.addProperty("text", problem.getText());
                responseJson.addProperty("end_session", false);
                result.add("response", responseJson);
            } else {
                final Problem problem = session.getCurrentProblem();

                if (problem == null) {
                    responseJson.addProperty("text", "Не поняла вас. Хотите задачу?");
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                } else if (problem.getState() == Problem.State.ANSWER_PROPOSED && checkAnswer(command, noAnswers)) {
                    responseJson.addProperty("text", "Хорошо, подумайте еще");
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                    problem.setState(Problem.State.ANSWER_PROPOSED);
                } else if (problem.getState() == Problem.State.ANSWER_PROPOSED &&
                        (checkAnswer(command, yesAnswers) || "скажи ответ".equalsIgnoreCase("command"))) {
                    responseJson.addProperty("text", "Правильный ответ " + problem.getTextAnswer() + ". Ещё задачу?");
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                    problem.setState(Problem.State.ANSWER_GIVEN);
                    session.setCurrentProblem(null);
                } else if (problem.checkAnswer(command)) {
                    responseJson.addProperty("text", "Это правильный ответ. Еще задачку?");
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                    problem.setState(Problem.State.SOLVED);
                    session.setCurrentProblem(null);
                } else {
                    responseJson.addProperty("text", "Это неправильный ответ. Сдаетесь?");
                    responseJson.addProperty("end_session", false);
                    result.add("response", responseJson);
                    problem.setState(Problem.State.ANSWER_PROPOSED);
                }
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

    private boolean checkAnswer(String answer, Set<String> answerCollection) {
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
        for (String yesAnswer1 : answerCollection) {
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
