package instructions;



import java.util.List;

import ast.Expression;
import interpreter.Environment;

public class IfInstruction implements Instruction {

    private final Expression condition;
    private final List<Instruction> body;

    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {

        Object result = condition.evaluate(env);

        if ((Boolean)result) {
            for (Instruction instr : body) {
                instr.execute(env);
            }
        }
    }
}
