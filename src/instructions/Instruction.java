package instructions;

import interpreter.Environment;

public interface Instruction {
    void execute(Environment env);
}

