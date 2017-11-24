package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtIf;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodChangeIfOperatorProcessor extends AbstractProcessor<CtClass> {

    List<CtClass> ctClassList;

    public void process(CtClass ctClass) {
        ctClassList = new ArrayList<>();
        CtClass ctClassCloned = ctClass.clone();
        ctClassList.add(ctClass);
        ctClassCloned.getMethods().stream().forEach(m -> {
            ((CtMethod) m).getBody().getStatements().stream().filter(ctStatement -> ctStatement instanceof CtIf)
                    .forEach(
                            s -> {

                                BinaryOperatorKind binaryOperator = ((CtBinaryOperator) ((CtIf) s).getCondition()).getKind();

                                switch (binaryOperator){
                                    case AND:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.OR);
                                        ctClassList.add(ctClassCloned.clone());
                                         ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.AND);
                                        break;
                                    case OR:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.AND);
                                        ctClassList.add(ctClassCloned.clone());
                                         ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.OR);
                                        break;
                                    case EQ:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.NE);
                                        ctClassList.add(ctClassCloned.clone());
                                         ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.EQ);
                                        break;
                                    case NE:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.EQ);
                                        ctClassList.add(ctClassCloned.clone());
                                         ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.NE);
                                        break;
                                    case LE:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.GT);
                                        ctClassList.add(ctClassCloned.clone());
                                         ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.LE);
                                        break;
                                    case GT:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.LE);
                                        ctClassList.add(ctClassCloned.clone());
                                         ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.GT);
                                        break;

                                }
                            });
        });
    }


    public List<CtClass> getCtClassList() {
        return ctClassList;
    }

}
