package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodChangeOperatorProcessor extends AbstractProcessor<CtClass> {
    List<CtClass> ctClassList;

    public void process(CtClass ctClass) {
        ctClassList = new ArrayList<>();
        CtClass ctClassCloned = ctClass.clone();
        ctClassList.add(ctClass);
        ctClassCloned.getMethods().stream().forEach(m -> {
            ((CtMethod) m).getBody().getStatements().stream().filter(ctStatement -> ctStatement instanceof CtLocalVariable)
                    .forEach(
                            s -> {

                                BinaryOperatorKind binaryOperator = ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).getKind();

                                switch (binaryOperator) {
                                    case PLUS:
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.MINUS);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.PLUS);
                                        break;
                                    case MINUS:
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.PLUS);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.MINUS);
                                        break;
                                    case MUL:
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.DIV);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.MINUS);
                                        break;
                                    case DIV:
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.MUL);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.DIV);
                                        break;
                                    case MOD:((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.MUL);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(BinaryOperatorKind.MUL);
                                        break;

                                }
                            });
        });
    }

    public List<CtClass> getCtClassList() {
        return ctClassList;
    }
}
