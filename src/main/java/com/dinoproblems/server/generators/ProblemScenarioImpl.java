package com.dinoproblems.server.generators;

import com.dinoproblems.server.ProblemScenario;

/**
 * Created by Katushka on 08.04.2019.
 */
public class ProblemScenarioImpl implements ProblemScenario {

    private final String id;
    private final boolean singleProblem;

    public ProblemScenarioImpl(String id) {
        this.id = id;
        singleProblem = false;
    }

    public ProblemScenarioImpl(String id, boolean isSingleProblem) {
        this.id = id;
        singleProblem = isSingleProblem;
    }

    @Override
    public String getScenarioId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProblemScenarioImpl that = (ProblemScenarioImpl) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSingleProblem() {
        return singleProblem;
    }
}
