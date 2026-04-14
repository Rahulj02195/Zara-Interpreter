package parser;

import java.util.List;

import lexer.Token;
import lexer.TokenType;

public class TokenStream {

    private final List<Token> tokens;
    private int pos = 0;

    public TokenStream(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    public Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw new RuntimeException(message + " at line " + peek().getLine());
    }

    public boolean check(TokenType type) {
        if (isAtEnd()) {
            return type == TokenType.EOF;
        }
        return peek().getType() == type;
    }

    public Token advance() {
        if (!isAtEnd()) {
            pos++;
        }
        return tokens.get(pos - 1);
    }

    public Token peek() {
        return tokens.get(pos);
    }

    public Token previous() {
        return tokens.get(pos - 1);
    }

    public boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    public void skipNewlines() {
        while (match(TokenType.NEWLINE)) {
            
        }
    }
}