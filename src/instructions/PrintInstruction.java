package instructions;

import ast.Expression;
import interpreter.Environment;

public class PrintInstruction implements Instruction {
    private final Expression expression;

    public PrintInstruction(Expression expression) {
        this.expression = expression;
    }

    public void execute(Environment env) {
        Object value = expression.evaluate(env);
        if (value instanceof Double number && number == Math.rint(number)) {
            System.out.println((long) number.doubleValue());
            return;
        }
        System.out.println(value);
    }
}
