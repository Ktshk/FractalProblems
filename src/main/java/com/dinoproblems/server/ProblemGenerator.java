package com.dinoproblems.server;

import com.dinoproblems.server.Problem.Difficulty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.dinoproblems.server.utils.GeneratorUtils.chooseRandomElement;

/**
 * Created by Katushka on 10.02.2019.
 */
public interface ProblemGenerator {
    @Nonnull
    Problem generateProblem(Difficulty difficulty, ProblemAvailability problemAvailability);

    @Nullable
    ProblemAvailability hasProblem(@Nonnull Collection<Problem> alreadySolvedProblems, @Nonnull Difficulty difficulty);

    enum ProblemAvailabilityType {
        newProblem(4), newForThisDifficulty(3), newScenarioProblem(1), minorScenarioChanges(0), trainProblem(1), easierProblem(2);

        private final int weight;

        ProblemAvailabilityType(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }
    }

    class ProblemAvailability {
        private final ProblemAvailabilityType type;
        private final ProblemScenario scenario;

        public ProblemAvailability(ProblemAvailabilityType type, ProblemScenario scenario) {
            this.type = type;
            this.scenario = scenario;
        }

        public ProblemAvailabilityType getType() {
            return type;
        }

        public ProblemScenario getScenario() {
            return scenario;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProblemAvailability that = (ProblemAvailability) o;

            if (type != that.type) return false;
            return scenario != null ? scenario.equals(that.scenario) : that.scenario == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (scenario != null ? scenario.hashCode() : 0);
            return result;
        }
    }

}
