package com.dinoproblems.server.session;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;

/**
 * Created by Katushka on 04.06.2019.
 */
public interface SessionState {
    @Nonnull
    SessionState getNextState(String command, JsonArray entitiesArray, Session session);

    void processRequest(String command, JsonObject request, Session session);
}
