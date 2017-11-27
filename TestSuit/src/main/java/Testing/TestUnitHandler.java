package Testing;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.SpoonModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestUnitHandler {

    private static SpoonModelBuilder compiler;
    private static JUnitCore junit;

    public static List<Failure> getFailures(){
        compiler.compile();
        return new ArrayList<>();
    }

    public static void initialize(SpoonModelBuilder cp){
        compiler = cp;
        //Initialise JUnit pour l'exécution des tests
        junit = new JUnitCore();
    }
}
