/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexer che riceve in ingresso una riga del file sottoposto a controllo e
 * restituisce il tipo di token corrispondente.
 */
public class Lexer {

    /**
     * Espressione regolare che descrive le dichiarazioni di inizio dei blocchi.
     */
    protected static final Pattern PATTERN_BEGIN = Pattern.compile(
            "//: \\s* begin \\s+ \" (.+) \" ", Pattern.COMMENTS);
    /**
     * Espressione regolare che descrive le dichiarazioni di fine dei blocchi.
     */
    protected static final Pattern PATTERN_END = Pattern.compile(
            "//: \\s* end", Pattern.COMMENTS);

    /**
     * Restituisce il token corrispondente alla riga passata come parametro.
     * 
     * @param line
     *            Riga di testo da analizzare.
     * @param lineNumber
     *            Numero della riga.
     * @return Token corrispondente alla riga specificata.
     */
    public Token matchLine(String line, int lineNumber) {
        Matcher mBegin = Lexer.PATTERN_BEGIN.matcher(line.trim());
        Matcher mEnd = Lexer.PATTERN_END.matcher(line.trim());
        if (mBegin.matches()) {
            return new Token(Token.BEGIN, lineNumber, mBegin.group(1));
        } else if (mEnd.matches()) {
            return new Token(Token.END, lineNumber, null);
        } else {
            return new Token(Token.TEXT, lineNumber, line);
        }
    }

}
