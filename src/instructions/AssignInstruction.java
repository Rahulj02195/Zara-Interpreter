package instructions;

import ast.Expression;
import interpreter.Environment;

public class AssignInstruction implements Instruction{
    private final String name;
    private final Expression expression;


    public AssignInstruction(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }


    public void execute(Environment env) {
        Object value= expression.evaluate(env);
        env.set(name, value);
    }

}

