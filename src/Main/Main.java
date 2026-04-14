package Main;

import java.nio.file.Files;
import java.nio.file.Paths;
import interpreter.Interpreter;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Please provide file path");
                return;
            }

            String sourceCode = Files.readString(Paths.get(args[0]));
            Interpreter interpreter = new Interpreter();
            interpreter.run(sourceCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}