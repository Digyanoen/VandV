package processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import processors.MethodChangeIfOperatorProcessor;
import processors.MethodChangeOperatorProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.util.List;

public class MethodChangeOperatorProcessorTest {

    private MethodChangeOperatorProcessor methodChangeOperatorProcessor;

    public BinaryOperatorKind[][] binaryOperatorKind;


    @Before
    public void init() {
        methodChangeOperatorProcessor = new MethodChangeOperatorProcessor();
        binaryOperatorKind = new BinaryOperatorKind[][]{
                {BinaryOperatorKind.PLUS, BinaryOperatorKind.MINUS},
                {BinaryOperatorKind.MINUS, BinaryOperatorKind.PLUS},
                {BinaryOperatorKind.MUL, BinaryOperatorKind.DIV},
                {BinaryOperatorKind.DIV, BinaryOperatorKind.MUL},
                {BinaryOperatorKind.MOD, BinaryOperatorKind.MUL}
        };

    }


    @Test
    public void MethodChangeOperatorOpTest() {
        CtClass changeOp = new CtClassImpl();
        changeOp.setSimpleName("ChangeOp");
        changeOp.addModifier(ModifierKind.PUBLIC);
        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();

        CtBinaryOperator ctBinaryOperator = new CtBinaryOperatorImpl();
        ctBinaryOperator.setLeftHandOperand(new CtLiteralImpl<Integer>().setValue(2));
        ctBinaryOperator.setRightHandOperand(new CtLiteralImpl<Integer>().setValue(3));


        CtLocalVariable variable = new CtLocalVariableImpl<Integer>().setSimpleName("x");

        variable.setAssignment(ctBinaryOperator);
        ctBlock.addStatement(variable);


        ctMethod.setSimpleName("changeOpMethod");
        ctMethod.addModifier(ModifierKind.PUBLIC);

        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);

        ctMethod.setBody(ctBlock);
        changeOp.addMethod(ctMethod);

        for (int i = 0; i < binaryOperatorKind.length; i++) {
            int j = 0;
            ctBinaryOperator.setKind(binaryOperatorKind[i][0]);
            methodChangeOperatorProcessor.process(changeOp);
            List<CtClass> ctClassList = methodChangeOperatorProcessor.getCtClassList();
            Assert.assertTrue("List must contains more than one element", ctClassList.size()>1);
            for (CtClass c : ctClassList) {

                BinaryOperatorKind binary = ((CtBinaryOperator ) ((CtLocalVariable) ( c.getMethod("changeOpMethod").getBody().getStatement(0))).getAssignment()).getKind();
                Assert.assertTrue("Operator is " + binary + " expected " + binaryOperatorKind[i][j], binary == binaryOperatorKind[i][j]);
                j++;

            }

        }
    }
}



