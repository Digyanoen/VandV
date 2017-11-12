import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

public class MethodVoidProcessor extends AbstractProcessor<CtClass> {


    @Override
    public void process(CtClass ctClass) {
       Set ctMethods = ctClass.getMethods();

       ctMethods.stream().filter(m -> ((CtMethod)m).getType().getSimpleName().equals("void") )
               .forEach(
                       method -> ((CtMethod)method).setBody(null));

    }


}
