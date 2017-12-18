package Testing;

import com.sun.org.apache.xalan.internal.xsltc.compiler.CompilerException;
import org.apache.log4j.Level;
import org.junit.Ignore;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;

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

    private static Launcher launcher;
    private static SpoonModelBuilder compiler;
    private static List<CtType> tests;

    private static JUnitCore junit = new JUnitCore();
    public static File dest = new File("dest/"); //Dossier de destination

    /**
     * Récupère la liste des tests qui ont échoué
     * @return La liste d'échecs
     */
    public static List<Failure> getFailures() throws CompilerException {

        compile();

        List<Failure> result = new ArrayList<>();

        //Récupère le dossier des classes de tests
        File classRoot = new File("dest/target/test-classes"); //TODO Confirmer le lieu de la compile Maven

        ClassLoader classLoader;

        //Initialise le ClassLoader
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});

            //Lance les différents tests
            for(String elm: getTests(classRoot)) {

                Class<?> cls = null;
                try {
                    cls = classLoader.loadClass(elm);//elm.getQualifiedName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                result.addAll(junit.run(cls).getFailures());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void deleteSpoonDirectory() {
        File spooned = new File("spooned-classes");
        if(spooned.exists() && spooned.isDirectory()){
            System.out.println("Testset");
            System.out.println(spooned.getPath());
            for(File f : spooned.listFiles()){
                deleteFiles(f);
            }
            spooned.delete();
        }
    }

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

    /**
     * Récupère les tests du modèle
     * @return La liste de tests
     */
    public static List<CtType> getTests() {
        return tests;
    }

    private static void deleteFiles(File f){
        if(f.isDirectory()){
            for(File file : f.listFiles()){
                deleteFiles(file);
            }
        }
        f.delete();
    }


}