package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtIf;
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
            ((CtMethod) m).getBody().getStatements().stream().filter(ctStatement -> ctStatement instanceof CtIf)
                    .forEach(
                            s -> {

                                BinaryOperatorKind binaryOperator = ((CtBinaryOperator) ((CtIf) s).getCondition()).getKind();

                                BinaryOperatorKind nouv;

                                switch (binaryOperator){
                                    case AND:
                                        nouv = BinaryOperatorKind.OR;
                                        break;
                                    case OR:
                                        nouv = BinaryOperatorKind.AND;
                                        break;
                                    case EQ:
                                        nouv = BinaryOperatorKind.NE;
                                        break;
                                    case NE:
                                        nouv = BinaryOperatorKind.EQ;
                                        break;
                                    case LE:
                                        nouv = BinaryOperatorKind.GT;
                                        break;
                                    case GT:
                                        nouv = BinaryOperatorKind.LE;
                                        break;
                                    case PLUS:
                                        nouv = BinaryOperatorKind.MINUS;
                                        break;
                                    case MINUS:
                                        nouv = BinaryOperatorKind.PLUS;
                                        break;
                                    case MUL:
                                        nouv = BinaryOperatorKind.DIV;
                                        break;
                                    case DIV:
                                        nouv = BinaryOperatorKind.MUL;
                                        break;
                                    default: nouv = binaryOperator; // TODO trouver mieux
                                        break;
                                }
                                ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(nouv);
                                ctClassList.add(ctClassCloned.clone());
                                ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(binaryOperator);
                            });
        });
    }


    public List<CtClass> getCtClassList() {
        return ctClassList;
    }

}
