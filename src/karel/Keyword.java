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

import java.util.Hashtable;

class Keyword {

    public static int AS = 301;
    public static int BEGIN = 302;
    public static int BEGEXEC = 303;
    public static int BEGPROG = 304;
    public static int DEFINST = 305;
    public static int DO = 306;
    public static int ELSE = 307;
    public static int END = 308;
    public static int ENDEXEC = 309;
    public static int ENDPROG = 310;
    public static int IF = 311;
    public static int ITERATE = 312;
    public static int THEN = 313;
    public static int TIMES = 314;
    public static int WHILE = 315;

    TokenType fType;

    String token;

    public Keyword(String s, TokenType t) {
	fType = t;
	token = s.toLowerCase().intern();
	keymap.put(token, this);
    }

    public String getToken() {
	return token;
    }

    public TokenType getType() {
	return fType;
    }

    static Hashtable keymap;

    static {
	keymap = new Hashtable();
	new Keyword("AS", TokenType.AS);
	new Keyword("BEGIN", TokenType.BEGIN);
	new Keyword("BEGINNING-OF-EXECUTION", TokenType.BEGEXEC);
	new Keyword("BEGINNING-OF-PROGRAM", TokenType.BEGPROG);
	new Keyword("DEFINE-NEW-INSTRUCTION", TokenType.DEFINST);
	new Keyword("DO", TokenType.DO);
	new Keyword("ELSE", TokenType.ELSE);
	new Keyword("END", TokenType.END);
	new Keyword("END-OF-EXECUTION", TokenType.ENDEXEC);
	new Keyword("END-OF-PROGRAM", TokenType.ENDPROG);
	new Keyword("IF", TokenType.IF);
	new Keyword("ITERATE", TokenType.ITERATE);
	new Keyword("THEN", TokenType.THEN);
	new Keyword("TIMES", TokenType.TIMES);
	new Keyword("WHILE", TokenType.WHILE);
    };

    public static Keyword lookup(String s) {
	return (Keyword) keymap.get(s.intern());
    }
}

