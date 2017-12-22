package Testing;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;

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
     * Compile l'ensemble du projet cible et récupère la liste des tests qui ont échoué
     * @return La liste des tests ayant échoué
     * @exception CompilerException La compilation à échouée
     */
    static List<Failure> getFailures() throws CompilerException {

        compile();

        List<Failure> result = new ArrayList<>();

        for (Class<?> clazz : clazzes) {
	    System.out.println("Entrée : "+clazz.getSimpleName());
            result.addAll(junit.run(clazz).getFailures());
        }

        return result;
    }

    /**
     * Compile les fichiers sources avec mvn compile
     * @throws CompilerException La compilation a échoué
     */
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

    /**
     * Récupère la liste des tests sous la forme nom.du.package.classe
     * @param classRoot dossier source des tests
     * @return Liste des classe de la forme nom.du.package.classe
     */
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
     * Méthode auxiliaire de getTests
     * @param dir répertoire/fichier courant
     * @param pack nom de package courant
     * @return liste des classes sous la forme nom.du.package.classe
     */
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

    /**
     * Supprime le répertoire/fichier
     * @param file répertoire/fichier à supprimer
     * @throws IOException La suppresion n'a pas fonctionné
     */
    private static void deleteFiles(File file) throws IOException {
        File [] children = file.listFiles();
        if(children != null) {
            for (File child : children) {
                deleteFiles(child);
            }
        }
        if(!file.delete()) throw new IOException();
    }

    /**
     * Initialise TestUnitHandler
     * @param l Launcher Spoon contenant le modèle du projet cible.
     */
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

    /**
     * Compile l'intégralité du projet cible
     */
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
}
