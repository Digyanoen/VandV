package processor;

import Testing.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest(Result.class)
public class MethodChangeOperatorProcessorTest extends AbstractTest{


    public BinaryOperatorKind[][] binaryOperatorKind;


    @Before
    public void init() {
        process = new MethodChangeOperatorProcessor();
        binaryOperatorKind = new BinaryOperatorKind[][]{
                {BinaryOperatorKind.PLUS, BinaryOperatorKind.MINUS},
                {BinaryOperatorKind.MINUS, BinaryOperatorKind.PLUS},
                {BinaryOperatorKind.MUL, BinaryOperatorKind.DIV},
                {BinaryOperatorKind.DIV, BinaryOperatorKind.MUL},
                {BinaryOperatorKind.MOD, BinaryOperatorKind.MUL},
                {BinaryOperatorKind.LE, BinaryOperatorKind.LE}
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
        PowerMockito.mockStatic(Result.class);
        PowerMockito.doNothing().when(Result.class);

        for (int i = 0; i < binaryOperatorKind.length; i++) {
            int j = 0;
            ctBinaryOperator.setKind(binaryOperatorKind[i][0]);
            process.process(changeOp);
            List<CtClass> ctClassList = process.getCtClasses();
            Assert.assertTrue("List must contains more than one element", ctClassList.size()>1);
            for (CtClass c : ctClassList) {

                BinaryOperatorKind binary = ((CtBinaryOperator ) ((CtLocalVariable) ( c.getMethod("changeOpMethod").getBody().getStatement(0))).getAssignment()).getKind();
                Assert.assertTrue("Operator is " + binary + " expected " + binaryOperatorKind[i][j], binary == binaryOperatorKind[i][j]);
                j++;

            }

        }
    }

    @Test
    public void MethodChangeUnaryTest() {
        CtClass changeOp = new CtClassImpl();
        changeOp.setSimpleName("ChangeOp");
        changeOp.addModifier(ModifierKind.PUBLIC);
        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();



        CtLocalVariable variable = new CtLocalVariableImpl<Integer>().setSimpleName("x");
        variable.setType(new CtTypeReferenceImpl().setSimpleName("int"));


        variable.setAssignment(new CtLiteralImpl<Integer>().setValue(4));
        ctBlock.addStatement(variable);


        ctMethod.setSimpleName("changeOpMethod");
        ctMethod.addModifier(ModifierKind.PUBLIC);

        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);

        ctMethod.setBody(ctBlock);
        changeOp.addMethod(ctMethod);

        process.process(changeOp);
        Assert.assertTrue("Size list must be one", process.getCtClasses().size() == 1);


    }


}



