import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtExpressionImpl;

public class MethodChangeOperatorProcessor extends AbstractProcessor<CtClass> {
    @Override
    public void process(CtClass ctClass) {
        ctClass.getMethods().stream().forEach(m -> {
            ((CtMethod) m).getBody().getStatements().stream().filter(ctStatement -> ctStatement instanceof CtIf)
                    .forEach(
                            s -> {

                                BinaryOperatorKind binaryOperator = ((CtBinaryOperator) ((CtIf) s).getCondition()).getKind();

                                switch (binaryOperator){
                                    case AND:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.OR);
                                        break;
                                    case OR:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.AND);
                                        break;
                                    case EQ:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.NE);
                                        break;
                                    case NE:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.EQ);
                                        break;
                                    case LE:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.GT);
                                        break;
                                    case GT:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.LE);
                                        break;
                                    case PLUS:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.MINUS);
                                        break;
                                    case MINUS:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.PLUS);
                                        break;
                                    case MUL:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.DIV);
                                        break;
                                    case DIV:
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(BinaryOperatorKind.MUL);
                                        break;

                                }
                            });
        });
    }
}
