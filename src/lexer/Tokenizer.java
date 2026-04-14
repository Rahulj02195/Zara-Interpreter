package lexer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private final String source;
    private int pos = 0;
    private int line = 1;
    private boolean startOfLine = true;
    private final List<Integer> indentStack = new ArrayList<>();

    public Tokenizer(String source) {
        this.source = source;
        indentStack.add(0);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            if (startOfLine) {
                handleIndentation(tokens);
                if (pos >= source.length()) {
                    break;
                }
            }

            char current = source.charAt(pos);

            if (current == '\r') {
                pos++;
                continue;
            }

            if (current == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                pos++;
                line++;
                startOfLine = true;
                continue;
            }

            if (current == ' ' || current == '\t') {
                pos++;
                continue;
            }

            if (Character.isDigit(current)) {
                tokens.add(readNumber());
                continue;
            }

            if (Character.isLetter(current)) {
                tokens.add(readIdentifier());
                continue;
            }

            switch (current) {
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", line));
                    pos++;
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", line));
                    pos++;
                    break;
                case '*':
                    tokens.add(new Token(TokenType.STAR, "*", line));
                    pos++;
                    break;
                case '/':
                    tokens.add(new Token(TokenType.SLASH, "/", line));
                    pos++;
                    break;
                case '>':
                    tokens.add(new Token(TokenType.GREATER, ">", line));
                    pos++;
                    break;
                case '<':
                    tokens.add(new Token(TokenType.LESS, "<", line));
                    pos++;
                    break;
                case ':':
                    tokens.add(new Token(TokenType.COLON, ":", line));
                    pos++;
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "(", line));
                    pos++;
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")", line));
                    pos++;
                    break;
                case '=':
                    if (peekNext() == '=') {
                        tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", line));
                        pos += 2;
                    } else {
                        tokens.add(new Token(TokenType.EQUAL, "=", line));
                        pos++;
                    }
                    break;
                case '"':
                    tokens.add(readString());
                    break;
                default:
                    throw new RuntimeException("Unexpected character '" + current + "' at line " + line);
            }
        }

        while (indentStack.size() > 1) {
            indentStack.remove(indentStack.size() - 1);
            tokens.add(new Token(TokenType.DEDENT, "", line));
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private void handleIndentation(List<Token> tokens) {
        int spaces = 0;

        while (pos < source.length()) {
            char current = source.charAt(pos);
            if (current == ' ') {
                spaces++;
                pos++;
            } else if (current == '\t') {
                spaces += 4;
                pos++;
            } else {
                break;
            }
        }

        if (pos < source.length() && source.charAt(pos) == '\n') {
            startOfLine = false;
            return;
        }

        int currentIndent = indentStack.get(indentStack.size() - 1);
        if (spaces > currentIndent) {
            indentStack.add(spaces);
            tokens.add(new Token(TokenType.INDENT, "", line));
        } else {
            while (spaces < indentStack.get(indentStack.size() - 1)) {
                indentStack.remove(indentStack.size() - 1);
                tokens.add(new Token(TokenType.DEDENT, "", line));
            }
            if (spaces != indentStack.get(indentStack.size() - 1)) {
                throw new RuntimeException("Invalid indentation at line " + line);
            }
        }

        startOfLine = false;
    }

    private Token readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && Character.isLetterOrDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }

        String word = sb.toString();
        if (word.equals("set")) {
            return new Token(TokenType.SET, word, line);
        }
        if (word.equals("show")) {
            return new Token(TokenType.SHOW, word, line);
        }
        if (word.equals("when")) {
            return new Token(TokenType.WHEN, word, line);
        }
        if (word.equals("loop")) {
            return new Token(TokenType.LOOP, word, line);
        }
        return new Token(TokenType.IDENTIFIER, word, line);
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
            sb.append(source.charAt(pos));
            pos++;
        }

        if (pos < source.length() && source.charAt(pos) == '.') {
            sb.append(source.charAt(pos));
            pos++;
            while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
                sb.append(source.charAt(pos));
                pos++;
            }
        }

        return new Token(TokenType.NUMBER, sb.toString(), line);
    }

    private Token readString() {
        StringBuilder sb = new StringBuilder();
        pos++;

        while (pos < source.length() && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') {
                throw new RuntimeException("Unterminated string at line " + line);
            }
            sb.append(source.charAt(pos));
            pos++;
        }

        if (pos >= source.length()) {
            throw new RuntimeException("Unterminated string at line " + line);
        }

        pos++;
        return new Token(TokenType.STRING, sb.toString(), line);
    }

    private char peekNext() {
        if (pos + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(pos + 1);
    }
}
