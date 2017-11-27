package Testing;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class TestUnitHandler {

    private static SpoonModelBuilder compiler;
    private static JUnitCore junit;
    private static List<CtType> tests;

    public static List<Failure> getFailures(){
        compiler.compile();

        File classRoot = new File("./spooned-classes/");

        List<Failure> result = new ArrayList<Failure>();

        //Initialise le ClassLoader
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{classRoot.toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Lance les différents tests et supprime les tests échouant
        //Pour chaque classe de test
        for(CtType elm: tests) {

            //Convertie le CtType en Class
            Class<?> cls = null;
            try {
                cls = classLoader.loadClass(elm.getQualifiedName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            result.addAll(junit.run(cls).getFailures());
        }

        return result;
    }

    /**
     * Initialise le tout //TODO Revoir la javadoc de TestUnitHandler.intialize
     * @param launcher launcher contenant le modèle du projet à tester
     */
    public static void initialize(Launcher launcher){

        compiler = launcher.createCompiler(launcher.getFactory());
        //Initialise JUnit pour l'exécution des tests
        junit = new JUnitCore();

        //Récupère la liste des classes de test
        tests = new ArrayList<CtType>();

        for (CtMethod<?> meth : launcher.getModel().getRootPackage().getElements(new TypeFilter<CtMethod>(CtMethod.class) {
            @Override
            public boolean matches(CtMethod element) {
                return !(tests.contains((CtType)element.getParent())) && super.matches(element) && (element.getAnnotation(Test.class) != null);
            }
        })) {
            tests.add((CtType)meth.getParent());
        }
    }

    /**
     * Récupère les tests du modèle
     * @return
     */
    public static List<CtType> getTests() {
        return tests;
    }
}

