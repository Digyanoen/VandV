import org.apache.commons.io.FileUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class TestCompil {
    private static Launcher launcher;
    private static URLClassLoader classLoader;
    private static JUnitCore junit;

    public static void main(String[] args){ //TODO Vérifier qu'il y a bien un pom.xml

        junit = new JUnitCore();

        File src = new File(args[0]);

        File dest = new File("dest/");

        deleteFiles(dest);

        try {
            FileUtils.copyDirectory(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        deleteFiles(new File("dest/src/main/java"));

//        launcher = new MavenLauncher(".", MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        launcher.addInputResource(args[0] + "/src/main/java");
        launcher.getModelBuilder().getSourceOutputDirectory();
        launcher.setSourceOutputDirectory("dest/src/main/java");
        launcher.buildModel();
        launcher.prettyprint();

        String[]command ={"mvn","compile"};
        ProcessBuilder ps=new ProcessBuilder(command);
        ps.redirectErrorStream(true);
        ps.directory(dest);//args[0]));

        Process process = null;
        try {
            process = ps.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> stdout = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = in.readLine()) != null) {
                stdout.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven

        //Initialise le ClassLoader
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        runTest(classRoot);
    }

    private static void runTest(File classRoot) {

        //Lance les différents tests et supprime les tests échouant
        //Pour chaque classe de test
        for(String elm: getTests(classRoot)) {

            System.out.println(elm);

            //Convertie le CtType en Class
            Class<?> cls = null;
            try {
                cls = classLoader.loadClass(elm);//elm.getQualifiedName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            List<Failure> result = new ArrayList();
            result.addAll(junit.run(cls).getFailures());

            for (Failure f : result) {
                System.out.println("fail : " +f.getDescription().getMethodName());
            }
        }

    }

    private static List<String> getTests(File classRoot) {
        List<String> res = new ArrayList<>();

        for (File file : classRoot.listFiles()) {
            res.addAll(getTests(file,""));
        }

        return res;
    }

    private static List<String> getTests(File dir, String pack) {
        List<String> res = new ArrayList<>();
        String name = dir.getName();

        if(dir.isDirectory()){
            for (File file : dir.listFiles()) {
                res.addAll(getTests(file, pack + dir.getName() + "."));
            }
        }else if(dir.isFile() && name.endsWith(".class") && !name.contains("$")){
            res.add(pack + name.substring(0,name.length()-6));
        }

        return res;
    }

    private static void deleteFiles(File file) {
        if(file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFiles(child);
            }
        }
        file.delete();
    }
}
