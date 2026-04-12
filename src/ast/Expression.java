package ast;
import interpreter.Environment;

public interface Expression {
    Object evaluate(Environment env);
}
