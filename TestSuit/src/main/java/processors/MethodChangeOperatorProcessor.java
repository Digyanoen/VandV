package processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLocalVariable;
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
                                BinaryOperatorKind newOp;

                                switch (binaryOperator){
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
                                    default: newOp = binaryOperator; // TODO trouver mieux
                                        break;
                                }
                                ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(newOp);
                                ctClassList.add(ctClassCloned.clone());
                                ((CtBinaryOperator) ((CtLocalVariable) s).getAssignment()).setKind(binaryOperator);

                            });
        });
    }


    public List<CtClass> getCtClassList() {
        return ctClassList;
    }

}
