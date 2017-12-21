package Testing;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.util.Context;
import net.sourceforge.cobertura.CoverageIgnore;
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
    public static File dest = new File("dest/"); //Dossier de destination
    private static List<Class<?>> clazzes = new ArrayList<>();

    private static Launcher launcher;
    private static ClassLoader classLoader;

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
            result.addAll(junit.run(clazz).getFailures());
        }

        return result;
    }


    public static void initialize(Launcher l) {
        launcher = l;//Récupère le dossier des classes de tests

        completeCompile();

        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven

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

    @CoverageIgnore
    private static void compile() throws CompilerException {
//        try {
//            deleteFiles(new File("dest/src/main/java"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //launcher.prettyprint();

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


    @CoverageIgnore
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

    @CoverageIgnore
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

    @CoverageIgnore
    public static void deleteFiles(File file) throws IOException {
        File [] children = file.listFiles();
        if(children != null) {
            for (File child : children) {
                deleteFiles(child);
            }
        }
        if(!file.delete()) throw new IOException();
    }




    @CoverageIgnore
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

    public static void replace(CtClass element){
        CtType type = element.getPosition().getCompilationUnit().getMainType();
        Factory factory = type.getFactory();
        Environment env = factory.getEnvironment();

        JavaOutputProcessor processor = new JavaOutputProcessor(new File("dest/src/main/java"), new DefaultJavaPrettyPrinter(env));
        processor.setFactory(factory);

        processor.createJavaFile(type);
    }

    @CoverageIgnore
    public static ClassLoader getClassLoader() {
        return classLoader;
    }
    @CoverageIgnore
    public static void setClassLoader(ClassLoader classLoader) {
        TestUnitHandler.classLoader = classLoader;
    }
    @CoverageIgnore
    public static List<Class<?>> getClazzes() {
        return clazzes;
    }
    @CoverageIgnore
    public static void setClazzes(List<Class<?>> clazzes) {
        TestUnitHandler.clazzes = clazzes;
    }
}