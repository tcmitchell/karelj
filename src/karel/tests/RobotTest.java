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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import junit.framework.*;
import karel.Robot;
import karel.KPoint;
import karel.World;
import karel.KarelNoBeeperAtCornerException;

/**
 *
 */
public class RobotTest extends TestCase {

    Robot fRobot;

    public RobotTest(String name) {
	super(name);
    }

    public static void main (String[] args) {
	junit.textui.TestRunner.run (suite());
    }

    public static Test suite() {
	return new TestSuite(RobotTest.class);
    }

    protected void setUp() {
	fRobot = new Robot();
    }

    public void testLocation() {
	int ave = 3;
	int st = 4;
	fRobot.setAvenue(ave);
	fRobot.setStreet(st);
	assertEquals(ave, fRobot.getAvenue());
	assertEquals(st, fRobot.getStreet());
    }

    public void testDirection() {
	fRobot.setDirection(Robot.NORTH);
	assertEquals(Robot.NORTH, fRobot.getDirection());
	fRobot.turnleft();
	assertEquals(Robot.WEST, fRobot.getDirection());
	fRobot.turnleft();
	assertEquals(Robot.SOUTH, fRobot.getDirection());
	fRobot.turnleft();
	assertEquals(Robot.EAST, fRobot.getDirection());
	fRobot.turnleft();
	assertEquals(Robot.NORTH, fRobot.getDirection());
    }

    public void testLocationProperty() {
	RecordingPropertyChangeListener listener
	    = new RecordingPropertyChangeListener();
	fRobot.addPropertyChangeListener(listener);
	fRobot.setAvenue(3);
	PropertyChangeEvent evt = listener.getFirstEvent();
	assertNotNull(evt);
	assertEquals(fRobot, evt.getSource());

	assertEquals(Robot.LocationProperty, evt.getPropertyName());

	KPoint oldVal = new KPoint(1, 1);
	assertEquals(oldVal, evt.getOldValue());

	KPoint newVal = new KPoint(3, 1);
	assertEquals(newVal, evt.getNewValue());
    }

    public void testDirectionProperty() {
	RecordingPropertyChangeListener listener
	    = new RecordingPropertyChangeListener();
	fRobot.addPropertyChangeListener(listener);
	fRobot.setDirection(Robot.EAST);
	PropertyChangeEvent evt = listener.getFirstEvent();
	assertNotNull(evt);
	assertEquals(fRobot, evt.getSource());

	assertEquals(Robot.DirectionProperty, evt.getPropertyName());

	Integer oldVal = new Integer(Robot.NORTH);
	assertEquals(oldVal, evt.getOldValue());

	Integer newVal = new Integer(Robot.EAST);
	assertEquals(newVal, evt.getNewValue());
    }

    public void testBeepersProperty() {
	RecordingPropertyChangeListener listener
	    = new RecordingPropertyChangeListener();
	World world = new World(1, 1);
	world.putBeeper(1, 1);
	fRobot.setWorld(world);
	fRobot.setAvenue(1);
	fRobot.setStreet(1);

	fRobot.addPropertyChangeListener(listener);
	try {
	    fRobot.pickBeeper();
	} catch (KarelNoBeeperAtCornerException e) {
	    fail("pickBeeper failed");
	}
	PropertyChangeEvent evt = listener.getFirstEvent();
	assertNotNull(evt);
	assertEquals(fRobot, evt.getSource());

	assertEquals(Robot.BeepersProperty, evt.getPropertyName());

	Integer oldVal = new Integer(0);
	assertEquals(oldVal, evt.getOldValue());

	Integer newVal = new Integer(1);
	assertEquals(newVal, evt.getNewValue());
    }
}
