package processors;

import Testing.Result;
import Testing.TestUnitHandler;
import org.eclipse.jdt.internal.core.SourceType;
import org.junit.runner.notification.Failure;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtUnaryOperatorImpl;

import java.util.ArrayList;
import java.util.List;


public class MethodChangeIfOperatorProcessor extends AbstractProcessor<CtClass> {

    private List<CtClass> ctClassList;

    public void process(CtClass ctClass) {
        ctClassList = new ArrayList<>();
        CtClass ctClassCloned = ctClass.clone();
        ctClassList.add(ctClass);
        ctClassCloned.getMethods().stream().forEach(m -> {
            ((CtMethod) m).getBody().getStatements().stream().filter(ctStatement -> ctStatement instanceof CtIf)
                    .forEach(
                            s -> {
                                if( !(((CtIf) s).getCondition() instanceof CtBinaryOperator)) {
                                    CtUnaryOperator operator = new CtUnaryOperatorImpl();
                                    CtUnaryOperator oldOp =(CtUnaryOperator) ((CtIf) s).getCondition();
                                    operator.setOperand(oldOp);
                                    operator.setKind(UnaryOperatorKind.NOT);
                                    ((CtIf) s).setCondition(operator);
                                    //TODO replace
                                    ctClassList.add(ctClassCloned.clone());
                                    ((CtIf) s).setCondition(oldOp);


                                }else  if( (((CtIf) s).getCondition() instanceof CtBinaryOperator)) {

                                    BinaryOperatorKind binaryOperator = ((CtBinaryOperator) ((CtIf) s).getCondition()).getKind();
                                    BinaryOperatorKind newOp;

                                    switch (binaryOperator) {
                                        case AND:
                                            newOp = BinaryOperatorKind.OR;
                                            break;
                                        case OR:
                                            newOp = BinaryOperatorKind.AND;
                                            break;
                                        case EQ:
                                            newOp = BinaryOperatorKind.NE;
                                            break;
                                        case NE:
                                            newOp = BinaryOperatorKind.EQ;
                                            break;
                                        case LE:
                                            newOp = BinaryOperatorKind.GT;
                                            break;
                                        case GT:
                                            newOp = BinaryOperatorKind.LE;
                                            break;
                                        default:
                                            newOp = binaryOperator;
                                            break;

                                    }
                                    ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(newOp);
                                    ctClassList.add(ctClassCloned.clone());
                                    ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(binaryOperator);
                                }

                               // Result.showResults();

                            });
        });
    }


    public List<CtClass> getCtClassList() {
        return ctClassList;
    }

}
