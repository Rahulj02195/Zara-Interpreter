package parser;

import ast.BinaryOpNode;
import ast.Expression;
import ast.NumberNode;
import ast.StringNode;
import ast.VariableNode;
import lexer.TokenType;

public class ExpressionParser {

    private final TokenStream ts;

    public ExpressionParser(TokenStream ts) {
        this.ts = ts;
    }

    public Expression parseComparison() {
        Expression left = parseExpression();

        while (ts.match(TokenType.GREATER) || ts.match(TokenType.LESS) || ts.match(TokenType.EQUAL_EQUAL)) {
            String operator = ts.previous().getValue();
            Expression right = parseExpression();
            left = new BinaryOpNode(left, operator, right);
        }

        return left;
    }

    public Expression parseExpression() {
        Expression left = parseTerm();

        while (ts.match(TokenType.PLUS) || ts.match(TokenType.MINUS)) {
            String operator = ts.previous().getValue();
            Expression right = parseTerm();
            left = new BinaryOpNode(left, operator, right);
        }

        return left;
    }

    public Expression parseTerm() {
        Expression left = parsePrimary();

        while (ts.match(TokenType.STAR) || ts.match(TokenType.SLASH)) {
            String operator = ts.previous().getValue();
            Expression right = parsePrimary();
            left = new BinaryOpNode(left, operator, right);
        }

        return left;
    }

    public Expression parsePrimary() {

        if (ts.match(TokenType.MINUS)) {
            Expression right = parsePrimary();
            return new BinaryOpNode(new NumberNode(0), "-", right);
        }

        if (ts.match(TokenType.LPAREN)) {
            Expression expr = parseComparison();
            ts.consume(TokenType.RPAREN, "Expected ')' after expression");
            return expr;
        }

        if (ts.match(TokenType.NUMBER)) {
            return new NumberNode(Double.parseDouble(ts.previous().getValue()));
        }

        if (ts.match(TokenType.STRING)) {
            return new StringNode(ts.previous().getValue());
        }

        if (ts.match(TokenType.IDENTIFIER)) {
            return new VariableNode(ts.previous().getValue());
        }

        throw new RuntimeException("Expected expression at line " + ts.peek().getLine());
    }
}