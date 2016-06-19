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

public class Test extends Builtin {

	public Test(String s) {
		super(s);
	}

	@Override
	public String toString() {
		return "#<Test " + description + ">";
	}

	@Override
	public String getToken() {
		return getDescription();
	}

	@Override
	public TokenType getId() {
		return TokenType.TEST;
	}

	static {
		new Test("any-beepers-in-beeper-bag") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getBeepers() > 0);
			}
		};
		new Test("facing-east") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() == Robot.EAST);
			}
		};
		new Test("facing-north") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() == Robot.NORTH);
			}
		};
		new Test("facing-south") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() == Robot.SOUTH);
			}
		};
		new Test("facing-west") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() == Robot.WEST);
			}
		};
		new Test("front-is-blocked") {
			@Override
			public boolean execute(KarelVM vm) {
				Robot r = vm.getRobot();
				return r.isDirBlocked(r.getDirection());
			}
		};
		new Test("front-is-clear") {
			@Override
			public boolean execute(KarelVM vm) throws KarelException {
				Robot r = vm.getRobot();
				return (!r.isDirBlocked(r.getDirection()));
			}
		};
		new Test("left-is-blocked") {
			@Override
			public boolean execute(KarelVM vm) {
				return vm.getRobot().leftIsBlocked();
			}
		};
		new Test("left-is-clear") {
			@Override
			public boolean execute(KarelVM vm) throws KarelException {
				return (!vm.getRobot().leftIsBlocked());
			}
		};
		new Test("next-to-a-beeper") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().nextToABeeper());
			}
		};
		new Test("no-beepers-in-beeper-bag") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getBeepers() == 0);
			}
		};
		new Test("not-facing-east") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() != Robot.EAST);
			}
		};
		new Test("not-facing-north") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() != Robot.NORTH);
			}
		};
		new Test("not-facing-south") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() != Robot.SOUTH);
			}
		};
		new Test("not-facing-west") {
			@Override
			public boolean execute(KarelVM vm) {
				return (vm.getRobot().getDirection() != Robot.WEST);
			}
		};
		new Test("not-next-to-a-beeper") {
			@Override
			public boolean execute(KarelVM vm) {
				return (!vm.getRobot().nextToABeeper());
			}
		};
		new Test("right-is-blocked") {
			@Override
			public boolean execute(KarelVM vm) {
				return vm.getRobot().rightIsBlocked();
			}
		};
		new Test("right-is-clear") {
			@Override
			public boolean execute(KarelVM vm) {
				return (!vm.getRobot().rightIsBlocked());
			}
		};
	};
}
