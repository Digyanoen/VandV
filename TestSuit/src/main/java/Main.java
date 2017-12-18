import Testing.TestUnitHandler;
import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.apache.commons.io.FileUtils;
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
        File src = new File(args[0]);

        //Création du dossier de destination
        File dest = TestUnitHandler.dest;

        //Suppression du dossier de destination et des potentiels fichiers présents
        if(dest.exists()) {
            try {
                deleteFiles(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Copie du dossier cible dans le dossier de destination et supprime les sources
        try {
            FileUtils.copyDirectory(src, dest);
            deleteFiles(new File("dest/src/main/java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialise le Launcher
        initLauncher(args[0]);

        launcher.addProcessor(new MethodChangeOperatorProcessor());
        launcher.addProcessor(new MethodBooleanProcessor());
        launcher.addProcessor(new MethodChangeIfOperatorProcessor());
        launcher.addProcessor(new MethodVoidProcessor());

        launcher.process();
//
//        List<CtMethod> meth = root.getElements(new TypeFilter<CtMethod>(CtMethod.class));
//
//        //list all classes of the model
//        for(CtMethod m : meth) {
//            System.out.println("method: "+m.getSimpleName());
//        }
//
//        List<CtClass> clazzes = root.getElements(new TypeFilter<CtClass>(CtClass.class){
//            @Override
//            public boolean matches(CtClass element) {
//                return super.matches(element) && !tests.contains(element);
//            }
//        });
//
//        //list all classes of the model
//        for(CtClass c : clazzes) {
//            System.out.println("class: "+c.getQualifiedName());
//        }

        // Launch a mutator
//        MethodChangeOperatorProcessor classProc = new MethodChangeOperatorProcessor();
//        clazzes.stream().forEach(m -> {
//                    classProc.process(m);
//                }
//
//
//        );
//        // Launch a mutator
//        MethodChangeIfOperatorProcessor classProc2 = new MethodChangeIfOperatorProcessor();
//        clazzes.stream().forEach(m -> {
//                    classProc2.process(m);
//                }
//
//
//        );
//        MethodBooleanProcessor classProc3 = new MethodBooleanProcessor();
//        clazzes.stream().forEach(m -> {
//                    classProc3.process(m);
//                }
//
//
//        );
//        MethodVoidProcessor classProc4 = new MethodVoidProcessor();
//        clazzes.stream().forEach(m -> {
//                    classProc4.process(m);
//                }
//
//
//        );

        //root.getElements(new TypeFilter<CtClass>(CtClass.class)).stream().forEach(ctClass -> System.out.println(ctClass));
    }

    private static void initLauncher(String inDir) {

        //Initialise le launcher principal
        launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        //Ajoute les sources de la cible en ressource
        launcher.addInputResource(inDir + "/src/main/java");

        //Change la destination du prettyprint()
        launcher.setSourceOutputDirectory("dest/src/main/java");

        //Construit le modèle
        launcher.buildModel();
        launcher.prettyprint();
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    private static void deleteFiles(File file) throws IOException {
        File [] children = file.listFiles();
        if(children != null) {
            for (File child : children) {
                deleteFiles(child);
            }
        }
        if(!file.delete()) throw new IOException();
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
