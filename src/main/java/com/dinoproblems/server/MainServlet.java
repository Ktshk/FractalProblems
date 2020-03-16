package com.dinoproblems.server;

import com.dinoproblems.server.session.Session;
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


/**
 * Created by Katushka
 * on 07.01.2019.
 */
@WebServlet("/DemoServlet")
public class MainServlet extends HttpServlet {

    private Map<String, Session> currentSessions = new HashMap<>();
    private Map<String, UserInfo> userInfos = new HashMap<>();
    public static String URL = "test-env.ha4x2kktxp.us-east-2.elasticbeanstalk.com";


    public MainServlet() {
        userInfos = DataBaseService.INSTANCE.getUserInfoFromDB();
        if (System.getProperty("URL") != null) {
            URL = System.getProperty("URL");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            final String command;
            if (requestJson.get("command") != null) {
                command = requestJson.get("command").getAsString();
            } else {
                if (requestJson.get("type") != null && requestJson.get("type").getAsString().equalsIgnoreCase("ButtonPressed")) {
                    final JsonElement payload = requestJson.get("payload");
                    if (payload != null) {
                        command = payload.getAsJsonObject().get("command").getAsString();
                    } else {
                        command = "";
                    }
                } else {
                    command = "";
                }
            }
            final String timeZone = bodyJson.get("meta").getAsJsonObject().get("timezone").getAsString();
            final boolean newSession = bodyJson.get("session").getAsJsonObject().get("new").getAsBoolean();
            final String sessionId = bodyJson.get("session").getAsJsonObject().get("session_id").getAsString();
            final String userId = bodyJson.get("session").getAsJsonObject().get("user_id").getAsString();
            final JsonArray entitiesArray = requestJson.get("nlu").getAsJsonObject().get("entities").getAsJsonArray();

            if (!"ping".equals(command)) {
                System.out.println("Request [" + new Date().toString() + "]: " + sb.toString());
            }

            final JsonObject result = new JsonObject();
            result.addProperty("version", version);
            result.add("session", bodyJson.get("session"));

            System.out.println("userInfos = " + userInfos);
            System.out.println("userId = " + userId);

            if (userInfos.containsKey(userId)) {
                final UserInfo userInfo = userInfos.get(userId);
                if (userInfo.getCurrentProblem() != null) {
                    System.out.println("currentProblem.hasHint() = " + userInfo.getCurrentProblem().hasHint());
                    System.out.println("currentProblem.wasHintGiven() = " + userInfo.getCurrentProblem().wasHintGiven());
                    System.out.println("currentProblem.hints = " + ((ProblemWithPossibleTextAnswers) userInfo.getCurrentProblem()).hints);
                }
            }

            final Session session;

//            if (userInfo.getCurrentProblem() != null) {
//                final Problem problem = userInfo.getCurrentProblem();
//                session.setCurrentDifficulty(problem.getDifficulty());
//                session.setCurrentProblem(problem);
//                responseJson.addProperty("text", helloText + problem.getText());
//                session.setLastServerResponse(helloText + problem.getText());
//                addProblemButtons(responseJson, problem);
//                if (problem.getTTS() != null) {
//                    responseJson.addProperty("tts", helloText + problem.getTTS());
//                }
//            }

            if (newSession || !currentSessions.containsKey(sessionId)) {
                if (!userInfos.containsKey(userId)) {
                    session = new Session(sessionId, clientId, userId);
                } else {
                    final UserInfo userInfo = userInfos.get(userId);
                    session = new Session(userInfo, sessionId, clientId);
                }
                currentSessions.put(sessionId, session);
            } else {
                session = currentSessions.get(sessionId);
            }
            session.processRequest(command, result, entitiesArray, timeZone);

            if (session.getUserInfo() != null && !userInfos.containsKey(userId)) {
                userInfos.put(userId, session.getUserInfo());
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

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
