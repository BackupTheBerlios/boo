/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.test;

import boo.parser.Lexer;
import boo.parser.Token;
import junit.framework.TestCase;

public class LexerTest extends TestCase {

	public void testMatchLine() {
		int n = 21453;
		
		String lineBegin = "\t//:     begin \"ciao\"  \t  ";
		String lineEnd = "//:\t\t\t      end  ";
		String lineText = "    testo //: testo testo     testo";
		
		Token expectedBegin = new Token(Token.BEGIN, n, "ciao");
		Token expectedEnd = new Token(Token.END, n, null);
		Token expectedText = new Token(Token.TEXT, n, lineText);
		
		Lexer l = new Lexer();
		
		assertEquals(l.matchLine(lineBegin, n), expectedBegin);
		assertEquals(l.matchLine(lineEnd, n), expectedEnd);
		assertEquals(l.matchLine(lineText, n), expectedText);
	}

}
