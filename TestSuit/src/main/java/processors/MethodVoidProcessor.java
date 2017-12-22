package processors;

import Testing.Result;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor which removes the body of a void method
 */
public class MethodVoidProcessor extends MyProcess{

    private List <CtClass> ctClasses;

    /**
     *
     * For a given class, the processor will retrieves its void methods
     * For each method, the process delete the body
     * Then, the processor give the mutated class to the Result, in order to write the effects of this mutation
     * @param ctClass
     */
    @Override
    public void process(CtClass ctClass) {
        ctClasses = new ArrayList<>();
        CtClass ctClassCloned = ctClass.clone();
        ctClasses.add(ctClass);

       ctClassCloned.getMethods().stream().filter(m -> ((CtMethod)m).getType().getSimpleName().equals("void") )
               .forEach(
                       method ->
                       {
                           CtBlock body = ((CtMethod)method).getBody();
                           CtBlock voidBody = ctClass.getFactory().createBlock();
                           ((CtMethod)method).setBody(voidBody);
                           Mutant m = new Mutant(ctClass.getSimpleName(), (CtMethod) method, voidBody, "DeleteVoidBody", ((CtMethod) method).getPosition().getLine());
                           ctClass.replace(ctClassCloned);
                           Result.showResults(m);
                           ctClasses.add(ctClassCloned.clone());
                           ((CtMethod) method).setBody(body);
                       });

    }

    @Override
    public List<CtClass> getCtClasses() {
        return ctClasses;
    }

}
