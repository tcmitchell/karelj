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

import karel.Program;
import karel.Builtin;
import karel.Instruction;

public class Parser {

    public Parser() {
    }

    public Token popToken(Lexer l) throws java.io.IOException {
	l.nextToken();
	return l.peekToken();
    }

    public boolean isSemicolon(Token t) {
	return (TokenType.CHAR == t.getType()
		&& ";".equals(t.getSVal()));
    }

    public boolean isElse(Token t) {
	return (TokenType.ELSE == t.getType());
    }

    public boolean expect(TokenType expected, Token got) {
	if (expected == got.getType()) {
	    return true;
	} else {
	    System.err.println("Expected " + expected + ", but found "
				 + got.getSVal());
	    return false;
	}
    }

    public boolean expect(String expected, Token got) {
	if ((TokenType.CHAR == got.getType())
	    && (expected.equals(got.getSVal()))) {
	    return true;
	} else {
	    System.err.println("Expected " + expected
			       + ", but found " + got.getSVal());
	    return false;
	}
    }

    public boolean expectDo(Token t) {
	return expect(TokenType.DO, t);
    }

    public boolean expectAs(Token t) {
	return expect(TokenType.AS, t);
    }

    public boolean expectEnd(Token t) {
	return expect(TokenType.END, t);
    }

    public boolean expectTimes(Token t) {
	return expect(TokenType.TIMES, t);
    }

    public boolean expectThen(Token t) {
	return expect(TokenType.THEN, t);
    }

    public boolean expectLogicTest(Program p, Token t, Lexer l) {
	if (TokenType.TEST == t.getType()) {
	    p.install(Builtin.lookup(t.getSVal()));
	    return true;
	} else {
	    System.err.println("Expected logic test, but found "
			       + t.getSVal());
	    return false;
	}
    }

    public boolean expectNumber(Program p, Token t, Lexer l) {
	if (TokenType.NUMBER == t.getType()) {
	    return true;
	} else {
	    System.err.println("Expected number, but found " + t.getSVal());
	    return false;
	}
    }

    public boolean expectEndExec(Program p, Token t, Lexer l) throws java.io.IOException {
	if (TokenType.ENDEXEC == t.getType()) {
	    p.install(Instruction.RETURN);
	    return expectEndProg(p, popToken(l), l);
	} else {
	    System.err.println("Expected END-OF-EXECUTION, but found "
			       + t.getSVal());
	    return false;
	}
    }

    public boolean expectEndProg(Program p, Token t, Lexer l) throws java.io.IOException {
	if (TokenType.ENDPROG == t.getType()) {
	    return expectEndOfFile(p, popToken(l), l);
	} else {
	    System.err.println("Expected END-OF-PROGRAM, but found "
			       + t.getSVal());
	    return false;
	}
    }

    public boolean expectEndOfFile(Program p, Token t, Lexer l) {
	if (TokenType.EOF == t.getType()) {
	    // Found EOF, return true
	    return true;
	} else {
	    System.err.println("Expected END-OF-FILE, but found "
			       + t.getSVal());
	    return false;
	}
    }

    public boolean expectStmt(Program p, Token t, Lexer l) throws java.io.IOException {
	if (TokenType.BEGIN == t.getType()) {
	    return (expectStmtList(p, popToken(l), l)
		    && expectEnd(popToken(l)));
	} else if (TokenType.ITERATE == t.getType()) {
	    p.install(Instruction.ITERATE);
	    if (expectNumber(p, popToken(l), l)) {
		p.install(l.peekToken().getIVal());
		int jumploc = p.getProgp();
		p.install();
		if (expectTimes(popToken(l))
		    && expectStmt(p, popToken(l), l)) {
		    p.install(Instruction.RETURN);
		    p.install(jumploc, p.getProgp());
		    return true;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	} else if (TokenType.IF == t.getType()) {
	    if (expectLogicTest(p, popToken(l), l)) {
		p.install(Instruction.CONDBRANCH);
		int jumploc = p.getProgp();
		p.install();
		if (expectThen(popToken(l))
		    && expectStmt(p, popToken(l), l)) {
		    p.install(jumploc, p.getProgp());
		    if (isElse(popToken(l))) {
			p.install(Instruction.BRANCH);
			int jumploc2 = p.getProgp();
			p.install();
			p.install(jumploc, jumploc2 + 1);
			if (expectStmt(p, popToken(l), l)) {
			    p.install(jumploc2, p.getProgp());
			    return true;
			} else {
			    return false;
			}
		    } else {
			l.pushBack(l.peekToken());
			return true;
		    }
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	} else if (TokenType.WHILE == t.getType()) {
	    int jumploc = p.getProgp();
	    if (expectLogicTest(p, popToken(l), l)) {
		p.install();
		p.install();
		if (expectDo(popToken(l))
		    && expectStmt(p, popToken(l), l)) {
		    p.install(jumploc + 1, Instruction.CONDBRANCH);
		    p.install(jumploc + 2, p.getProgp() + 2);
		    p.install(Instruction.BRANCH);
		    p.install(jumploc);
		    return true;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	} else if (TokenType.NAME == t.getType()) {
	    Symbol sym = l.lookupSymbol(t.getSVal());
	    if (sym != null) {
		p.install(Instruction.CALL);
		p.install(sym.getCodePtr());
		return true;
	    } else {
		System.err.println("undefined token: " + t.getSVal() + "\n");
		return false;
	    }
	} else if (TokenType.BLTIN == t.getType()) {
	    p.install(Builtin.lookup(t.getSVal()));
	    return true;
	} else {
	    System.err.println("Unexpected token " + t.getSVal()
			       + " in expectStmt");
	    return false;
	}
    }

    public boolean expectStmtList(Program p, Token t, Lexer l) throws java.io.IOException {
	if (expectStmt(p, t, l)) {
	    if (isSemicolon(popToken(l))) {
		return expectStmtList(p, popToken(l), l);
	    } else {
		l.pushBack(l.peekToken());
		return true;
	    }
	} else {
	    return false;
	}
    }

    public boolean expectMain(Program p, Token t, Lexer l) throws java.io.IOException {
	return (expectStmtList(p, t, l)
		&& (expectEndExec(p, popToken(l), l)));
    }

    public boolean expectBegExec(Program p, Token t, Lexer l) throws java.io.IOException {
	if (TokenType.BEGEXEC == t.getType()) {
	    p.setStartAddr(p.getProgp());
	    return expectMain(p, popToken(l), l);
	} else {
	    System.err.println("Expected BEGINNING-OF-EXECUTION, but found "
			       + t.getSVal());
	    return false;
	}
    }

    public boolean expectSemicolon(Program p, Token t, Lexer l) throws java.io.IOException {
	if (expectStmt(p, t, l)) {
	    if (isSemicolon(popToken(l))) {
		return expectStmtList(p, popToken(l), l);
	    } else {
		l.pushBack(l.peekToken());
		return true;
	    }
	} else {
	    return false;
	}
    }

    public boolean expectSemicolon1(Program p, Token t, Lexer l)
	throws java.io.IOException
    {
  	return expect(";", t);
    }

    public boolean expectDefInst(Program p, Token t, Lexer l) throws java.io.IOException {
	if (TokenType.DEFINST == t.getType()) {
	    Token name = popToken(l);
	    l.installSymbol(name.getSVal(), p.getProgp());
	    if (expectAs(popToken(l))
		&& expectStmt(p, popToken(l), l)
		&& expectSemicolon1(p, popToken(l), l)) {
		p.install(Instruction.RETURN);
		return expectDefInst(p, popToken(l), l);
	    } else {
		return false;
	    }
	} else {
	    return expectBegExec(p, t, l);
	}
    }

    public boolean expectProgram(Program p, Token t, Lexer l) throws java.io.IOException {
	if (TokenType.BEGPROG == t.getType()) {
	    return expectDefInst(p, popToken(l), l);
	} else {
	    System.err.println("Expected BEGINNING-OF-PROGRAM, but found "
			       + t.getSVal());
	    return false;
	}
    }

    public Program parse(String file) throws java.io.IOException {
	Program prog = new Program();
	Lexer lexer = new Lexer(file);
	if (expectProgram(prog, lexer.peekToken(), lexer)) {
	    return prog;
	} else {
	    return null;
	}
    }
}
