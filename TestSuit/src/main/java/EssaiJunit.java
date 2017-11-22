
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
    Classe de test, ne pas tenir compte de la syntaxe, des problèmes d'optimisation,
    des exceptions possibles et autres
 **/

public class EssaiJunit {

    //("/home/julien/Documents/TP/VV/DummyProject/src/test/java/");


    public static void main(String[] args){
        List<String> essayons = new ArrayList<String>();
        for(String a: args){
            essayons.add(a);
        }
        String[] b = new String[essayons.size()];

        test2(args);
    }

    public static void test1(String[] args){
        JUnitCore junit = new JUnitCore();
        Result result = new Result();

        File root = new File(args[0]+"/target/test-classes/");
        System.out.println(root.isDirectory());
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Class<?> cls = null;
        try {
            cls = classLoader.loadClass("fr.inria.AppTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        result = junit.run(cls);
    }

    public static void test2 (String... args){

        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        Launcher testLauncher = new Launcher();
        testLauncher.getEnvironment().setAutoImports(true);
        testLauncher.getEnvironment().setNoClasspath(true);

        File inDir = new File(args[0]+"/src/main/java");
        File inTestDir = new File(args[0]+"/src/test/java");

        launcher.addInputResource(inDir.getPath());
        launcher.setSourceOutputDirectory(new File(args[1]));
        launcher.buildModel();


        testLauncher.addInputResource(inTestDir.getPath());
        testLauncher.buildModel();

        Factory fact = launcher.getFactory();

        List<CtClass> testclazzes = testLauncher.getModel()
                .getElements(new TypeFilter<CtClass>(CtClass.class));
        List<CtType> testtypes = testLauncher.getModel()
                .getElements(new TypeFilter<CtType>(CtType.class));

        testtypes.forEach(ctType -> System.out.println("test : " + ctType.getSimpleName()));

        launcher.getModel().getElements(new TypeFilter<>(CtMethod.class)).forEach(ctMethod -> System.out.println("before : "+ctMethod.getSimpleName()));
        //launcher.getModel().getElements(new TypeFilter<CtClass>(CtClass.class)).addAll(testclazzes);
        CtPackage rootp = launcher.getModel().getRootPackage();
        testtypes.forEach(ctType -> rootp.addType(ctType));
        launcher.getModel().getElements(new TypeFilter<>(CtMethod.class)).forEach(ctMethod -> System.out.println("after : " +ctMethod.getSimpleName()));

        SpoonModelBuilder compiler = launcher.createCompiler(fact);

        compiler.compile();


        JUnitCore junit = new JUnitCore();

        File root = new File("./spooned-classes/");
        System.out.println(root.isDirectory());
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("Avant suppression");

        for (CtClass c : fact.getModel().getElements(new TypeFilter<CtClass>(CtClass.class))) {
            c.getMethods().stream().forEach( m -> {System.out.println(((CtMethod) m).getSimpleName());});
            System.out.println(c.getSimpleName());
        }

        for(CtType elm: testtypes) {

            Class<?> cls = null;
            try {
                System.out.println(elm.getQualifiedName());
                cls = classLoader.loadClass(elm.getQualifiedName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String methodToJunk = junit.run(cls).getFailures().get(0).getDescription().getMethodName();

            elm.removeMethod(elm.getMethod(methodToJunk));

            for (Object e: elm.getMethods()) {
                System.out.println("test : "+((CtMethod)e).getSimpleName());
            }

        }

        System.out.println("Après suppression");

        for (CtClass c : fact.getModel().getElements(new TypeFilter<CtClass>(CtClass.class))) {
            c.getMethods().stream().forEach( m -> {System.out.println(((CtMethod) m).getSimpleName());});
            System.out.println(c.getSimpleName());
        }

        compiler.compile();
    }


//    private static void compileclazzes(List<CtClass> clazzes) {
//        List<Class> compiledMutants = new ArrayList<Class>();
//        for (CtClass mutantClass : clazzes) {
//            Class<?> klass = InMemoryJavaCompiler.newInstance().compile(
//                    mutantClass.getQualifiedName(), "package "
//                            + mutantClass.getPackage().getQualifiedName() + ";"
//                            + mutantClass);
//            compiledMutants.add(klass);
//        }
//        return compiledMutants;
//
//    }
}
