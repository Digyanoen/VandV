package processors;

import net.sourceforge.cobertura.CoverageIgnore;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

public class Mutant {

    private String className;
    private CtMethod method;
    private CtStatement statement;
    private String mutantName;
    private int line;


    @CoverageIgnore
    public Mutant(String simpleName, CtMethod method, CtStatement statement, String mutantName, int line) {
        this.method = method;
        this.statement = statement;
        this.mutantName = mutantName + "Mutant";
        this.line = line;
        this.className = simpleName;
    }

    @CoverageIgnore
    public CtMethod getMethod() {
        return method;
    }
    @CoverageIgnore
    public void setMethod(CtMethod method) {
        this.method = method;
    }
    @CoverageIgnore
    public CtStatement getStatement() {
        return statement;
    }
    @CoverageIgnore
    public void setStatement(CtStatement statement) {
        this.statement = statement;
    }
    @CoverageIgnore
    public String getMutantName() {
        return mutantName;
    }
    @CoverageIgnore
    public void setMutantName(String mutantName) {
        this.mutantName = mutantName;
    }
    @CoverageIgnore
    public int getLine() {
        return line;
    }
    @CoverageIgnore
    public void setLine(int line) {
        this.line = line;
    }
    @CoverageIgnore
    public String getClassName() {
        return className;
    }
    @CoverageIgnore
    public void setClassName(String className) {
        this.className = className;
    }
}
