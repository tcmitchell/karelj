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

public class Test extends Builtin {

    public Test(String s) {
	super(s);
    }

    public String toString() {
	return "#<Test " + description + ">";
    }

    public String getToken() {
	return getDescription();
    }

    public TokenType getId() {
	return TokenType.TEST;
    }

    static {
	new Test("any-beepers-in-beeper-bag") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getBeepers() > 0);
		}
	    };
	new Test("facing-east") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() == Robot.EAST);
		}
	    };
	new Test("facing-north") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() == Robot.NORTH);
		}
	    };
	new Test("facing-south") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() == Robot.SOUTH);
		}
	    };
	new Test("facing-west") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() == Robot.WEST);
		}
	    };
	new Test("front-is-blocked") {
		public boolean execute(KarelVM vm) {
		    Robot r = vm.getRobot();
		    return r.isDirBlocked(r.getDirection());
		}
	    };
	new Test("front-is-clear") {
		public boolean execute(KarelVM vm) throws KarelException {
		    Robot r = vm.getRobot();
		    return (! r.isDirBlocked(r.getDirection()));
		}
	    };
	new Test("left-is-blocked") {
		public boolean execute(KarelVM vm) {
		    return vm.getRobot().leftIsBlocked();
		}
	    };
	new Test("left-is-clear") {
		public boolean execute(KarelVM vm) throws KarelException {
		    return (! vm.getRobot().leftIsBlocked());
		}
	    };
	new Test("next-to-a-beeper") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().nextToABeeper());
		}
	    };
	new Test("no-beepers-in-beeper-bag") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getBeepers() == 0);
		}
	    };
	new Test("not-facing-east") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() != Robot.EAST);
		}
	    };
	new Test("not-facing-north") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() != Robot.NORTH);
		}
	    };
	new Test("not-facing-south") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() != Robot.SOUTH);
		}
	    };
	new Test("not-facing-west") {
		public boolean execute(KarelVM vm) {
		    return (vm.getRobot().getDirection() != Robot.WEST);
		}
	    };
	new Test("not-next-to-a-beeper") {
		public boolean execute(KarelVM vm) {
		    return (!vm.getRobot().nextToABeeper());
		}
	    };
	new Test("right-is-blocked") {
		public boolean execute(KarelVM vm) {
		    return vm.getRobot().rightIsBlocked();
		}
	    };
	new Test("right-is-clear") {
		public boolean execute(KarelVM vm) {
		    return (!vm.getRobot().rightIsBlocked());
		}
	    };
    };
}
