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

import junit.framework.*;

/**
 * TestSuite that runs all the sample tests
 *
 */
public class AllTests {

    public static void main (String[] args) {
	junit.textui.TestRunner.run (suite());
    }

    public static Test suite ( ) {
	TestSuite suite= new TestSuite("All Karel Tests");
	suite.addTest(ProgramTest.suite());
	suite.addTest(WorldTest.suite());
	suite.addTest(RobotTest.suite());
	suite.addTest(LexerTest.suite());
	return suite;
    }
}
