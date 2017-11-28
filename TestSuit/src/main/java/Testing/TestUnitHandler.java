package Testing;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.declaration.CtClass;
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

    private static Launcher launcher;
    private static SpoonModelBuilder compiler;
    private static JUnitCore junit;
    private static List<CtType> tests;

    /**
     * Récupère la liste des tests qui ont échoué
     * @return La liste d'échecs
     */
    public static List<Failure> getFailures(){

        //Crée le compiler Spoon
        compiler = launcher.createCompiler(launcher.getFactory());

//        System.out.println(launcher.getModel().getElements(new TypeFilter<>(CtClass.class)));

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
     * Initialise le handler, à savoir le JUnitCore, le compiler Spoon et la liste des tests présents dans le modèle
     * @param l launcher contenant le modèle du projet à tester
     */
    public static void initialize(Launcher l){

         launcher = l;

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
            CtType c = (CtType)meth.getParent();
            if(!tests.contains(c))tests.add(c);
        }
    }

    /**
     * Récupère les tests du modèle
     * @return La liste de tests
     */
    public static List<CtType> getTests() {
        return tests;
    }
}