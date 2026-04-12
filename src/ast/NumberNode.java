package ast;

import interpreter.Environment;

public class NumberNode implements Expression{
    private final double number;

    public NumberNode(double number) {
        this.number= number;
    }

    public Object evaluate(Environment env) {
        return number;
    }
}

