package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MethodVoidProcessor extends AbstractProcessor<CtClass> {

    List <CtClass> ctClasses;
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
                           ((CtMethod)method).setBody(null);
                           ctClasses.add(ctClassCloned.clone());
                           ((CtMethod) method).setBody(body);
                       });

    }

    public List<CtClass> getCtClasses() {
        return ctClasses;
    }

    public void setCtClasses(List<CtClass> ctClasses) {
        this.ctClasses = ctClasses;
    }
}