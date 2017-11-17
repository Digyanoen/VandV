import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.util.*;

public class MethodChangeOperatorProcessorTest {

    private MethodChangeOperatorProcessor methodChangeOperatorProcessor;

    public BinaryOperatorKind[][] binaryOperatorKind;




    @Before
    public void init(){
        methodChangeOperatorProcessor = new MethodChangeOperatorProcessor();
        binaryOperatorKind = new BinaryOperatorKind[][] {
                {BinaryOperatorKind.AND, BinaryOperatorKind.OR},
                {BinaryOperatorKind.PLUS, BinaryOperatorKind.MINUS},
                {BinaryOperatorKind.OR, BinaryOperatorKind.AND},
                {BinaryOperatorKind.EQ, BinaryOperatorKind.NE},
                {BinaryOperatorKind.LE, BinaryOperatorKind.GT},
                {BinaryOperatorKind.GT, BinaryOperatorKind.LE},
                {BinaryOperatorKind.MUL, BinaryOperatorKind.DIV},
                {BinaryOperatorKind.DIV, BinaryOperatorKind.MUL}
        };

    }




    @Test
    public void MethodChangeOperatorAndTest() {
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

        for (int i = 0; i < binaryOperatorKind.length; i++) {
            int j=0;
            condition.setKind(binaryOperatorKind[i][0]);
            ctIf.setCondition(condition);
            methodChangeOperatorProcessor.process(changeAnd);
            List<CtClass> ctClassList = methodChangeOperatorProcessor.getCtClassList();


                for (CtClass c : ctClassList) {

                    BinaryOperatorKind binary = ((CtBinaryOperator) ((CtIf) c.getMethod("changeAndMethod").getBody().getStatement(0)).getCondition()).getKind();
                    System.out.println(c);
                    Assert.assertTrue("Operator is " + binary + " expected " + binaryOperatorKind[i][j], binary == binaryOperatorKind[i][j]);
                    j++;

                }

        }
    }





    @Test
    public void MethodChangeOperatorORTest(){
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
        condition.setKind(BinaryOperatorKind.OR);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected AND", condition.getKind() == BinaryOperatorKind.AND);

    }
    @Test
    public void MethodChangeOperatorEQTest(){
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
        condition.setKind(BinaryOperatorKind.EQ);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected NE", condition.getKind() == BinaryOperatorKind.NE);

    }
    @Test
    public void MethodChangeOperatorNEdTest(){
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
        condition.setKind(BinaryOperatorKind.NE);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected EQ", condition.getKind() == BinaryOperatorKind.EQ);

    }
    @Test
    public void MethodChangeOperatorLETest(){
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
        condition.setKind(BinaryOperatorKind.LE);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected GT", condition.getKind() == BinaryOperatorKind.GT);

    }
    @Test
    public void MethodChangeOperatorGTTest(){
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
        condition.setKind(BinaryOperatorKind.GT);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected LE", condition.getKind() == BinaryOperatorKind.LE);

    }
    @Test
    public void MethodChangeOperatorPLUSTest(){
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
        condition.setKind(BinaryOperatorKind.PLUS);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected MINUS", condition.getKind() == BinaryOperatorKind.MINUS);

    }
    @Test
    public void MethodChangeOperatorMINUSTest(){
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
        condition.setKind(BinaryOperatorKind.MINUS);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected PLUS", condition.getKind() == BinaryOperatorKind.PLUS);

    }
    @Test
    public void MethodChangeOperatorMULTest(){
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
        condition.setKind(BinaryOperatorKind.MUL);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected DIV", condition.getKind() == BinaryOperatorKind.DIV);

    }
    @Test
    public void MethodChangeOperatorDIVTest(){
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
        condition.setKind(BinaryOperatorKind.DIV);


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

        Assert.assertTrue("Operator is "+ condition.getKind()+" expected MUL", condition.getKind() == BinaryOperatorKind.MUL);

    }
}
