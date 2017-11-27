import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.SpoonModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestUnitHandler {

    private static SpoonModelBuilder compiler;

    public static List<Failure> getFailures(){
        return new ArrayList<>();
    }

    public static void initialize(SpoonModelBuilder cp){
        compiler = cp;
        compiler.compile();
        //Initialise JUnit pour l'exécution des tests
        JUnitCore junit = new JUnitCore();
    }
}
