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

import java.util.Enumeration;
import java.util.Vector;

public class Program implements java.io.Serializable {

    static final long serialVersionUID = -5893828962083530008L;

    static final boolean DEBUG = false;

    Vector instructions;

    int startAddress;

    public Program() {
	instructions = new Vector();
	startAddress = 0;
    }
    
    public boolean equals(Object obj) {
	if (obj instanceof Program) {
	    Program that = (Program) obj;
	    return (this.startAddress == that.startAddress
		    && this.instructions.equals(that.instructions));
	}
	return false;
    }

    public void install() {
	if (DEBUG)
	    System.out.println("Installing null at " + instructions.size());
	instructions.add(null);
    }

    public void install(Instruction i) {
	if (DEBUG)
	    System.out.println("Installing " + i.getDescription()
			       + " at " + instructions.size());
	instructions.add(i);
    }

    public void install(int loc, Instruction i) {
	if (DEBUG)
	    System.out.println("Installing " + i.getDescription()
			       + " at " + loc);
	instructions.set(loc, i);
    }

    public void install(int jump) {
	if (DEBUG)
	    System.out.println("Installing " + jump +
			       " at " + instructions.size());
	instructions.add(new Integer(jump));
    }

    public void install(int loc, int jump) {
	if (DEBUG)
	    System.out.println("Installing " + jump + " at " + loc);
	instructions.set(loc, new Integer(jump));
    }

    public void setStartAddr(int addr) {
	startAddress = addr;
    }

    public int getStartAddr() {
	return startAddress;
    }

    public int getProgp() {
	return instructions.size();
    }

    public Object getElementAt(int loc) {
	return instructions.elementAt(loc);
    }

    public Instruction getInstructionAt(int loc) {
	return (Instruction) getElementAt(loc);
    }

    public void print() {
	int size = instructions.size();
	System.out.println("Program with " + size + " instructions:");
	for (int i=0; i<size; i++) {
	    System.out.println(i + ": " + instructions.elementAt(i));
	}
    }
}
