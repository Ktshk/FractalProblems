import com.dinoproblems.server.Problem;
import com.dinoproblems.server.generators.SequenceGenerator;
import org.junit.Test;

import java.util.Set;

/**
 * Created by Katushka on 13.03.2019.
 */
public class TestProblemGenerator {

    public static final SequenceGenerator GENERATOR = new SequenceGenerator();

    @Test
    public void testGenerateProblem() {
        final Set<Problem.Difficulty> availableDifficulties = GENERATOR.getAvailableDifficulties();
        for (Problem.Difficulty availableDifficulty : availableDifficulties) {
            System.out.println("************* " + availableDifficulty);
            for (int i = 0; i < 10; i++) {
                final Problem problem = GENERATOR.generateProblem(availableDifficulty);
                System.out.println("Problem " + (i + 1) + ": " + problem.getText());
                System.out.println("Answer: " + problem.getTextAnswer());
                System.out.println("*** ");
            }
        }

    }
}
