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
                                BinaryOperatorKind newOp;

                                switch (binaryOperator){
                                    case AND:
                                        newOp = BinaryOperatorKind.OR;
                                        break;
                                    case OR:
                                        newOp =BinaryOperatorKind.AND;
                                        break;
                                    case EQ:
                                        newOp =BinaryOperatorKind.NE;
                                        break;
                                    case NE:
                                        newOp =BinaryOperatorKind.EQ;
                                        break;
                                    case LE:
                                        newOp =BinaryOperatorKind.GT;
                                        break;
                                    case GT:
                                        newOp =BinaryOperatorKind.LE;
                                        break;
                                    default:
                                        newOp = binaryOperator;
                                        break;

                                }
                                ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(newOp);
                                ctClassList.add(ctClassCloned.clone());
                                ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(binaryOperator);
                            });
        });
    }


    public List<CtClass> getCtClassList() {
        return ctClassList;
    }

}
