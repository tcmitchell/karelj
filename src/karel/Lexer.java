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

package karel;

import java.io.*;
import java.util.Hashtable;

public class Lexer
{
    static final boolean DEBUG = false;

    Reader r;
    StreamTokenizer tokenizer;
    int current_line = 0;
    Hashtable symbols;
    Token fCurrentToken;
    Token fPushbackToken;

    public Lexer(Reader rdr) throws IOException {
	r = rdr;
	symbols = new Hashtable();
	tokenizer = new StreamTokenizer(r);
	tokenizer.lowerCaseMode(true);
	tokenizer.eolIsSignificant(true);
	tokenizer.slashStarComments(true);
	fCurrentToken = getNextToken();
    }

    public Lexer(String filename) throws IOException {
	this(new BufferedReader(new FileReader(filename)));
    }

    public int getCurrentLine() {
	return current_line;
    }

    private Token getNextToken() throws IOException {
	int ttype = tokenizer.nextToken();
	if (DEBUG)
	    System.out.print("ttype=" + ttype + "; ");

	switch (ttype) {
	case StreamTokenizer.TT_EOF:
	    if (DEBUG)
		System.out.println("EOF");
	    return new Token(TokenType.EOF, "");
	case StreamTokenizer.TT_EOL:
	    current_line += 1;
	    return getNextToken();
	case StreamTokenizer.TT_NUMBER:
	    if (DEBUG)
		System.out.println("NUMBER=" + tokenizer.nval);
	    return new Token(TokenType.NUMBER, (int)tokenizer.nval);
	case StreamTokenizer.TT_WORD:
	    if (DEBUG)
		System.out.print("WORD=" + tokenizer.sval);
	    Keyword key = Keyword.lookup(tokenizer.sval);
	    if (key != null) {
		if (DEBUG)
		    System.out.println("; Keyword=" + key.getToken());
		return new Token(key.getType(), key.getToken());
	    }
  	    Builtin b = Builtin.lookup(tokenizer.sval);
  	    if (b != null) {
  		if (DEBUG)
  		    System.out.println("; Builtin=" + b.getToken());
		if (b.getId() == TokenType.TEST)
		    return new Token(TokenType.TEST, b.getToken());
		else
		    return new Token(TokenType.BLTIN, b.getToken());
  	    }

	    if (DEBUG)
		System.out.println();
	    return new Token(TokenType.NAME, tokenizer.sval);

	default:
	    if (DEBUG)
		System.out.println("Unknown=" + (char)ttype);
	    StringBuffer sb = new StringBuffer();
	    sb.append((char)ttype);
	    return new Token(TokenType.CHAR, sb.toString());
	}
    }

    public boolean pushBack(Token tok) {
	if (fPushbackToken == null) {
	    fPushbackToken = tok;
	} else {
	    throw(new IllegalArgumentException("Already have pushback token"));
	}
	return true;
    }

    public Token peekToken() throws IOException {
	if (fPushbackToken == null) {
	    return fCurrentToken;
	} else {
	    return fPushbackToken;
	}
    }

    public Token nextToken() throws IOException {
	if (fPushbackToken == null) {
	    Token value = fCurrentToken;
	    fCurrentToken = getNextToken();
	    return value;
	} else {
	    Token value = fPushbackToken;
	    fPushbackToken = null;
	    return value;
	}
    }
	
    public void installSymbol(String symbol, int loc) {
	String sym = symbol.intern();
	symbols.put(sym, new Symbol(sym, loc));
    }

    public Symbol lookupSymbol(String symbol) {
	return (Symbol) symbols.get(symbol.intern());
    }

    public static void main(String args[]) {
	System.out.println("Lexer.main\n");
	if (args.length < 1) {
	    System.err.println("usage: Lexer <filename>\n");
	    System.exit(1);
	}

	try {
	    Lexer l = new Lexer(args[0]);
	    Token tok;
	    while ((tok = l.nextToken()) != null) {
		System.out.println("token: " + tok);
		System.out.println("peek: " + l.peekToken());
		System.out.println();
		if (tok.getType() == TokenType.EOF)
		    break;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}
