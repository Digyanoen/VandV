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
    private static JUnitCore junit;

    public static void main(String[] args){ //TODO Vérifier qu'il y a bien un pom.xml

        junit = new JUnitCore();

        File src = new File(args[0]);

        File dest = new File("dest/");

        if(dest.exists()) {
            try {
                deleteFiles(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileUtils.copyDirectory(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            deleteFiles(new File("dest/src/main/java"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        launcher = new MavenLauncher(".", MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        launcher.addInputResource(args[0] + "/src/main/java");
        launcher.setSourceOutputDirectory("dest/src/main/java");
        launcher.buildModel();
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
        }

        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven


        runTest(classRoot);
    }

    private static void runTest(File classRoot) {

        ClassLoader classLoader;

        //Initialise le ClassLoader
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});

            //Lance les différents tests et supprime les tests échouant
            //Pour chaque classe de test
            for(String elm: getTests(classRoot)) {

                //Convertie le CtType en Class
                Class<?> cls = null;
                try {
                    cls = classLoader.loadClass(elm);//elm.getQualifiedName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                List<Failure> result = new ArrayList<>();
                result.addAll(junit.run(cls).getFailures());

                for (Failure f : result) {
                    System.out.println("fail : " +f.getDescription().getMethodName());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        if(!file.delete()) throw new IOException("Unable to delete " + file.getName());
    }
}
