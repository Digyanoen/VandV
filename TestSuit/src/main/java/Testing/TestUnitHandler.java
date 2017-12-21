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
     * Compile les sources du projet cible et
     * récupère la liste des tests qui ont échoué
     * @return La liste d'échecs
     * @throws CompilerException La compilation a renvoyé une erreur
     */
    static List<Failure> getFailures() throws CompilerException {

        //Compile le projet cible
        compile();

        List<Failure> result = new ArrayList<>();

        //Lance les tests et récupère les tests échouant
        for (Class<?> clazz : clazzes) {
            result.addAll(junit.run(clazz).getFailures());
        }

        return result;
    }

    /**
     * Initialise TestUnitHandler
     * @param l Launcher Spoon contenant le modèle à tester
     */
    public static void initialize(Launcher l) {
        launcher = l;//Récupère le dossier des classes de tests

        //Compile entièrement le projet cible
        completeCompile();

        File classRoot = new File("dest/target/test-classes"); //Condition sine qua non

        //Initialise le ClassLoader
        try {
            if (classLoader == null) classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});
            //Charge les différents tests
            for(String elm: getTests(classRoot)) {

                Class<?> cls = null;
                try {
                    cls = classLoader.loadClass(elm);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                clazzes.add(cls);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * lance la commande "mvn compile" sur le projet cible, copié dans le dossier "dest"
     * @throws CompilerException La commande a échoué
     */
    @CoverageIgnore
    private static void compile() throws CompilerException {

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

    /**
     * Récupère les tests sous la forme acceptée par classLoader
     * @param classRoot Objet File pointant sur le dossier contenant les .class des tests
     * @return La liste des noms des classes sous la forme nom.de.package.classe
     */
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

    /**
     * Méthode auxiliaire de getTests(File classRoot)
     * @param dir Répertoir ou fichier courant
     * @param pack nom du package courant
     * @return La liste des noms des classes sous la forme nom.de.package.classe contenu dans dir
     */
    @CoverageIgnore
    private static List<String> getTests(File dir, String pack) {
        List<String> res = new ArrayList<>();
        String name = dir.getName();

        //Si dir est un répertoire, appelle la méthode sur son contenu
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    res.addAll(getTests(file, pack + dir.getName() + "."));
                }
            }
        }else if(dir.isFile() && name.endsWith(".class") && !name.contains("$")){
            //Si dir est un fichier .class génère le nom de package
            res.add(pack + name.substring(0,name.length()-6));
        }

        return res;
    }

    /**
     * Supprime les documents donnés en paramêtre
     * @param file Fichier ou répertoire à supprimer
     * @throws IOException Le fichier ou le répertoire n'a pas pu être supprimé
     */
    public static void deleteFiles(File file) throws IOException {
        File [] children = file.listFiles();
        if(children != null) {
            for (File child : children) {
                deleteFiles(child);
            }
        }
        if(!file.delete()) throw new IOException();
    }

    /**
     * Compile l'intégralité du projet cible
     */
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

    /**
     * Génère le fichier Spoon dans la copie du projet cible
     * @param element Class Spoon à générer
     */
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