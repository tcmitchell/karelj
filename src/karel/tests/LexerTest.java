/*
  Copyright (C) 2000,2001 Tom Mitchell

  This file is part of KarelJ.

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package karel.tests;

import java.io.StringReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import karel.Lexer;
import karel.Token;
import karel.TokenType;

/**
 *
 */
public class LexerTest extends TestCase {

	public LexerTest(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(LexerTest.class);
	}

	public void validateLexer(String program, Token tokens[]) {
		StringReader input = new StringReader(program);

		int nTokens = tokens.length;

		try {
			Lexer lex = new Lexer(input);

			for (int i = 0; i < nTokens; i++) {
				assertEquals(tokens[i], lex.nextToken());
			}

		} catch (java.io.IOException e) {
			fail("Caught IOException " + e.toString());
		}
	}

	public void testWords() {
		String program = "tom dick harry";
		Token tokens[] = { new Token(TokenType.NAME, "tom"), new Token(TokenType.NAME, "dick"),
				new Token(TokenType.NAME, "harry") };
		validateLexer(program, tokens);
	}

	public void testNumbers() {
		String program = "3 17 383";
		Token tokens[] = { new Token(TokenType.NUMBER, 3), new Token(TokenType.NUMBER, 17),
				new Token(TokenType.NUMBER, 383), new Token(TokenType.EOF, "") };
		validateLexer(program, tokens);
	}

	public void testWordsAndNumbers() {
		String program = "tom 2 dick 45 harry 83";
		Token tokens[] = { new Token(TokenType.NAME, "tom"), new Token(TokenType.NUMBER, 2),
				new Token(TokenType.NAME, "dick"), new Token(TokenType.NUMBER, 45), new Token(TokenType.NAME, "harry"),
				new Token(TokenType.NUMBER, 83), new Token(TokenType.EOF, "") };
		validateLexer(program, tokens);
	}

	public void testComments() {
		String program = "/* this is a comment */\nBEGINNING-OF-PROGRAM\n"
				+ "/* another comment */\nEND-OF-PROGRAM\n/* a third comment */";
		Token tokens[] = { new Token(TokenType.BEGPROG, "beginning-of-program"),
				new Token(TokenType.ENDPROG, "end-of-program"), new Token(TokenType.EOF, "") };
		validateLexer(program, tokens);
	}
}
