package com.dinoproblems.server.session;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * Created by Katushka on 04.06.2019.
 */
public interface SessionState {
    @Nonnull
    SessionState getNextState(String command, JsonArray entitiesArray, Session session, String timeZone);

    void processRequest(JsonObject request, Session session, String timeZone);

    @Nonnull
    SessionState addTextPrefix(String text);
}
