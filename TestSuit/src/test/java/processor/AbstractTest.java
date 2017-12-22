package processor;

import org.junit.Assert;
import org.junit.Test;
import processors.MyProcess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtMethodImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

public abstract class AbstractTest {

    protected MyProcess process;

    /**
     * Test the behaviour of an interface or an abstract class
     */
    @Test
    public void methodNullTest(){
        CtClass ctClass = new CtClassImpl();
        ctClass.setSimpleName("AbstractClass");
        ctClass.addModifier(ModifierKind.ABSTRACT);
        ctClass.addModifier(ModifierKind.PUBLIC);

        CtMethod ctMethod = new CtMethodImpl();
        CtMethod ctMethod2 = new CtMethodImpl();
        CtMethod ctMethod3 = new CtMethodImpl();

        ctMethod.setSimpleName("methodOfanAbstractClass");
        ctMethod2.setSimpleName("otherMethodOfAnAbstractClass");
        ctMethod3.setSimpleName("thirdMethodOfAnAbstractClass");

        ctMethod.addModifier(ModifierKind.PUBLIC);
        ctMethod2.addModifier(ModifierKind.PUBLIC);
        ctMethod3.addModifier(ModifierKind.PUBLIC);

        CtTypeReference ctTypeReference = new CtTypeReferenceImpl();
        ctTypeReference.setSimpleName("void");
        ctMethod.setType(ctTypeReference);
        ctMethod2.setType(ctTypeReference);
        ctMethod3.setType(ctTypeReference);



        ctClass.addTypeMember(ctMethod);
        ctClass.addTypeMember(ctMethod2);
        ctClass.addTypeMember(ctMethod3);

        process.process(ctClass);
        Assert.assertTrue("size list must be 1", process.getCtClasses().size() == 1);

    }
}
