package processor;

import Testing.Result;
import Testing.TestUnitHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import processors.MethodBooleanProcessor;
import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.support.reflect.code.*;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Result.class)
public class MethodBooleanProcessorTest {


    private static MethodBooleanProcessor methodBooleanProcessor;

    private static CtClass booleanClass;

    private static Launcher launcher;

    @BeforeClass
    /**
     * set up the data
     */
    public static void setup(){
        methodBooleanProcessor = new MethodBooleanProcessor();
        booleanClass = new CtClassImpl();
        booleanClass.setSimpleName("BooleanClass");
        booleanClass.addModifier(ModifierKind.PUBLIC);

        CtMethod booleanMethod = new CtMethodImpl();
        CtIf conditional=  new CtIfImpl();
        CtBinaryOperator binaryOperator= new CtBinaryOperatorImpl<Boolean>();
        binaryOperator.setKind(BinaryOperatorKind.LT);
        binaryOperator.setLeftHandOperand(new CtLiteralImpl<Integer>().setValue(2));
        binaryOperator.setRightHandOperand(new CtLiteralImpl<Integer>().setValue(3));
        CtBlock body = new CtBlockImpl();

        conditional.setCondition(binaryOperator);

        CtReturnImpl ctReturnTrue = new CtReturnImpl();
        CtReturnImpl ctReturnFalse = new CtReturnImpl();
        ctReturnTrue.setReturnedExpression(new CtLiteralImpl<Boolean>().setValue(true));
        ctReturnFalse.setReturnedExpression(new CtLiteralImpl<Boolean>().setValue(false));

        conditional.setThenStatement(ctReturnTrue);
        conditional.setElseStatement(ctReturnFalse);

        body.addStatement(conditional);
        booleanMethod.setBody(body);
        booleanMethod.addModifier(ModifierKind.PUBLIC);
        booleanMethod.setType(new CtTypeReferenceImpl().setSimpleName("boolean"));
        booleanMethod.setSimpleName("isSuperior");
        booleanClass.addTypeMember(booleanMethod);
        launcher = new Launcher();
    }

    


    @Test
    public void processBooleanTest(){
        launcher.addProcessor(methodBooleanProcessor);
        PowerMockito.mockStatic(Result.class);
        PowerMockito.doNothing().when(Result.class);

        methodBooleanProcessor.process(booleanClass);

        CtReturnImpl ctReturnTrue = new CtReturnImpl();
        CtReturnImpl ctReturnFalse = new CtReturnImpl();
        ctReturnTrue.setReturnedExpression(new CtLiteralImpl<Boolean>().setValue(true));
        ctReturnFalse.setReturnedExpression(new CtLiteralImpl<Boolean>().setValue(false));
        Assert.assertTrue("Classes must be the same", methodBooleanProcessor.getCtClasses().get(0).equals(booleanClass));
        Assert.assertTrue("Body must be a return statement", methodBooleanProcessor.getCtClasses().get(1).getMethod("isSuperior").getBody().getStatement(0).equals(ctReturnTrue));
        Assert.assertTrue("Body must be a return statement", methodBooleanProcessor.getCtClasses().get(2).getMethod("isSuperior").getBody().getStatement(0).equals(ctReturnFalse));
    }
}
