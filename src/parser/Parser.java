package parser;

import java.util.ArrayList;
import java.util.List;

import ast.Expression;
import instructions.AssignInstruction;
import instructions.IfInstruction;
import instructions.Instruction;
import instructions.PrintInstruction;
import instructions.RepeatInstruction;
import lexer.Token;
import lexer.TokenType;

public class Parser {

    private final TokenStream ts;
    private final ExpressionParser expressionParser;

    public Parser(List<Token> tokens) {
        this.ts = new TokenStream(tokens);
        this.expressionParser = new ExpressionParser(ts);
    }

    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();
        ts.skipNewlines();

        while (!ts.isAtEnd()) {
            if (ts.match(TokenType.DEDENT)) {
                continue;
            }
            instructions.add(parseInstruction());
            ts.skipNewlines();
        }

        return instructions;
    }

    private Instruction parseInstruction() {


        if (ts.match(TokenType.SET)) {
            Token name = ts.consume(TokenType.IDENTIFIER, "Expected variable name");
            ts.consume(TokenType.EQUAL, "Expected '=' after variable name");
            Expression expr = expressionParser.parseComparison();
            return new AssignInstruction(name.getValue(), expr);
        }

    
        if (ts.match(TokenType.SHOW)) {
            Expression expr = expressionParser.parseComparison();
            return new PrintInstruction(expr);
        }


        if (ts.match(TokenType.WHEN)) {
            Expression condition = expressionParser.parseComparison();
            ts.consume(TokenType.COLON, "Expected ':' after condition");
            ts.consume(TokenType.NEWLINE, "Expected newline after ':'");
            ts.consume(TokenType.INDENT, "Expected indented block");
            List<Instruction> body = parseBlock();
            return new IfInstruction(condition, body);
        }

        if (ts.match(TokenType.LOOP)) {
            Token countToken = ts.consume(TokenType.NUMBER, "Expected repeat count");
            ts.consume(TokenType.COLON, "Expected ':' after repeat count");
            ts.consume(TokenType.NEWLINE, "Expected newline after ':'");
            ts.consume(TokenType.INDENT, "Expected indented block");
            List<Instruction> body = parseBlock();
            int count = (int) Double.parseDouble(countToken.getValue());
            return new RepeatInstruction(count, body);
        }

        throw new RuntimeException("Unexpected token: " + ts.peek().getValue() + " at line " + ts.peek().getLine());
    }

    private List<Instruction> parseBlock() {
        List<Instruction> body = new ArrayList<>();
        ts.skipNewlines();

        while (!ts.check(TokenType.DEDENT) && !ts.isAtEnd()) {
            body.add(parseInstruction());
            ts.skipNewlines();
        }

        ts.consume(TokenType.DEDENT, "Expected end of block");
        return body;
    }
}