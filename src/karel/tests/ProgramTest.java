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

package karel.tests;

import java.io.*;
import junit.framework.*;
import karel.Program;
import karel.Instruction;

/**
 * Serialize, deserialize, install, get progp, set/get startAddr
 */
public class ProgramTest extends TestCase {

    protected Program prog;

    public ProgramTest(String name) {
	super(name);
    }

    public static void main (String[] args) {
	junit.textui.TestRunner.run (suite());
    }

    public static Test suite() {
	return new TestSuite(ProgramTest.class);
    }

    protected void setUp() {
//    	System.out.println("setUp");
	prog = new Program();
    }

//      protected void tearDown() {
//    	System.out.println("tearDown");
//      }

    public void testStartAddr() {
	int addr = 5;
	prog.setStartAddr(addr);
	assertEquals(addr, prog.getStartAddr());
    }

    public void testProgp() {
	prog.install(1);
	assertEquals(1, prog.getProgp());
	prog.install(2);
	assertEquals(2, prog.getProgp());
	prog.install(15);
	assertEquals(3, prog.getProgp());
    }

    protected void initForEquals(Program p) {
	Instruction inst1 = new Instruction("foo");
	Instruction inst2 = new Instruction("bar");
	p.setStartAddr(4);
	p.install();
	p.install(inst1);
	p.install();
	p.install(2, inst2);
	p.install(45);
	p.install();
	p.install(4, 57);
    }

    public void testEquals() {
	Program prog2 = new Program();
	initForEquals(prog);
	initForEquals(prog2);
	assertEquals(prog, prog2);
    }

    public void testInstallVoid() {
	prog.install();
	Object obj = prog.getElementAt(0);
	assertEquals(null, obj);
    }

    public void testInstallInst() {
	Instruction inst1 = new Instruction("foo");
	prog.install(inst1);
	Object obj = prog.getElementAt(0);
	assertEquals(inst1, obj);
	Instruction inst = prog.getInstructionAt(0);
	assertEquals(inst1, inst);
    }

    public void testInstallIntInst() {
	Instruction inst1 = new Instruction("foo");
	prog.install();
	prog.install();
	prog.install(1, inst1);
	Object obj = prog.getElementAt(1);
	assertEquals(inst1, obj);
	Instruction inst = prog.getInstructionAt(1);
	assertEquals(inst1, inst);
    }

    public void testInstallInt() {
	int i1 = 5;
	prog.install(i1);
	Object obj = prog.getElementAt(0);
	assertEquals(new Integer(i1), obj);
	try { 
	    Instruction inst = prog.getInstructionAt(0);
	    fail("Should raise a ClassCastException"); 
	} catch (ClassCastException e) { 
	}
    }

    public void testInstallIntInt() {
	int loc = 1;
	int i1 = 5;
	prog.install();
	prog.install();
	prog.install(loc, i1);
	Object obj = prog.getElementAt(loc);
	assertEquals(new Integer(i1), obj);
	try { 
	    Instruction inst = prog.getInstructionAt(loc);
	    fail("Should raise a ClassCastException"); 
	} catch (ClassCastException e) { 
	}
    }

    public void testSerialization() {
	File tmpFile = null;

	initForEquals(prog);

	try {
	    tmpFile = File.createTempFile("ProgramTest", ".ser");
	    FileOutputStream fos = new FileOutputStream(tmpFile);
	    ObjectOutputStream out = new ObjectOutputStream(fos);
	    out.writeObject(prog);
	    out.close();
	    FileInputStream fis = new FileInputStream(tmpFile);
	    ObjectInputStream in = new ObjectInputStream(fis);
	    Program prog2 = (Program) in.readObject();
	    in.close();
	    assertEquals(prog, prog2);
	} catch (FileNotFoundException e) {
	    fail(e.toString());
	} catch (IOException e) {
	    fail(e.toString());
	} catch (ClassNotFoundException e) {
	    fail(e.toString());
	} finally {
	    tmpFile.delete();
	}
    }
}
