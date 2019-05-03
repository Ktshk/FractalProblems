package com.dinoproblems.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by Katushka on 02.05.2019.
 */
public class UserInfo {
    private String deviceId;
    private String name;
    private Multimap<String, Problem> solvedProblemsByTheme = HashMultimap.create();

    public UserInfo(String deviceId, String name, Multimap<String, Problem> solvedProblemsByTheme) {
        this.deviceId = deviceId;
        this.name = name;
        this.solvedProblemsByTheme = solvedProblemsByTheme;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Multimap<String, Problem> getSolvedProblemsByTheme() {
        return solvedProblemsByTheme;
    }
}
