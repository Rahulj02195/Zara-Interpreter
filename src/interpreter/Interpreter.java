package interpreter;



import java.util.List;

import instructions.Instruction;
import lexer.Token;
import lexer.Tokenizer;
import parser.Parser;

public class Interpreter {

    public void run(String sourceCode) {
        
        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token> tokens = tokenizer.tokenize();

       
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();


        Environment env = new Environment();
        for (Instruction instruction : instructions) {
            instruction.execute(env);
        }
    }
}
