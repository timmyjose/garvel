package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.common.parser.CharWrapper;
import com.tzj.garvel.core.parser.exception.LexerException;
import com.tzj.garvel.core.parser.exception.JsonScannerException;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.*;

public class JsonScanner {
    private String filename;
    private JsonLexer lexer;

    private List<JsonToken> tokens;
    private int idx;

    private JsonTokenType currentKind;
    private StringBuffer currentSpelling;

    private CharWrapper currentChar;
    private int currentLine;
    private int currentColumn;

    public JsonScanner(final String filename) throws JsonScannerException {
        this.filename = filename;
        try {
            this.lexer = new JsonLexer(filename);
        } catch (LexerException e) {
            throw new JsonScannerException(String.format("Error while creating a lexer for %s", filename));
        }

        this.tokens = new ArrayList<>();
        scanAll();
    }

    // test
    public static void main(String[] args) {
        final String filename = System.getProperty("user.dir") + "/src/com/tzj/garvel/playground/misc/Garvel_single.gl.sample";

        try {
            JsonScanner scanner = new JsonScanner(filename);

            while (scanner.hasMoreTokens()) {
                System.out.println(scanner.scan());
            }
        } catch (JsonScannerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Skipe the current character unconditionally.
     */
    void skipIt() {
        currentChar = lexer.nextCharacter();
    }

    /**
     * Skip the current character after ensuring it is the expected character.
     *
     * @param expectedChar
     * @throws JsonScannerException
     */
    void skip(final char expectedChar) throws JsonScannerException {
        if (currentChar.c() != expectedChar) {
            throw new JsonScannerException(String.format("Scanner Error at line %d, column %d while scanning %s, expected to skip %c, found %s",
                    currentChar.line(), currentChar.column(), filename, expectedChar, currentChar.c()));
        }
        currentChar = lexer.nextCharacter();
    }

    /**
     * Add the current character to currentSpelling unconditionally.
     */
    void takeIt() {
        currentSpelling.append(currentChar.c());
        currentChar = lexer.nextCharacter();
    }

    /**
     * Add the current character to currentSpelling after ensuring it is the expected character.
     *
     * @param expectedChar
     * @throws JsonScannerException
     */
    void take(final char expectedChar) throws JsonScannerException {
        if (currentChar.c() != expectedChar) {
            throw new JsonScannerException(String.format("Scanner Error at line %d, col %d while scanning %s: expected to take %c, found %c",
                    currentChar.line(), currentChar.column(), filename, expectedChar, currentChar.c()));
        }
    }

    /**
     * Populate the tokens list.
     *
     * @throws JsonScannerException
     */
    private void scanAll() throws JsonScannerException {
        currentChar = lexer.nextCharacter();

        while (lexer.hasMoreCharacters()) {
            while (currentChar.c() == OCTOTHORPE || currentChar.c() == SPACE || currentChar.c() == EOL) {
                scanSeparator();
            }

            currentSpelling = new StringBuffer();
            currentKind = scanToken();
            tokens.add(new JsonToken(currentKind, currentSpelling.toString(), currentLine, currentColumn));
        }
    }

    /**
     * Return the type of the current token.
     *
     * @return
     * @throws JsonScannerException
     */
    private JsonTokenType scanToken() throws JsonScannerException {
        JsonTokenType kind = null;

        currentLine = currentChar.line();
        currentColumn = currentChar.column();

        switch (currentChar.c()) {
            case '\"': {
                skipIt();

                while (isStringCharacter(currentChar.c())) {
                    takeIt();
                }

                skip('\"');
                kind = JsonTokenType.STRING;
            }
            break;

            case ',': {
                takeIt();
                kind = JsonTokenType.COMMA;
            }
            break;

            case ':': {
                takeIt();
                kind = JsonTokenType.COLON;
            }
            break;

            case '{': {
                takeIt();
                kind = JsonTokenType.LEFT_BRACE;
            }
            break;

            case '}': {
                takeIt();
                kind = JsonTokenType.RIGHT_BRACE;
            }
            break;

            case '[': {
                takeIt();
                kind = JsonTokenType.LEFT_SQUARE_BRACKETS;
            }
            break;

            case ']': {
                takeIt();
                kind = JsonTokenType.RIGHT_SQUARE_BRACKETS;
            }
            break;

            case EOI: {
                kind = JsonTokenType.EOT;
            }
            break;

            default:
                throw new JsonScannerException(String.format("Scanner Error at line %d, col %d while scanning %s: %c cannot start a valid token",
                        currentChar.line(), currentChar.column(), filename, currentChar.c()));
        }

        return kind;
    }

    private boolean isStringCharacter(final char c) {
        switch (c) {
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case SPACE:
            case PERIOD:
            case COLON:
            case SEMICOLON:
            case DASH:
            case ASTERISK:
            case FORWARD_SLASH:
            case COMMA:
            case UNDERSCORE:
            case BANG:
            case CARET:
            case TILDE:
            case AMPERSAND:
                return true;

            default:
                return false;
        }
    }

    private boolean isGraphic(final char c) {
        switch (c) {
            case EOL:
            case EOI:
                return false;

            default:
                return true;
        }
    }

    /**
     * This JSON veriant supports comments using the `#` character.
     */
    private void scanSeparator() throws JsonScannerException {
        switch (currentChar.c()) {
            case OCTOTHORPE: {
                skipIt();

                while (isGraphic(currentChar.c())) {
                    skipIt();
                }
                skip(EOL);
            }
            break;

            case SPACE:
            case EOL:
                skipIt();
                while (currentChar.c() == SPACE || currentChar.c() == EOL) {
                    skipIt();
                }
                break;
        }
    }

    private JsonToken nextToken() {
        idx++;
        return tokens.get(idx - 1);
    }

    private boolean hasMoreTokens() {
        return idx < tokens.size();
    }

    public JsonToken scan() throws JsonScannerException {
        if (!hasMoreTokens()) {
            throw new JsonScannerException(String.format("No more tokens in the token stream while scanning %s", filename));
        }

        return nextToken();
    }
}

