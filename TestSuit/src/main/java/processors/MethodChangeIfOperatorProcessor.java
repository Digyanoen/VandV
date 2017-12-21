package processors;

import Testing.Result;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtUnaryOperatorImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor which changes boolean operators in an if statement
 */
public class MethodChangeIfOperatorProcessor extends MyProcess {

    private List<CtClass> ctClassList;

    /**
     * For a given class, the processor will retrieves its methods
     * For each method, the process look for if statements
     * For each if statement, the processor will change the boolean operator
     * Then, the processor give the mutated class to the Result, in order to write the effects of this mutation
     * @param ctClass, the class to process
     */
    @Override
    public void process(CtClass ctClass) {
        ctClassList = new ArrayList<>();
        CtClass ctClassCloned = ctClass.clone();
        ctClassList.add(ctClass);
        ctClassCloned.getMethods().forEach(m -> {
            CtBlock methods = ((CtMethod) m).getBody();
            if(methods != null) {
                ((CtMethod) m).getBody().getStatements().stream().filter(ctStatement -> ctStatement instanceof CtIf)
                        .forEach(
                                s -> {
                                    Mutant mutant;
                                    if ((((CtIf) s).getCondition() instanceof CtUnaryOperator)) {
                                        CtUnaryOperator operator = new CtUnaryOperatorImpl();
                                        CtUnaryOperator oldOp = (CtUnaryOperator) ((CtIf) s).getCondition();
                                        operator.setOperand(oldOp);
                                        operator.setKind(UnaryOperatorKind.NOT);
                                        ((CtIf) s).setCondition(operator);
                                        //TODO replace
                                        ctClass.replace(ctClassCloned);
                                        mutant =new Mutant(ctClass.getSimpleName(), (CtMethod) m, s, "ChangeBooleanOperator", s.getPosition().getLine());
                                        Result.showResults(mutant);
                                        ctClassCloned.replace(ctClass);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtIf) s).setCondition(oldOp);


                                    } else if ((((CtIf) s).getCondition() instanceof CtBinaryOperator)) {

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
                                        //ctClass.replace(ctClassCloned);

                                        mutant =new Mutant(ctClass.getSimpleName(), (CtMethod) m, s, "ChangeBooleanOperator", s.getPosition().getLine());
                                        Result.showResults(mutant);
                                        //ctClassCloned.replace(ctClass);
                                        ctClassList.add(ctClassCloned.clone());
                                        ((CtBinaryOperator) ((CtIf) s).getCondition()).setKind(binaryOperator);
                                    }


                                });
            }
        });
    }


    @Override
    public List<CtClass> getCtClasses() {
        return ctClassList;
    }
}
