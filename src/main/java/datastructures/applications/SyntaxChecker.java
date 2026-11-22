package datastructures.applications;

import datastructures.linear.LinkedStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Checks bracket nesting while ignoring Java-style comments and quoted literals. */
public final class SyntaxChecker {
    private SyntaxChecker() { }

    private enum Mode { CODE, LINE_COMMENT, BLOCK_COMMENT, SINGLE_QUOTE, DOUBLE_QUOTE }
    private static final class Opening {
        final char token; final int line, column;
        Opening(char token, int line, int column) {
            this.token = token; this.line = line; this.column = column;
        }
    }
    public static final class Issue {
        private final int line, column;
        private final String message;
        Issue(int line, int column, String message) {
            this.line = line; this.column = column; this.message = message;
        }
        public int line() { return line; }
        public int column() { return column; }
        public String message() { return message; }
        @Override public String toString() { return line + ":" + column + " " + message; }
    }
    public static final class Result {
        private final List<Issue> issues;
        Result(List<Issue> issues) {
            this.issues = Collections.unmodifiableList(new ArrayList<>(issues));
        }
        public boolean isValid() { return issues.isEmpty(); }
        public List<Issue> issues() { return issues; }
    }

    /** Reports unclosed/mismatched delimiters with one-based line and column positions. */
    public static Result check(String source) {
        if (source == null) throw new IllegalArgumentException("source cannot be null");
        List<Issue> issues = new ArrayList<>();
        LinkedStack<Opening> opened = new LinkedStack<>();
        Mode mode = Mode.CODE;
        int line = 1, column = 1, literalLine = 0, literalColumn = 0;
        boolean escaped = false;
        for (int i = 0; i < source.length(); i++) {
            char character = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            if (mode == Mode.LINE_COMMENT) {
                if (character == '\n') mode = Mode.CODE;
            } else if (mode == Mode.BLOCK_COMMENT) {
                if (character == '*' && next == '/') { mode = Mode.CODE; i++; column++; }
            } else if (mode == Mode.SINGLE_QUOTE || mode == Mode.DOUBLE_QUOTE) {
                if (escaped) escaped = false;
                else if (character == '\\') escaped = true;
                else if ((mode == Mode.SINGLE_QUOTE && character == '\'') ||
                         (mode == Mode.DOUBLE_QUOTE && character == '"')) mode = Mode.CODE;
                // A newline cannot appear unescaped inside an ordinary Java quoted literal.
                if (character == '\n' && mode != Mode.CODE) {
                    issues.add(new Issue(literalLine, literalColumn, "unclosed quoted literal"));
                    mode = Mode.CODE; escaped = false;
                }
            } else {
                if (character == '/' && next == '/') { mode = Mode.LINE_COMMENT; i++; column++; }
                else if (character == '/' && next == '*') {
                    mode = Mode.BLOCK_COMMENT; literalLine = line; literalColumn = column;
                    i++; column++;
                } else if (character == '\'' || character == '"') {
                    mode = character == '\'' ? Mode.SINGLE_QUOTE : Mode.DOUBLE_QUOTE;
                    literalLine = line; literalColumn = column; escaped = false;
                } else if (character == '(' || character == '[' || character == '{') {
                    opened.push(new Opening(character, line, column));
                } else if (character == ')' || character == ']' || character == '}') {
                    if (opened.isEmpty()) {
                        issues.add(new Issue(line, column, "unexpected closing '" + character + "'"));
                    } else if (matches(opened.peek().token, character)) {
                        opened.pop();
                    } else {
                        Opening wrong = opened.pop();
                        issues.add(new Issue(line, column,
                                "closing '" + character + "' does not match '" + wrong.token +
                                "' opened at " + wrong.line + ":" + wrong.column));
                    }
                }
            }
            if (character == '\n') { line++; column = 1; }
            else column++;
        }
        if (mode == Mode.BLOCK_COMMENT) issues.add(new Issue(literalLine, literalColumn, "unclosed block comment"));
        if (mode == Mode.SINGLE_QUOTE || mode == Mode.DOUBLE_QUOTE)
            issues.add(new Issue(literalLine, literalColumn, "unclosed quoted literal"));
        while (!opened.isEmpty()) {
            Opening item = opened.pop();
            issues.add(new Issue(item.line, item.column, "unclosed opening '" + item.token + "'"));
        }
        return new Result(issues);
    }
    private static boolean matches(char opening, char closing) {
        return (opening == '(' && closing == ')') ||
               (opening == '[' && closing == ']') ||
               (opening == '{' && closing == '}');
    }
}
