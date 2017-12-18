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
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtInterfaceImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.util.*;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Result.class)
public class MethodChangeIfOperatorProcessorTest extends AbstractTest{


    public BinaryOperatorKind[][] binaryOperatorKind;


    @Before
    public void init() {
        this.process = new MethodChangeIfOperatorProcessor();
        binaryOperatorKind = new BinaryOperatorKind[][]{
                {BinaryOperatorKind.AND, BinaryOperatorKind.OR},
                {BinaryOperatorKind.OR, BinaryOperatorKind.AND},
                {BinaryOperatorKind.EQ, BinaryOperatorKind.NE},
                {BinaryOperatorKind.NE, BinaryOperatorKind.EQ},
                {BinaryOperatorKind.LE, BinaryOperatorKind.GT},
                {BinaryOperatorKind.GT, BinaryOperatorKind.LE},
                {BinaryOperatorKind.MUL, BinaryOperatorKind.MUL}
        };

    }


    @Test
    public void MethodChangeOperatorTest() {
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

        ctIf.setThenStatement(ctStatement1);
        ctIf.setElseStatement(ctStatement2);


        ctBlock.addStatement(ctIf);
        ctMethod.setSimpleName("changeAndMethod");
        ctMethod.addModifier(ModifierKind.PUBLIC);

        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);

        ctMethod.setBody(ctBlock);
        changeAnd.addMethod(ctMethod);

        PowerMockito.mockStatic(Result.class);
        PowerMockito.doNothing().when(Result.class);

        for (int i = 0; i < binaryOperatorKind.length; i++) {
            int j = 0;
            condition.setKind(binaryOperatorKind[i][0]);
            ctIf.setCondition(condition);
            ((MethodChangeIfOperatorProcessor) process).process(changeAnd);
            List<CtClass> ctClassList = process.getCtClasses();


            for (CtClass c : ctClassList) {

                BinaryOperatorKind binary = ((CtBinaryOperator) ((CtIf) c.getMethod("changeAndMethod").getBody().getStatement(0)).getCondition()).getKind();
                Assert.assertTrue("Operator is " + binary + " expected " + binaryOperatorKind[i][j], binary == binaryOperatorKind[i][j]);
                j++;

            }

        }
    }


    @Test
    public void MethodChangeUnaryOperatorTest() {
        CtClass changeAnd = new CtClassImpl();
        changeAnd.setSimpleName("ChangeAnd");
        changeAnd.addModifier(ModifierKind.PUBLIC);
        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();
        CtIf ctIf = new CtIfImpl();
        CtStatement ctStatement1 = changeAnd.getFactory().createCodeSnippetStatement("System.out.println(\"test if\")");
        CtStatement ctStatement2 = changeAnd.getFactory().createCodeSnippetStatement("System.out.println(\"test else\")");
        CtUnaryOperator condition = new CtUnaryOperatorImpl();
        condition.setKind(UnaryOperatorKind.NOT);
        condition.setOperand(new CtLiteralImpl<Boolean>().setValue(false));
        ctMethod.setSimpleName("changeAndMethod");
        ctMethod.addModifier(ModifierKind.PUBLIC);
        ctIf.setCondition(condition);
        ctIf.setThenStatement(ctStatement1);
        ctIf.setElseStatement(ctStatement2);
        ctBlock.addStatement(ctIf);

        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);

        ctMethod.setBody(ctBlock);
        changeAnd.addMethod(ctMethod);

        PowerMockito.mockStatic(Result.class);
        PowerMockito.doNothing().when(Result.class);

        ((MethodChangeIfOperatorProcessor) process).process(changeAnd);
        for(CtClass c : process.getCtClasses()){
            Assert.assertTrue("Operator is not",((CtUnaryOperator) ((CtIf) c.getMethod("changeAndMethod").getBody().getStatement(0)).getCondition()).getKind().equals(UnaryOperatorKind.NOT));
        }
    }






}



