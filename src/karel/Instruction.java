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

public class Instruction implements java.io.Serializable {

    static final long serialVersionUID = -2239955774797876234L;

    static final boolean DEBUG = false;

    protected String description;

    public Instruction(String desc) {
	description = desc;
    }

    public String toString() {
	return "#<Instruction " + description + ">";
    }

    public boolean equals(Object obj) {
	if (obj instanceof Instruction) {
	    Instruction that = (Instruction) obj;
	    return (this.description.equals(that.description));
	}
	return false;
    }

//      public boolean execute(Robot r) throws KarelException {
//  	if (DEBUG)
//  	    System.out.println("Executing default implementation of "
//  			       + description);
//  	return true;
//      }

    public boolean execute(KarelVM vm) throws KarelException {
	if (DEBUG)
	    System.out.println("Executing default implementation of "
			       + description);
	return true;
    }

    public String getDescription() {
	return description;
    }

    public static Instruction RETURN =
	new Instruction("RETURN") {
		public boolean execute(KarelVM vm) throws KarelException {
		    return true;
		}
	    };
    
    public static Instruction BRANCH =
	new Instruction("BRANCH") {
		public boolean execute(KarelVM vm) throws KarelException {
  		    return vm.branch();
		}
	    };
    
    public static Instruction CONDBRANCH =
	new Instruction("CONDBRANCH") {
		public boolean execute(KarelVM vm) throws KarelException {
  		    return vm.condbranch();
		}
	    };
    
    public static Instruction ITERATE =
	new Instruction("ITERATE") {
		public boolean execute(KarelVM vm) throws KarelException {
		    return vm.iterate();
		}
	    };
    
    public static Instruction CALL =
	new Instruction("CALL") {
		public boolean execute(KarelVM vm) throws KarelException {
		    return vm.callproc();
		}
	    };
}
