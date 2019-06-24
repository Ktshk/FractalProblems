package com.dinoproblems.server.session;

import com.dinoproblems.server.UserInfo;
import com.dinoproblems.server.utils.TextWithTTSBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

import java.util.Set;

import static com.dinoproblems.server.utils.GeneratorUtils.upperCaseFirstLetter;

/**
 * Created by Katushka on 04.06.2019.
 */
public class RegistrationSessionState implements SessionState {
    private static final String[] MY_NAME_IS = {"меня зовут", "мое имя", "моё имя", "зови меня", "можешь называть меня",
            "называй меня", "можешь звать меня"};
    static final Set<String> CHANGE_NAME = Sets.newHashSet("поменять имя", "поменяй имя", "изменить имя", "измени имя");

    private final SessionState nextState;

    public RegistrationSessionState(SessionState nextState) {
        this.nextState = nextState;
    }

    public RegistrationSessionState() {
        this(null);
    }

    @Nonnull
    @Override
    public SessionState getNextState(String command, JsonArray entitiesArray, Session session, String timeZone) {
        String userNameFromCommand = getUserNameFromCommand(command);
        if (userNameFromCommand == null) {
            userNameFromCommand = command;
        }
        session.setUserInfo(new UserInfo(session.getUserId(), userNameFromCommand, session.getClientId()));

        if (nextState == null) {
            return new MenuSessionState(new TextWithTTSBuilder()
                    .append("Добро пожаловать, ")
                    .append(upperCaseFirstLetter(userNameFromCommand))
                    .append("! Вы всегда можете поменять имя, сказав команду: поменять имя. "));
        } else {
            return nextState.addTextPrefix("Очень приятно, " + upperCaseFirstLetter(userNameFromCommand) + "! Вы всегда можете поменять имя, сказав команду: поменять имя. ");
        }
    }

    @Nonnull
    @Override
    public SessionState addTextPrefix(String text) {
        return this;
    }

    @Override
    public void processRequest(JsonObject responseJson, Session session, String timeZone) {
        final TextWithTTSBuilder helloText = new TextWithTTSBuilder();
        if (nextState == null) {
            helloText.append("Предлагаю вам порешать олимпиадные задачи по математике от ")
                    .append("кружка", "кружк+а")
                    .append(" Фрактал. ");
        }
        helloText.append("Как я могу вас называть?");

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
