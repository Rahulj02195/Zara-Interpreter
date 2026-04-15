# Zara Interpreter
A mini interpreter for the **Zara scripting language**, built entirely in pure Java.
Zara is a clean, minimal scripting language — think Python, but even simpler.

---

## Team Members
- Ayush Yadav
- Kuldeep Saraswat
- Rahul Kumar Verma

---

## What is Zara?
Zara is a programming language that supports variables, arithmetic, conditionals, and loops.
You write `.zara` source files, feed them to this interpreter, and see real output on screen.

```
set x = 10
set y = 3
set result = x + y * 2
show result
```
```
Output: 16
```

---

## How the Interpreter Works
The interpreter is a pipeline of three steps:

```
Source Code  →  Tokenizer  →  Parser  →  Interpreter  →  Output
```

| Step | Class | What it does |
|---|---|---|
| 1 | `Tokenizer` | Reads source code character by character and breaks it into tokens |
| 2 | `Parser` | Reads tokens and builds a list of instructions |
| 3 | `Interpreter` | Executes each instruction and produces output |

---

## Zara Syntax

| Feature | Syntax | Example |
|---|---|---|
| Assign variable | `set <name> = <expr>` | `set x = 10` |
| Print | `show <expr>` | `show x` |
| Conditional | `when <condition>:` | `when x > 5:` |
| Loop | `loop <count>:` | `loop 3:` |
| Arithmetic | `+ - * /` | `x + y * 2` |
| Comparison | `> < ==` | `score > 50` |
| String | `"text"` | `show "hello"` |

---

## Project Structure

```
src/
├── Main/
│   └── Main.java               Entry point — reads .zara file and runs interpreter
├── lexer/
│   ├── TokenType.java          Enum of all token types (SET, SHOW, NUMBER, etc.)
│   ├── Token.java              Immutable object holding one token (type, value, line)
│   └── Tokenizer.java          Converts raw source code into a list of tokens
├── parser/
│   ├── TokenStream.java        Navigates through the token list (peek, advance, match)
│   ├── ExpressionParser.java   Parses expressions and builds expression trees
│   └── Parser.java             Parses statements (set, show, when, loop) into instructions
├── ast/
│   ├── Expression.java         Interface — all expression nodes implement this
│   ├── NumberNode.java         Represents a number literal e.g. 42
│   ├── StringNode.java         Represents a string literal e.g. "hello"
│   ├── VariableNode.java       Represents a variable reference e.g. x
│   └── BinaryOpNode.java       Represents an operation e.g. x + y * 2
├── instructions/
│   ├── Instruction.java        Interface — all instruction classes implement this
│   ├── AssignInstruction.java  Handles: set x = <expr>
│   ├── PrintInstruction.java   Handles: show <expr>
│   ├── IfInstruction.java      Handles: when <condition>: <block>
│   └── RepeatInstruction.java  Handles: loop <count>: <block>
└── interpreter/
    ├── Environment.java        Variable store — maps variable names to their values
    └── Interpreter.java        Connects all three steps and executes the program
```

---

## How to Run

### 1. Compile
```bash
javac -d bin $(find src -name "*.java")
```

### 2. Run a `.zara` file
```bash
java -cp bin Main.Main src/test/program1.zara
```

---

## Sample Programs

### Program 1 — Arithmetic and variables
```
set x = 10
set y = 3
set result = x + y * 2
show result
```
```
Output: 16
```

### Program 2 — String output
```
set name = "Sitare"
show name
show "Hello from ZARA"
```
```
Output:
Sitare
Hello from ZARA
```

### Program 3 — Conditional
```
set score = 85
when score > 50:
    show "Pass"
```
```
Output: Pass
```

### Program 4 — Loop
```
set i = 1
loop 4:
    show i
    set i = i + 1
show i
```
```
Output:
1
2
3
4
5
```

---

## Design Decisions

### Why is the `parser` package split into three classes?

The original `Parser.java` was doing three unrelated jobs in one class — a violation of the **Single Responsibility Principle (SRP)**:

| Class | Single Responsibility |
|---|---|
| `TokenStream` | Navigate through the token list |
| `ExpressionParser` | Parse expressions and build expression trees |
| `Parser` | Parse statements and build instruction list |

This means:
- Adding a new **operator** → only `ExpressionParser` changes
- Adding a new **instruction** → only `Parser` changes
- Changing how **tokens are stored** → only `TokenStream` changes

With the old design, all three reasons would modify the same file.

### How does operator precedence work?
`ExpressionParser` uses a chain of four methods. Each level calls the one below it first, so lower levels bind tighter:

```
parseComparison()   →   handles  >  <  ==     (lowest priority)
    calls
parseExpression()   →   handles  +  -         (medium priority)
    calls
parseTerm()         →   handles  *  /         (high priority)
    calls
parsePrimary()      →   handles  numbers,     (highest priority)
                                 strings,
                                 variables,
                                 ( )
```
The shape of the tree enforces precedence automatically — no special tricks needed.

### How does the shared `TokenStream` work?
`Parser` and `ExpressionParser` both hold a reference to the **same** `TokenStream` instance. So when `ExpressionParser` consumes tokens while parsing `x + y * 2`, `Parser` automatically sees the updated position. They stay in sync without any extra coordination.

---

## Supported Test Programs
| File | Tests |
|---|---|
| `program1.zara` | Arithmetic with operator precedence |
| `program2.zara` | String output |
| `program3.zara` | Conditional (`when`) |
| `program4.zara` | Loop with variable update |
| `program5.zara` | Multiple conditionals |
| `program6.zara` | Loop with counter |
| `program7.zara` | Unary minus and parentheses |
| `program8.zara` | Strings + parenthesised arithmetic |
| `program9.zara` | Parentheses changing precedence |
| `program10.zara` | Nested parentheses |
| `program11.zara` | Area and perimeter calculation |