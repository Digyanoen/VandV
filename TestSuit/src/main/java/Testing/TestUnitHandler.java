package Testing;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUnitHandler {

    private static JUnitCore junit = new JUnitCore();
    private static ClassLoader classLoader;
    public static File dest = new File("dest/"); //Dossier de destination
    private static Launcher launcher;



    /**
     * Récupère la liste des tests qui ont échoué
     * @return La liste d'échecs
     */
   public static List<Failure> getFailures() throws CompilerException {


        compile();

        List<Failure> result = new ArrayList<>();

        //Récupère le dossier des classes de tests
        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven

        //Initialise le ClassLoader
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});

            //Lance les différents tests
            for(String elm: getTests(classRoot)) {

                Class<?> cls = null;
                try {
                    cls = classLoader.loadClass(elm);//elm.getQualifiedName());
                    result.addAll(junit.run(cls).getFailures());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void deleteSpoonDirectory() throws IOException {
        File spooned = new File("spooned-classes");
        if(spooned.exists() && spooned.isDirectory()){

            File[] files = spooned.listFiles();

            if (files.length>0) {
                System.out.println(files.length);
                for(File f : files){
                    deleteFiles(f);
                }
            }
            spooned.delete();
        }
    }

    private static void compile() throws CompilerException {
        try {
            deleteFiles(new File("dest/src/main/java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        launcher.prettyprint();
        System.out.println("coucou");
        String[]command ={"mvn","test"};
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
        launcher = l;
    }
}