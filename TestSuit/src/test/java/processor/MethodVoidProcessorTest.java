package processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import processors.MethodVoidProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtReturnImpl;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MethodVoidProcessorTest{


    private static MethodVoidProcessor methodVoidProcessor;
    private static CtClass removeClass;

    @BeforeClass
    public static void setUp(){
        removeClass = new CtClassImpl();
        methodVoidProcessor = new MethodVoidProcessor();
        removeClass.setSimpleName("RemoveBody");
        removeClass.addModifier(ModifierKind.PUBLIC);

        CtMethod ctMethodNotVoid = new CtMethodImpl();
        CtBlock ctBlockNotVoid = new CtBlockImpl();
        CtStatement ctStatementNotVoid = removeClass.getFactory().createCodeSnippetStatement("System.out.println(\"body of the method\")");
        CtReturn returnStatement = new CtReturnImpl<Integer>();

        returnStatement.setReturnedExpression(new CtLiteralImpl().setValue(2));
        ctBlockNotVoid.addStatement(ctStatementNotVoid);
        ctBlockNotVoid.addStatement(returnStatement);
        ctMethodNotVoid.setBody(ctBlockNotVoid);
        ctMethodNotVoid.setSimpleName("bodyNotRemovedMethod");

        ctMethodNotVoid.addModifier(ModifierKind.PUBLIC);
        CtTypeReference ctTypeReferenceNotVoid = new CtTypeReferenceImpl();
        ctTypeReferenceNotVoid.setSimpleName("int");
        ctMethodNotVoid.setType(ctTypeReferenceNotVoid);

        CtMethod ctMethod = new CtMethodImpl();
        CtBlock ctBlock = new CtBlockImpl();
        CtStatement ctStatement = removeClass.getFactory().createCodeSnippetStatement("System.out.println(\"body of the method\")");

        ctBlock.addStatement(ctStatement);
        ctMethod.setBody(ctBlock);
        ctMethod.setSimpleName("removeBodyMethod");

        ctMethod.addModifier(ModifierKind.PUBLIC);
        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);
        List<CtTypeMember> methods = new LinkedList<>();
        methods.add(ctMethod);
        methods.add(ctMethodNotVoid);
        removeClass.setTypeMembers(methods);


    }

    @Test
    public void removeBodyTest(){
        methodVoidProcessor.process(removeClass);
        for (CtClass c : methodVoidProcessor.getCtClasses().subList(1, methodVoidProcessor.getCtClasses().size())){
            Assert.assertTrue("Body must be null", c.getMethod("removeBodyMethod").getBody() == null);
        }

    }

    @Test
    public void bodyNotRemovedTest() {

        methodVoidProcessor.process(removeClass);
        for (CtClass c : methodVoidProcessor.getCtClasses()) {
            Assert.assertTrue("Body must not be null", c.getMethod("bodyNotRemovedMethod").getBody() != null);

        }
    }
}
