package processors;

import Testing.Result;
import Testing.TestUnitHandler;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtReturnImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor which replaces the body of a boolean method by true then false
 */
public class MethodBooleanProcessor extends MyProcess{
    private List<CtClass> ctClassList;

    /**
     *
     * For a given class, the processor will retrieves its boolean methods
     * For each method, the process replace the body by a return true then a return false statement
     * Then, the processor give the mutated class to the Result, in order to write the effects of this mutation
     * @param ctClass
     */
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
            ctClass.replace(ctClassCloned);
            ctClassList.add(ctClassCloned.clone());
            Mutant mutant = new Mutant(ctClass.getSimpleName(), (CtMethod) m, returnTrue, "ReplaceBooleanBody", ((CtMethod) m).getPosition().getLine());
            Result.showResults(mutant);
            ctClassCloned.replace(ctClass);

            ((CtMethod) m).setBody(returnFalse);
//            ctClass.replace(ctClassCloned);
            TestUnitHandler.replace(ctClassCloned);
            ctClassList.add(ctClassCloned.clone());
            mutant.setStatement(returnFalse);
            Result.showResults(mutant);
//            ctClassCloned.replace(ctClass);
            TestUnitHandler.replace(ctClass);
            ((CtMethod) m).setBody(body);

        });
    }


    @Override
    public List<CtClass> getCtClasses() {
        return ctClassList;
    }
}
