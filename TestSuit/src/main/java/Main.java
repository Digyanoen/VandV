import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;

import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Main {
    public static void main(String[] args){


        //("/home/julien/Documents/TP/VV/DummyProject/src/test/java/");


        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        File inDir = new File("/home/julien/Documents/TP/VV/DummyProject/src");

        launcher.addInputResource(inDir.getPath());
        launcher.setSourceOutputDirectory(new File("/home/julien/Documents/TP/VV/VandV/TestSuit/ressources"));
        launcher.addProcessor(new MyProcess());
        launcher.buildModel();
        launcher.prettyprint();
        CtModel root = launcher.getModel();

        for (CtMethod<?> meth : root.getRootPackage().getElements(new TypeFilter<CtMethod>(CtMethod.class) {
            @Override
            public boolean matches(CtMethod element) {
                return super.matches(element) && element.getAnnotation(Test.class) == null;
            }
        })) {
            meth.getBody().forEach((CtStatement ac) -> {
                ac.getFactory();
            });
            //assertTrue("naming contract violated for "+meth.getParent(CtClass.class).getSimpleName(), meth.getParent(CtClass.class).getSimpleName().startsWith("Test") || meth.getParent(CtClass.class).getSimpleName().endsWith("Test"));
        }

        Result r = new JUnitCore().run();

//        root.getAllPackages().stream().forEach(
//                p->System.out.println("p: " + p.getQualifiedName())
//        );

//        System.out.println();
//
//        root.getAllTypes().stream().forEach(
//                s->System.out.println("s: " + s.getQualifiedName())
//        );


// list all classes of the model
//        for(CtType<?> s : root.getAllTypes()) {
//            System.out.println("class: "+s.getQualifiedName());
//        }

        System.out.println("Hello World!");
    }

//    @Test
//    public void testGoodTestClassNames() throws Exception {
//        SpoonAPI spoon = new Launcher();
//        spoon.addInputResource("src/test/java/");
//        spoon.buildModel();
//
//        for (CtMethod<?> meth : spoon.getModel().getRootPackage().getElements(new TypeFilter<CtMethod>(CtMethod.class) {
//            @Override
//            public boolean matches(CtMethod element) {
//                return super.matches(element) && element.getAnnotation(Test.class) != null;
//            }
//        })) {
//            assertTrue("naming contract violated for "+meth.getParent(CtClass.class).getSimpleName(), meth.getParent(CtClass.class).getSimpleName().startsWith("Test") || meth.getParent(CtClass.class).getSimpleName().endsWith("Test"));
//        }
//    }


}
