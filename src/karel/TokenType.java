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

public class TokenType {

    protected String fName;

    public TokenType(String name) {
	fName = name;
    }

    public String toString() {
	return fName;
    }

    public final static TokenType EOF = new TokenType("EOF");
    public final static TokenType CHAR = new TokenType("CHAR");

    public final static TokenType AS = new TokenType("AS");
    public final static TokenType BEGEXEC = new TokenType("BEGEXEC");
    public final static TokenType BEGIN = new TokenType("BEGIN");
    public final static TokenType BEGPROG = new TokenType("BEGPROG");
    public final static TokenType DEFINST = new TokenType("DEFINST");
    public final static TokenType DO = new TokenType("DO");
    public final static TokenType ELSE = new TokenType("ELSE");
    public final static TokenType END = new TokenType("END");
    public final static TokenType ENDEXEC = new TokenType("ENDEXEC");
    public final static TokenType ENDPROG = new TokenType("ENDPROG");
    public final static TokenType IF = new TokenType("IF");
    public final static TokenType ITERATE = new TokenType("ITERATE");
    public final static TokenType THEN = new TokenType("THEN");
    public final static TokenType TIMES = new TokenType("TIMES");
    public final static TokenType WHILE = new TokenType("WHILE");
    public final static TokenType KEY = new TokenType("KEY");
    public final static TokenType BLTIN = new TokenType("BLTIN");
    public final static TokenType TEST = new TokenType("TEST");
    public final static TokenType NUMBER = new TokenType("NUMBER");
    public final static TokenType NAME = new TokenType("NAME");
}
