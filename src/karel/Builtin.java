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

public class Builtin extends Instruction {

    public Builtin(String s) {
	super(s);
	String token = s.toLowerCase().intern();
	keymap.put(token, this);
    }

    public String toString() {
	return "#<Builtin " + description + ">";
    }

    public String getToken() {
	return getDescription();
    }

    public TokenType getId() {
	return TokenType.BLTIN;
    }

    static Hashtable keymap = new Hashtable();

    static {
	try {			// Get the class 'Test' to load
	    Class.forName("karel.Test");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	new Builtin("move") {
		public boolean execute(KarelVM vm) throws KarelException {
		    vm.getRobot().move();
		    return true;
		}
	    };

	new Builtin("pickbeeper") {
		public boolean execute(KarelVM vm) throws KarelException {
		    vm.getRobot().pickBeeper();
		    return true;
		}
	    };

	new Builtin("putbeeper"){
		public boolean execute(KarelVM vm) throws KarelException {
		    vm.getRobot().putBeeper();
		    return true;
		}
	    };

	new Builtin("turnleft") {
		public boolean execute(KarelVM vm) throws KarelException {
		    vm.getRobot().turnleft();
		    return true;
		}
	    };

	new Builtin("turnoff") {
		public boolean execute(KarelVM vm) throws KarelException {
		    vm.getRobot().turnoff();
		    return true;
		}
	    };
    };

    public static Builtin lookup(String s) {
	return (Builtin) keymap.get(s.intern());
    }
}
