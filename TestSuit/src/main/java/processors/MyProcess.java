package processors;

import Testing.Result;
import spoon.processing.AbstractProcessor;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtClass;

import java.util.List;

/**
 * Abstract class for processors
 */
public abstract class MyProcess extends AbstractProcessor<CtClass> {
    /**
     *
     * @return the list of the modified class
     */
    public abstract List<CtClass> getCtClasses();

}
