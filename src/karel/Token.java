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

/*
 * We probably want a separate TokenType class instead
 * of using an int to represent the type.  For now, use
 * the StreamTokenizer token types.
 */
public class Token {

    protected String fSVal;

    protected int fIVal;

    protected TokenType fType;

    public Token() {
    }

    public Token(TokenType t, String s) {
	fType = t;
	fSVal = s;
    }

    public Token(TokenType t, int i) {
	fType = t;
	fIVal = i;
    }

    public boolean equals(Object obj) {
	if (obj instanceof Token) {
	    Token that = (Token) obj;

	    // Be careful.  Either the SVals are both null, or
	    // they are equal.  Can't run .equals() on null...
	    return (this.fType.equals(that.fType)
		    && ((this.fSVal == null && that.fSVal == null)
			|| this.fSVal.equals(that.fSVal))
		    && this.fIVal == that.fIVal);
	}
	return false;
    }

    public String toString() {
	return "#<Token type: " + getType() + "; sval: " + getSVal()
	    + "; ival: " + getIVal() + ">";
    }

    public String getSVal() {
	return fSVal;
    }

    public int getIVal() {
	return fIVal;
    }

    public TokenType getType() {
	return fType;
    }
}
