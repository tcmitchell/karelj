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

public class KarelVM extends Thread {

    static final boolean DEBUG = false;

    Robot fRobot;

    Program fProgram;
    
    public KarelVM(Robot r, Program p) {
	fRobot = r;
	fProgram = p;
    }

    public Robot getRobot() {
	return fRobot;
    }

    public void run() {
	try {
  	    execute(fProgram);
	} catch (KarelException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    /*-----------------------------------------------------------------*/
    /*                     Program Execution                           */
    /*-----------------------------------------------------------------*/

    private boolean status;
    private int pc;
    private Program program;

    private int getProgramCounter() {
	return pc;
    }

    private void setProgramCounter(int newPC) {
	pc = newPC;
    }

    private Object getProgramElement(int loc) {
	return program.getElementAt(loc);
    }

    private boolean execute(Program p, int startPC) throws KarelException {
	int tmpPC = pc;
	Program tmpProg = program;

	pc = startPC;
	program = p;
	Instruction i = program.getInstructionAt(pc);

	while (! Instruction.RETURN.equals(i)) {
	    status = i.execute(this);
	    pc += 1;
	    i = program.getInstructionAt(pc);
	}

	pc = tmpPC;
	program = tmpProg;

	return true;
    }

    public boolean branch() throws KarelException {
	Object o = getProgramElement(getProgramCounter() + 1);
	int jump = ((Integer)o).intValue();
	if (DEBUG)
	    System.out.println("Executing BRANCH instruction: " + jump);

	// the PC gets incremented by the engine
	// after this instruction runs, so
	// decrement here to get the right jump
	setProgramCounter(jump - 1);
	return true;
    }

    public boolean condbranch() throws KarelException {
	if (! status) {
	    Object o = getProgramElement(getProgramCounter() + 1);
	    int jump = ((Integer)o).intValue();
	    if (DEBUG)
		System.out.println("Executing CONDBRANCH: " + jump);
	    // the PC gets incremented by the engine
	    // after this instruction runs, so
	    // decrement here to get the right jump
	    setProgramCounter(jump - 1);
	} else {
	    if (DEBUG)
		System.out.println("Executing CONDBRANCH: noop");

	    // Jump past the jump in the instruction set.
	    setProgramCounter(getProgramCounter() + 1);
	}
	return true;
    }

    public boolean iterate() throws KarelException {
	int limit, jump, loopbody;

	Object o = getProgramElement(getProgramCounter() + 1);
	limit = ((Integer)o).intValue();

	o = getProgramElement(getProgramCounter() + 2);
	jump = ((Integer)o).intValue();

	loopbody = getProgramCounter() + 3;

	if (DEBUG)
	    System.out.println("Executing ITERATE: " + limit);

	for (int k=0; k<limit; k++)
	    execute(program, loopbody);

	if (DEBUG)
	    System.out.println("Executing ITERATE BRANCH: " + jump);

	// the PC gets incremented by the engine
	// after this instruction runs, so
	// decrement here to get the right jump
	setProgramCounter(jump - 1);
	return true;
	
    }

    public boolean callproc() throws KarelException {
	Object o = getProgramElement(getProgramCounter() + 1);
	int subroutine = ((Integer)o).intValue();
	execute(program, subroutine);

	setProgramCounter(getProgramCounter() + 1);
	return true;
    }

    public boolean execute(Program p) throws KarelException {
	return execute(p, p.getStartAddr());
    }

}
