package com.dinoproblems.server.session;

import com.dinoproblems.server.UserInfo;
import com.dinoproblems.server.utils.ProblemTextBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * Created by Katushka on 04.06.2019.
 */
public class RegistrationSessionState implements SessionState {
    private static final String MY_NAME_IS[] = {"меня зовут", "мое имя", "моё имя", "зови меня", "можешь называть меня",
            "называй меня", "можешь звать меня"};

    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session) {
        String userNameFromCommand = getUserNameFromCommand(command);
        if (userNameFromCommand == null) {
            userNameFromCommand = command;
        }
        session.setUserInfo(new UserInfo(session.getUserId(), userNameFromCommand));

        return new MenuSessionState(false, true);
    }

    @Override
    public void processRequest(String command, JsonObject responseJson, Session session) {
        final ProblemTextBuilder helloText = new ProblemTextBuilder()
                .append("Предлагаю вам порешать олимпиадные задачи по математике от ").append("кружка", "кружк+а")
                .append(" Фрактал. Как я могу вас называть?");

        responseJson.addProperty("text", helloText.getText());
        responseJson.addProperty("tts", helloText.getTTS());
    }

    private String getUserNameFromCommand(String command) {
        for (String s : MY_NAME_IS) {
            final int ind = command.indexOf(s);
            if (ind >= 0) {
                return command.substring(ind + s.length()).trim();
            }
        }
        return null;
    }
}
