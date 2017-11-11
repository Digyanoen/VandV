import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeParameterReference;


public class MyProcess extends AbstractProcessor<CtClass> {

    public void process(CtClass elem) {
        Factory f = elem.getFactory();
        /*elem.getMethods().stream().forEach(
                m -> ((CtMethod)m).getBody().addStatement(elem.getFactory().createCodeSnippetStatement("System.out.println(\"bordel de merde\")"))
        );*/
        if(!elem.getSimpleName().contains("Test")) {

        }
}
}
