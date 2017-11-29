package processors;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

public class Mutant {

    private CtMethod method;
    private CtStatement statement;
    private String mutantName;
    private int line;


    public Mutant(CtMethod method, CtStatement statement, String mutantName, int line) {
        this.method = method;
        this.statement = statement;
        this.mutantName = mutantName + "Mutant";
        this.line = line;
    }


    public CtMethod getMethod() {
        return method;
    }

    public void setMethod(CtMethod method) {
        this.method = method;
    }

    public CtStatement getStatement() {
        return statement;
    }

    public void setStatement(CtStatement statement) {
        this.statement = statement;
    }

    public String getMutantName() {
        return mutantName;
    }

    public void setMutantName(String mutantName) {
        this.mutantName = mutantName;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
