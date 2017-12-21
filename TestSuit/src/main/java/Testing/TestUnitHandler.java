package Testing;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.util.Context;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.JavaOutputProcessor;

import javax.tools.JavaFileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class TestUnitHandler {

    private static JUnitCore junit = new JUnitCore();
    private static ClassLoader classLoader;
    public static File dest = new File("dest/"); //Dossier de destination
    private static Launcher launcher;
    private static List<Class<?>> clazzes;

    /**
     * Récupère la liste des tests qui ont échoué
     * @return La liste d'échecs
     */
    static List<Failure> getFailures() throws CompilerException {

        compile();

        List<Failure> result = new ArrayList<>();

//        //Récupère le dossier des classes de tests
//        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven
//
//        //Initialise le ClassLoader
//        try {
//            if (classLoader == null) classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});
//            //Lance les différents tests
//            for(String elm: getTests(classRoot)) {
//
//                Class<?> cls = null;
//                try {
//                    cls = classLoader.loadClass(elm);//elm.getQualifiedName());
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                result.addAll(junit.run(cls).getFailures());
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        for (Class<?> clazz : clazzes) {
	    System.out.println("Entrée : "+clazz.getSimpleName());
            result.addAll(junit.run(clazz).getFailures());
        }

        return result;
    }

    private static void compile() throws CompilerException {
        try {
            deleteFiles(new File("dest/src/main/java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        launcher.prettyprint();

        String[]command ={"mvn","compile"};
        ProcessBuilder ps=new ProcessBuilder(command);
        ps.redirectErrorStream(true);
        ps.directory(dest);

        Process process;
        try {
            process = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            in.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CompilerException("Fail to compile");
        }
    }

//    public static void removeJunkTest() throws CompilerException {
//
//        for (CtMethod ignored : launcher.getModel().getElements(new TypeFilter<CtMethod>(CtMethod.class) {
//            @Override
//            public boolean matches(CtMethod element) {
//                return super.matches(element) && (element.getAnnotation(Ignore.class) != null);
//            }
//        })) {
//            ((CtType) ignored.getParent()).removeMethod(ignored);
//        }
//
//        List<Failure> methodsToJunk = getFailures();
//
//        //TODO Améliorer la suppression des tests
//        //Lance les différents tests et supprime les tests échouant
//        //Pour chaque classe de test
//        for(CtType elm: tests) {
//
//            //Pour chaque échec supprime le test du modèle
//            for (Failure junk : methodsToJunk) {
//                System.out.println(junk.getDescription().getClassName());
//                if(junk.getDescription().getClassName().equals(elm.getQualifiedName())) { //TODO Vérifier le cas des classes de même nom
//                    elm.removeMethod(elm.getMethod(junk.getDescription().getMethodName()));
//                }
//            }
//        }
//    }


    private static List<String> getTests(File classRoot) {
        List<String> res = new ArrayList<>();

        File[] files = classRoot.listFiles();

        if (files != null) {
            for (File file : files) {
                res.addAll(getTests(file, ""));
            }
        }

        return res;
    }

    private static List<String> getTests(File dir, String pack) {
        List<String> res = new ArrayList<>();
        String name = dir.getName();

        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    res.addAll(getTests(file, pack + dir.getName() + "."));
                }
            }
        }else if(dir.isFile() && name.endsWith(".class") && !name.contains("$")){
            res.add(pack + name.substring(0,name.length()-6));
        }

        return res;
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


    public static void initialize(Launcher l) {
        launcher = l;//Récupère le dossier des classes de tests

        completeCompile();

        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven

        clazzes = new ArrayList<>();

        //Initialise le ClassLoader
        try {
            if (classLoader == null) classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});
            //Lance les différents tests
            for(String elm: getTests(classRoot)) {

                Class<?> cls = null;
                try {
                    cls = classLoader.loadClass(elm);//elm.getQualifiedName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                clazzes.add(cls);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void completeCompile() {

        launcher.prettyprint();

        String[]command ={"mvn","install"};
        ProcessBuilder ps=new ProcessBuilder(command);
        ps.redirectErrorStream(true);
        ps.directory(dest);

        Process process;
        try {
            process = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
            in.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void replace(CtClass element){
//        CtType type = element.getPosition().getCompilationUnit().getMainType();
//        Factory factory = type.getFactory();
//        Environment env = factory.getEnvironment();
//
//        JavaOutputProcessor processor = new JavaOutputProcessor(new File(directory), new DefaultJavaPrettyPrinter(env));
//        processor.setFactory(factory);
//
//        processor.createJavaFile(type);
//    }
}
