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
import karel.World;
import karel.WorldParser;
import karel.WorldParserException;
import karel.KarelNoBeeperAtCornerException;

/**
 *
 */
public class WorldTest extends TestCase {

    protected World fWorld;
    protected int fAvenues = 7;
    protected int fStreets = 5;

    public WorldTest(String name) {
	super(name);
    }

    public static void main (String[] args) {
	junit.textui.TestRunner.run (suite());
    }

    public static Test suite() {
	return new TestSuite(WorldTest.class);
    }

    protected void setUp() {
	fWorld = new World(fAvenues, fStreets);
    }

    public void testBounds() {
	assert("isInBounds()", fWorld.isInBounds(fAvenues, fStreets));
	assert("isInBounds()", ! fWorld.isInBounds(fAvenues + 1,
						   fStreets + 1));
    }

    public void testBeepers() {
	assert(!fWorld.checkBeeper(fAvenues, fStreets));
	assert(fWorld.putBeeper(fAvenues, fStreets));
	assert(fWorld.checkBeeper(fAvenues, fStreets));
	try {
	    fWorld.pickBeeper(fAvenues, fStreets);
	} catch (KarelNoBeeperAtCornerException e) {
	    fail("Unexpected KarelNoBeeperAtCornerException");
	}
	assert(!fWorld.checkBeeper(fAvenues, fStreets));
	try {
	    fWorld.pickBeeper(fAvenues, fStreets);
	    fail("Expected KarelNoBeeperAtCornerException");
	} catch (KarelNoBeeperAtCornerException e) {
	}
    }

    /**
     * Make sure there are walls to the east and north of the
     * world.  This was a bug, hence a test.
     */
    public void testWalls() {
  	for (int a=1; a<fAvenues; a++) {
  	    assert(fWorld.checkEWWall(a, fStreets));
  	}
	for (int s=1; s<fStreets; s++) {
  	    assert(fWorld.checkNSWall(fAvenues, s));
	}
    }

    public void testEWWall() {
	int startAve = 1;
	int northOfStreet = 1;
	int nBlocks = 1;
	fWorld.addEWWall(startAve, northOfStreet, nBlocks);

	for (int i=0; i<nBlocks; i++) {
	    assert(fWorld.checkEWWall(startAve + i, northOfStreet));
	}
	assert( ! fWorld.checkEWWall(startAve + nBlocks, northOfStreet));
    }

    public void testNSWall() {
	int eastOfAve = 1;
	int startStreet = 1;
	int nBlocks = 1;
	fWorld.addNSWall(eastOfAve, startStreet, nBlocks);

	for (int i=0; i<nBlocks; i++) {
	    assert(fWorld.checkNSWall(eastOfAve, startStreet + i));
	}
	assert( ! fWorld.checkNSWall(eastOfAve, startStreet + nBlocks));
    }

    protected void initForEquals(World w) {
    }

    public void testEquals() {
	World world2 = new World(fAvenues, fStreets);

	initForEquals(fWorld);
	initForEquals(world2);
	assertEquals("World.equals method failed:", fWorld, world2);
    }

    public void testSerialization() {
	File tmpFile = null;

	initForEquals(fWorld);

	try {
	    tmpFile = File.createTempFile("WorldTest", ".ser");
	    FileOutputStream fos = new FileOutputStream(tmpFile);
	    ObjectOutputStream out = new ObjectOutputStream(fos);
	    out.writeObject(fWorld);
	    out.close();
	    FileInputStream fis = new FileInputStream(tmpFile);
	    ObjectInputStream in = new ObjectInputStream(fis);
	    World world2 = (World) in.readObject();
	    in.close();
	    assertEquals("World serialization failed:", fWorld, world2);
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

    public void testParser() {
	String input =
	    "World 5 5\n"
	    + "Beepers 3 3 1\n"
	    + "Robot 4 3 1 0\n"
	    + "Wall 2 2 1\n"
	    + "Wall 3 2 1\n"
	    + "Wall 1 1 4\n"
	    + "Wall 2 1 4\n"
	    + "Wall 2 2 4\n"
	    + "Wall 3 1 4\n"
	    + "Wall 3 2 4\n"
	    + "Wall 3 3 4\n"
	    + "Wall 4 1 4\n"
	    + "Wall 4 2 4\n"
	    + "Wall 4 3 4\n"
	    + "Wall 4 4 4";

	StringReader sr = new StringReader(input);
	WorldParser parser = new WorldParser();
	try {
	    fWorld = parser.parse(sr);
	    assert(fWorld instanceof World);
	    try {
		assert(fWorld.pickBeeper(3, 3));
		fWorld.pickBeeper(3, 3);
		fail("Expected KarelNoBeeperAtCornerException");
	    } catch (KarelNoBeeperAtCornerException e) {
	    }
	    assertEquals(4, fWorld.getRobotStartAvenue());
	    assertEquals(3, fWorld.getRobotStartStreet());
	    assertEquals(1, fWorld.getRobotStartDirection());
	    assertEquals(0, fWorld.getRobotStartBeepers());
	    assert(fWorld.checkEWWall(2, 2));
	    assert(fWorld.checkEWWall(3, 2));
	    assert(fWorld.checkNSWall(1, 1));
	    assert(fWorld.checkNSWall(2, 1));
	    assert(fWorld.checkNSWall(2, 2));
	    assert(fWorld.checkNSWall(3, 1));
	    assert(fWorld.checkNSWall(3, 2));
	    assert(fWorld.checkNSWall(3, 3));
	    assert(fWorld.checkNSWall(4, 1));
	    assert(fWorld.checkNSWall(4, 2));
	    assert(fWorld.checkNSWall(4, 3));
	    assert(fWorld.checkNSWall(4, 4));
	    sr.close();
	} catch (WorldParserException e) {
	    fail(e.toString());
	} catch (IOException e) {
	    fail(e.toString());
	}
    }

    public void testComments() {
	String input =
	    "# This is a comment\n"
	    + "World 5 5\n"
	    + "# and another comment\n"
	    + "Beepers 3 3 1 # and an inline comment\n"
	    + "Robot 4 3 1 0\n"
	    + "Wall 2 2 1\n"
	    + "Wall 3 2 1\n"
	    + "Wall 1 1 4\n"
	    + "Wall 2 1 4\n"
	    + "Wall 2 2 4\n"
	    + "Wall 3 1 4\n"
	    + "Wall 3 2 4\n"
	    + "Wall 3 3 4\n"
	    + "Wall 4 1 4\n"
	    + "Wall 4 2 4\n"
	    + "Wall 4 3 4\n"
	    + "Wall 4 4 4";

	StringReader sr = new StringReader(input);
	WorldParser parser = new WorldParser();
	try {
	    fWorld = parser.parse(sr);
	    assert(fWorld instanceof World);
	    try {
		assert(fWorld.pickBeeper(3, 3));
		fWorld.pickBeeper(3, 3);
		fail("Expected KarelNoBeeperAtCornerException");
	    } catch (KarelNoBeeperAtCornerException e) {
	    }
	    assertEquals(4, fWorld.getRobotStartAvenue());
	    assertEquals(3, fWorld.getRobotStartStreet());
	    assertEquals(1, fWorld.getRobotStartDirection());
	    assertEquals(0, fWorld.getRobotStartBeepers());
	    assert(fWorld.checkEWWall(2, 2));
	    assert(fWorld.checkEWWall(3, 2));
	    assert(fWorld.checkNSWall(1, 1));
	    assert(fWorld.checkNSWall(2, 1));
	    assert(fWorld.checkNSWall(2, 2));
	    assert(fWorld.checkNSWall(3, 1));
	    assert(fWorld.checkNSWall(3, 2));
	    assert(fWorld.checkNSWall(3, 3));
	    assert(fWorld.checkNSWall(4, 1));
	    assert(fWorld.checkNSWall(4, 2));
	    assert(fWorld.checkNSWall(4, 3));
	    assert(fWorld.checkNSWall(4, 4));
	    sr.close();
	} catch (WorldParserException e) {
	    fail(e.toString());
	} catch (IOException e) {
	    fail(e.toString());
	}
    }
}
