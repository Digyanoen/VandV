package Testing;

import org.junit.runner.notification.Failure;
import processors.Mutant;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.List;

public class Result {
    private static int total=0;
    private static int killed = 0;


    public static void showResults(Mutant m) {
        System.out.println("Nom du mutant : "+m.getMutantName());
        System.out.println("Mutation effectuée dans la méthode : "+m.getMethod().getSimpleName());
        System.out.println("Ligne de la modification : "+m.getLine());
        System.out.println("Modification effectué :"+m.getStatement());

        List<Failure> failureList = TestUnitHandler.getFailures();
        if (failureList.size() == 0) {
            System.out.println("Le mutant" +m.getMutantName() +" n'a pas été tué");
        } else {
            System.out.println("Le mutant "+m.getMutantName()+" a été tué par les tests suivant :");
            killed+=1;
            for (Failure f : failureList) {
                System.out.println("    " + f.getDescription().getMethodName());

            }
        }

        total+=1;
        System.out.println("Nombre de mutant : "+total);
        System.out.println("Nombre de mutant tués :"+killed);
        System.out.println("Ratio : "+ (killed/total));
    }
}
