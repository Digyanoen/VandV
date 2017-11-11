import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class Main {
    public static void main(String[] args){

        try {
            Process p = Runtime.getRuntime().exec("ls");
            printLines("test", p.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(args.length < 2){
            throw new IllegalArgumentException("Expected parameters : <source folder> and <dest folder>");
        }

        //("/home/julien/Documents/TP/VV/DummyProject/src/test/java/");


        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);

        File inDir = new File(args[0]);

        launcher.addInputResource(inDir.getPath());
        launcher.setSourceOutputDirectory(new File(args[1]));
        launcher.addProcessor(new MyProcess());
        launcher.buildModel();
        launcher.prettyprint();
       // launcher.run();
        CtModel root = launcher.getModel();

        for (CtMethod<?> meth : root.getRootPackage().getElements(new TypeFilter<CtMethod>(CtMethod.class) {
            @Override
            public boolean matches(CtMethod element) {
                return super.matches(element) && element.getAnnotation(Test.class) == null;
            }
        })) {
            System.out.println("pouet "+meth.getType().getSimpleName());
            meth.getBody().forEach((CtStatement ac) -> {
                ac.getFactory();
            });
            //assertTrue("naming contract violated for "+meth.getParent(CtClass.class).getSimpleName(), meth.getParent(CtClass.class).getSimpleName().startsWith("Test") || meth.getParent(CtClass.class).getSimpleName().endsWith("Test"));
        }

        Result r = new JUnitCore().run();

        root.getAllPackages().stream().forEach(
                p->System.out.println("p: " + p.getQualifiedName())
        );

        System.out.println();

        root.getAllTypes().stream().forEach(
                s->System.out.println("s: " + s.getQualifiedName())
        );


 //list all classes of the model
        for(CtType<?> s : root.getAllTypes()) {
            System.out.println("class: "+s.getQualifiedName());
        }
        MethodChangeOperatorProcessor classProc = new MethodChangeOperatorProcessor();
        root.getAllTypes().stream().forEach(m -> {
                    classProc.process((CtClass) m);
                    System.out.println(m);
                }
        );



        System.out.println("Hello World!");
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
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
