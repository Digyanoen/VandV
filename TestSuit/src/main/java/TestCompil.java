import Testing.TestUnitHandler;
import org.apache.commons.io.FileUtils;
import spoon.Launcher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TestCompil {
    private static Launcher launcher;

    public static void main(String[] args){

        File src = new File(args[0]);

        File dest = new File("dest/");

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
        System.out.println((args[0] + "/src/main/java"));
        launcher.getModelBuilder().getSourceOutputDirectory();
        launcher.setSourceOutputDirectory("dest/src/main/java");
        launcher.buildModel();
        launcher.prettyprint();

        String[]command ={"mvn","install"};
        ProcessBuilder ps=new ProcessBuilder(command);
        ps.redirectErrorStream(true);
        ps.directory(launcher.getModelBuilder().getSourceOutputDirectory());//args[0]));

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
    }

    private static void deleteFiles(File file) {
        System.out.println(file.isDirectory());
        if(file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFiles(child);
            }
        }
        file.delete();
    }
}
