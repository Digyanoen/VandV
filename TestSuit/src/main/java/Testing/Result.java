package Testing;

import org.junit.runner.notification.Failure;

import java.util.List;

public class Result {


    public static void showResults() {

        List<Failure> failureList = TestUnitHandler.getFailures();
        if (failureList.size() == 0) {
            System.out.println("Le mutant n'a pas été tué");
        } else {
            System.out.println("Mutant tué par les tests suivant :");
            for (Failure f : failureList) {
                System.out.println(f.getDescription().getMethodName());

            }
        }
    }
}
