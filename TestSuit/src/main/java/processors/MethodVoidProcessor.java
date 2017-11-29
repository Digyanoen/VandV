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
public class MethodVoidProcessor extends AbstractProcessor<CtClass> {

    private List <CtClass> ctClasses;
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
                           Mutant m = new Mutant(ctClass.getSimpleName(), (CtMethod) method, voidBody, "DeleteVoidBody", 1);
                           ctClass.replace(ctClassCloned);
                           Result.showResults(m);
                           ctClasses.add(ctClassCloned.clone());
                           ((CtMethod) method).setBody(body);
                       });

    }

    public List<CtClass> getCtClasses() {
        return ctClasses;
    }

}
