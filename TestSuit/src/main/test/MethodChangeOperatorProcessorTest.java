import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.ClassFactory;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.util.HashSet;
import java.util.Set;

public class MethodChangeOperatorProcessorTest {

    private MethodChangeOperatorProcessor methodChangeOperatorProcessor;


    @Before
    public void init(){
        methodChangeOperatorProcessor = new MethodChangeOperatorProcessor();
    }


    @Test
    public void changeAndTest(){
        CtClass changeAnd = new CtClassImpl();
        changeAnd.setSimpleName("ChangeAnd");
        changeAnd.addModifier(ModifierKind.PUBLIC);

        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();
        CtIf ctIf = new CtIfImpl();
        CtStatement ctStatement1 = changeAnd.getFactory().createCodeSnippetStatement("System.out.println(\"test if\")");
        CtStatement ctStatement2 = changeAnd.getFactory().createCodeSnippetStatement("System.out.println(\"test else\")");
        CtBinaryOperatorImpl<Boolean> condition = new CtBinaryOperatorImpl<Boolean>();
        condition.setLeftHandOperand((new CtLiteralImpl<Integer>()).setValue(2));
        condition.setRightHandOperand((new CtLiteralImpl<Integer>()).setValue(3));
        condition.setKind(BinaryOperatorKind.AND);


        ctIf.setThenStatement(ctStatement1);
        ctIf.setElseStatement(ctStatement2);
        ctIf.setCondition(condition);

        ctBlock.addStatement(ctIf);
        ctMethod.setSimpleName("changeAndMethod");
        ctMethod.addModifier(ModifierKind.PUBLIC);

        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);

        ctMethod.setBody(ctBlock);
        changeAnd.addMethod(ctMethod);
        methodChangeOperatorProcessor.process(changeAnd);

        Assert.assertTrue("Operator is OR", condition.getKind() == BinaryOperatorKind.OR);

    }
}
