import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import processors.MethodChangeOperatorProcessor;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.*;
import java.util.List;

import static javafx.application.Platform.exit;
import static org.junit.Assert.assertTrue;

public class Main {

    public static void main(String[] args){

        //Vérifie si nous avons les arguments nécessaire
        if(args.length < 1){
            throw new IllegalArgumentException("Expected parameters : <source folder>");
        }

        //Initialise le launcher principal
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        //Initialise le launcher des tests
        Launcher testLauncher = new Launcher();
        testLauncher.getEnvironment().setAutoImports(true);
        testLauncher.getEnvironment().setNoClasspath(true);

        //Récupère les dossiers de sources et de tests
        File inDir = new File(args[0]+"/src/main/java");
        File inTestDir = new File(args[0]+"/src/test/java");

        //Ajoute les sources aux launchers et build les models
        launcher.addInputResource(inDir.getPath());
        launcher.buildModel();

        testLauncher.addInputResource(inTestDir.getPath());
        testLauncher.buildModel();

        //Récupère la liste des classes de test
        List<CtType> testtypes = testLauncher.getModel().getElements(new TypeFilter<CtType>(CtType.class));
        //Récupère le package source
        CtPackage rootp = launcher.getModel().getRootPackage();
        //Ajoute chaque classe dans le launcher principal
        testtypes.forEach(ctType -> rootp.addType(ctType));

        //Récupère la factory
        Factory factory = launcher.getFactory();
        //Crée un compiler spoon et compile
        SpoonModelBuilder compiler = launcher.createCompiler(factory);
        compiler.compile();



        CtModel root = launcher.getModel();
 //list all classes of the model
        for(CtType<?> s : root.getAllTypes()) {
            System.out.println("class: "+s.getQualifiedName());
        }
        MethodChangeOperatorProcessor classProc = new MethodChangeOperatorProcessor();
        root.getAllTypes().stream().forEach(m -> {
                    classProc.process((CtClass) m);
                    System.out.println(m);
                }
        );



        System.out.println("Hello World!");
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

//    @Test
//    public void testGoodTestClassNames() throws Exception {
//        SpoonAPI spoon = new Launcher();
//        spoon.addInputResource("src/test/java/");
//        spoon.buildModel();
//
//        for (CtMethod<?> meth : spoon.getModel().getRootPackage().getElements(new TypeFilter<CtMethod>(CtMethod.class) {
//            @Override
//            public boolean matches(CtMethod element) {
//                return super.matches(element) && element.getAnnotation(Test.class) != null;
//            }
//        })) {
//            assertTrue("naming contract violated for "+meth.getParent(CtClass.class).getSimpleName(), meth.getParent(CtClass.class).getSimpleName().startsWith("Test") || meth.getParent(CtClass.class).getSimpleName().endsWith("Test"));
//        }
//    }


}
