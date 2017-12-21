package processors;

import Testing.Result;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor which changes arithmetic operators
 */
public class MethodChangeOperatorProcessor extends MyProcess{

    private List<CtClass> ctClassList;

    public void process(CtClass ctClass) {
        ctClassList = new ArrayList<>();
        CtClass ctClassCloned = ctClass.clone();
        ctClassList.add(ctClass);
        ctClassCloned.getMethods().forEach(m -> {
            CtBlock methods = ((CtMethod) m).getBody();
            if(methods != null) {
                methods.getStatements().stream().filter(ctStatement -> ctStatement instanceof CtLocalVariable)
                        .forEach(
                                s -> {
                                    if (!((((CtLocalVariable) s).getAssignment()) instanceof CtBinaryOperator)) return;



                                    BinaryOperatorKind binaryOperator = ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).getKind();
                                    BinaryOperatorKind newOp;

                                    switch (binaryOperator) {
                                        case PLUS:
                                            newOp = BinaryOperatorKind.MINUS;
                                            break;
                                        case MINUS:
                                            newOp = BinaryOperatorKind.PLUS;
                                            break;
                                        case MUL:
                                            newOp = BinaryOperatorKind.DIV;
                                            break;
                                        case DIV:
                                        case MOD:
                                            newOp = BinaryOperatorKind.MUL;
                                            break;
                                        default:
                                            newOp = binaryOperator; // TODO trouver mieux

                                            break;
                                    }
                                    ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(newOp);
                                    ctClass.replace(ctClassCloned);
                                    Result.showResults(new Mutant(ctClass.getSimpleName(), (CtMethod) m, s, "ChangeArithmeticOperator",
                                            s.getPosition().getLine()));
                                    ctClassList.add(ctClassCloned.clone());
                                    ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(binaryOperator);

                                });
            }


        });
    }


    @Override
    public List<CtClass> getCtClasses() {
        return ctClassList;
    }
}
