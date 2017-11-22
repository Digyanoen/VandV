package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtReturnImpl;

import java.util.ArrayList;
import java.util.List;


public class MethodBooleanProcessor extends AbstractProcessor<CtClass> {
    List<CtClass> ctClassList;

    @Override
    public void process(CtClass ctClass) {
        ctClassList = new ArrayList<>();
        ctClassList.add(ctClass);
        CtClass ctClassCloned = ctClass.clone();
        ctClassCloned.getMethods().stream().filter(m ->((CtMethod) m).getType().getSimpleName().equals("boolean") ).forEach(m ->
        {
            CtBlock body = ((CtMethod) m).getBody();
            CtReturn returnTrue = new CtReturnImpl<Boolean>();
            returnTrue.setReturnedExpression(new CtLiteralImpl().setValue(true));
            CtReturn returnFalse = new CtReturnImpl<Boolean>();
            returnFalse.setReturnedExpression(new CtLiteralImpl().setValue(false));

            ((CtMethod) m).setBody(returnTrue);
            ctClassList.add(ctClassCloned.clone());
            ((CtMethod) m).setBody(returnFalse);
            ctClassList.add(ctClassCloned.clone());
            ((CtMethod) m).setBody(body);
        });
    }

    public List<CtClass> getCtClassList() {
        return ctClassList;
    }

}
