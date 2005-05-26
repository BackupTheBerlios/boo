/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.parser;

/**
 * Rappresentazione intermedia di una riga di testo comunicata dal lexer al
 * parser.
 */
public class Token {

    /**
     * Tipo corrispondente a un'istruzione <code>begin</code>. Il valore
     * associato &egrave; il nome del blocco.
     */
    public static final int BEGIN = 1;
    /**
     * Tipo corrispondente a un'istruzione <code>end</code>. Il valore
     * associato &egrave; sempre <code>null</code>.
     */
    public static final int END = 2;
    /**
     * Tipo corrispondente a una riga di testo. Il valore associato &egrave; la
     * riga, escludendo il carattere di fine riga.
     */
    public static final int TEXT = 3;

    protected int type;
    protected int lineNumber;
    protected String contents;

    /**
     * &Egrave; un costruttore, che vuoi che faccia?
     * 
     * @param type
     *            Tipo del token
     * @param lineNumber
     *            Numero di riga a cui appartiene il token
     * @param contents
     *            Valore associato al token
     */
    public Token(int type, int lineNumber, String contents) {
        this.contents = contents;
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getContents() {
        return contents;
    }

    public String toString() {
        return "Token(type: " + type + ", lineNumber: " + lineNumber
                + ", contents: \"" + contents + "\")";
    }

    public boolean equals(Object other) {
        if (other.getClass() != getClass()) return false;
        Token t = (Token) other;
        return type == t.type
                && lineNumber == t.lineNumber
                && ((contents == null && t.contents == null) || contents
                        .equals(t.contents));
    }

}