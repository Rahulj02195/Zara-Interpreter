package instructions;


import java.util.List;

import interpreter.Environment;

public class RepeatInstruction implements Instruction{
    private final int count;
    private final List<Instruction> body;

    public RepeatInstruction(int count,List<Instruction> body) {
        this.count=count;
        this.body = body;
    }

    public void execute(Environment env) {
        for (int i=0;i<count; i++) {
            for(Instruction instr : body) {
                instr.execute(env);
            }
        }

    }

}
