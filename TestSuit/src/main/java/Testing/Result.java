package Testing;

import org.junit.runner.notification.Failure;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.List;

public class Result {


    public static void showResults(CtMethod m) {

        List<Failure> failureList = TestUnitHandler.getFailures();
        if (failureList.size() == 0) {
            System.out.println("Le mutant n'a pas été tué");
            System.out.println("Mutation effectuée dans la méthode : "+m.getSimpleName());
        } else {
            System.out.println("Mutant tué par les tests suivant :");
            for (Failure f : failureList) {
                System.out.println("    " + f.getDescription().getMethodName());

            }
        }
    }
}
