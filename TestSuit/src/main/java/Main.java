import Testing.TestUnitHandler;
import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.junit.Test;
import org.junit.runner.notification.Failure;
import processors.MethodBooleanProcessor;
import processors.MethodChangeIfOperatorProcessor;
import processors.MethodChangeOperatorProcessor;
import processors.MethodVoidProcessor;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class Main {

    private static Launcher launcher;

    public static void main(String[] args){

        //Vérifie si nous avons les arguments nécessaire
        if(args.length < 1){
            throw new IllegalArgumentException("Expected parameters : <source folder>");
        }

        //Récupère les dossiers de sources et de tests
        File inDir = new File(args[0]);

        //Initialise le Launcher
        initLauncher(inDir);

        //Retire les tests du projet qui sont ignorés ou qui échouent
        try {
            TestUnitHandler.removeJunkTest();
        } catch (CompilerException e) {
            e.printStackTrace();
        }

        List<CtType> tests = TestUnitHandler.getTests();


        CtModel root = launcher.getModel();
//
//        List<CtMethod> meth = root.getElements(new TypeFilter<CtMethod>(CtMethod.class));
//
//        //list all classes of the model
//        for(CtMethod m : meth) {
//            System.out.println("method: "+m.getSimpleName());
//        }
//
        List<CtClass> clazzes = root.getElements(new TypeFilter<CtClass>(CtClass.class){
            @Override
            public boolean matches(CtClass element) {
                return super.matches(element) && !tests.contains(element);
            }
        });
//
//        //list all classes of the model
//        for(CtClass c : clazzes) {
//            System.out.println("class: "+c.getQualifiedName());
//        }

        // Launch a mutator
        MethodChangeOperatorProcessor classProc = new MethodChangeOperatorProcessor();
        clazzes.stream().forEach(m -> {
                    classProc.process(m);
                }


        );
        // Launch a mutator
        MethodChangeIfOperatorProcessor classProc2 = new MethodChangeIfOperatorProcessor();
        clazzes.stream().forEach(m -> {
                    classProc2.process(m);
                }


        );
        MethodBooleanProcessor classProc3 = new MethodBooleanProcessor();
        clazzes.stream().forEach(m -> {
                    classProc3.process(m);
                }


        );
        MethodVoidProcessor classProc4 = new MethodVoidProcessor();
        clazzes.stream().forEach(m -> {
                    classProc4.process(m);
                }


        );

        //root.getElements(new TypeFilter<CtClass>(CtClass.class)).stream().forEach(ctClass -> System.out.println(ctClass));
    }

    private static void initLauncher(File inDir) {

        //Initialise le launcher principal
        launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        //Ajoute les sources aux launchers et build les models
        launcher.addInputResource(inDir.getPath());
        launcher.buildModel();

        TestUnitHandler.initialize(launcher);
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
