package processors;

import spoon.processing.AbstractProcessor;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtClass;

import java.util.List;

public abstract class MyProcess extends AbstractProcessor<CtClass> {
    public abstract List<CtClass> getCtClasses();

}