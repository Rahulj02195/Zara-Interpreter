package lexer;

public enum TokenType {
   SET,
   SHOW,
   WHEN,
   LOOP,
   NUMBER,
   STRING,
   IDENTIFIER,
   PLUS,
   MINUS,
   STAR,
   SLASH,
   GREATER,
   LESS,
   EQUAL_EQUAL,
   EQUAL,
   COLON,
   LPAREN,
   RPAREN,
   NEWLINE,
   INDENT,
   DEDENT,
   EOF;

   private TokenType() {
   }
}
