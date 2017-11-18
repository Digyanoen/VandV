package processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import processors.MethodVoidProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtReturnImpl;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

public class MethodVoidProcessorTest {


    private MethodVoidProcessor methodVoidProcessor;

    @Before
    public void init(){
        this.methodVoidProcessor = new MethodVoidProcessor();
    }

    @Test
    public void removeBodyTest(){
        CtClass removeBody = new CtClassImpl();
        removeBody.setSimpleName("RemoveBody");
        removeBody.addModifier(ModifierKind.PUBLIC);

        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();
        CtStatement ctStatement = removeBody.getFactory().createCodeSnippetStatement("System.out.println(\"body of the method\")");

        ctBlock.addStatement(ctStatement);
        ctMethod.setBody(ctBlock);
        ctMethod.setSimpleName("removeBodyMethod");

        ctMethod.addModifier(ModifierKind.PUBLIC);
        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);
        removeBody.addMethod(ctMethod);

        methodVoidProcessor.process(removeBody);

        Assert.assertTrue("Body must be null", removeBody.getMethod("removeBodyMethod").getBody() == null);

    }

    @Test
    public void bodyNotRemovedTest(){
        CtClass removeBody = new CtClassImpl();
        removeBody.setSimpleName("RemoveBody");
        removeBody.addModifier(ModifierKind.PUBLIC);

        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();
        CtStatement ctStatement = removeBody.getFactory().createCodeSnippetStatement("System.out.println(\"body of the method\")");
        CtReturn returnStatement = new CtReturnImpl<Integer>();

        returnStatement.setReturnedExpression(new CtLiteralImpl().setValue(2));
        ctBlock.addStatement(ctStatement);
        ctBlock.addStatement(returnStatement);
        ctMethod.setBody(ctBlock);
        ctMethod.setSimpleName("bodyNotRemovedMethod");

        ctMethod.addModifier(ModifierKind.PUBLIC);
        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("int");
        ctMethod.setType(ctTypeReference);
        removeBody.addMethod(ctMethod);

        methodVoidProcessor.process(removeBody);

        Assert.assertTrue("Body must not be null", removeBody.getMethod("bodyNotRemovedMethod").getBody() != null);

    }
}
