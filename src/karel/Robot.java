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

import java.beans.*;

public class Robot {

    public static final int NORTH = 1;
    public static final int WEST = 2;
    public static final int SOUTH = 3;
    public static final int EAST = 4;

    public static final String LocationProperty = "Location";
    public static final String DirectionProperty = "Direction";
    public static final String BeepersProperty = "Beepers";

    static final boolean DEBUG = false;

    static final Integer defaultSleepInterval = new Integer(300);

    static int fSleepInterval;

    static {
	try {
	    String ms = System.getProperty("karel.sleepInterval",
					   defaultSleepInterval.toString());
	    fSleepInterval = Integer.parseInt(ms);
	} catch (NumberFormatException e) {
	    fSleepInterval = defaultSleepInterval.intValue();
	}
    };

    KPoint location;
    int direction;
    int beepers;
    World world;
    PropertyChangeSupport propChanger;
    Program fProgram;

    public static String directionToString(int d) {
	switch (d) {
	case NORTH:
	    return "North";
	case EAST:
	    return "East";
	case SOUTH:
	    return "South";
	case WEST:
	    return "West";
	default:
	    return "Unknown";
	}
    }

    public Robot() {
	propChanger = new PropertyChangeSupport(this);
	location = new KPoint(1, 1);
	direction = NORTH;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
	propChanger.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName,
					  PropertyChangeListener listener) {
	propChanger.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
	propChanger.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName,
					     PropertyChangeListener listener)
    {
	propChanger.removePropertyChangeListener(propertyName, listener);
    }

    public void setWorld(World w) {
	world = w;
	location.setAvenue(w.getRobotStartAvenue());
	location.setStreet(w.getRobotStartStreet());
	beepers = w.getRobotStartBeepers();
	direction = w.getRobotStartDirection();
    }

    public void setProgram(Program p) {
	fProgram = p;
    }

    public void setStreet(int s) {
	KPoint old = (KPoint) location.clone();
	location.setStreet(s);
	propChanger.firePropertyChange(LocationProperty, old, location);
    }

    /* Do we really need this method? */
    public int getStreet() {
	return location.getStreet();
    }

    public void setAvenue(int a) {
	KPoint old = (KPoint) location.clone();
	location.setAvenue(a);
	propChanger.firePropertyChange(LocationProperty, old, location);
    }

    /* Do we really need this method? */
    public int getAvenue() {
	return location.getAvenue();
    }

    public void setDirection(int d) {
	int old = direction;
	direction = d;
	propChanger.firePropertyChange(DirectionProperty, old, direction);
    }

    public int getDirection() {
	return direction;
    }

    /**
     * Returns the number of beepers in the beeper bag
     */
    public int getBeepers() {
	return beepers;
    }

    protected void setBeepers(int nBeepers) {
	int old = beepers;
	beepers = nBeepers;
	propChanger.firePropertyChange(BeepersProperty, old, beepers);
    }

    public void start() {
	KarelVM kvm = new KarelVM(this, fProgram);
	kvm.start();
    }

    /**
     * For backward compatibility.  Deprecate soon.
     * Used in Parser test code, and Trace test code.
     */
    public boolean execute(Program p) throws KarelException {
	fProgram = p;
	start();
	return true;
    }

    private void sleep() {
	try {
	    Thread.sleep(400);
	} catch (InterruptedException e) {
	}
    }

    private void sleep(int millis) {
	try {
	    Thread.sleep(millis);
	} catch (InterruptedException e) {
	}
    }

    //------------------------------------------------------------
    // Karel language primitives
    //------------------------------------------------------------

    public void turnleft() {
	switch (direction) {
	case NORTH:
	    setDirection(WEST);
	    break;
	case EAST:
	    setDirection(NORTH);
	    break;
	case SOUTH:
	    setDirection(EAST);
	    break;
	case WEST:
	    setDirection(SOUTH);
	    break;
	}

	sleep(fSleepInterval);
    }

    public void move()
	throws KarelOutOfBoundsException, KarelCrashedException
    {
	switch (direction)
	    {
	    case NORTH:
		if (! world.isInBounds(getAvenue(),
				       getStreet()+1))
		    throw new KarelOutOfBoundsException();
		else if (world.checkEWWall(getAvenue(),
					   getStreet()))
		    throw new KarelCrashedException();
		else
		    setStreet(getStreet()+1);
		break;
	    case EAST:
		if (! world.isInBounds(getAvenue()+1, getStreet()))
		    throw new KarelOutOfBoundsException();
		else if (world.checkNSWall(getAvenue(), getStreet()))
		    throw new KarelCrashedException();
		else
		    setAvenue(getAvenue()+1);
		break;
	    case SOUTH:
		if (! world.isInBounds(getAvenue(), getStreet()-1))
		    throw new KarelOutOfBoundsException();
		else if (world.checkEWWall(getAvenue(), getStreet()-1))
		    throw new KarelCrashedException();
		else
		    setStreet(getStreet()-1);
		break;
	    case WEST:
		if (! world.isInBounds(getAvenue()-1, getStreet()))
		    throw new KarelOutOfBoundsException();
		else if (world.checkNSWall(getAvenue()-1, getStreet()))
		    throw new KarelCrashedException();
		else
		    setAvenue(getAvenue()-1);
		break;
	    }

	sleep(fSleepInterval);
    }

    public void pickBeeper() throws KarelNoBeeperAtCornerException {
	world.pickBeeper(getAvenue(), getStreet());
	setBeepers(getBeepers() + 1);
    }

    /**
     * TODO: bounds checking on putBeeper!  Is beeper bag empty?
     */
    public void putBeeper() {
	setBeepers(getBeepers() - 1);
	world.putBeeper(getAvenue(), getStreet());
    }

    public void turnoff() {
    }

    public boolean isDirBlocked(int direction) {
	switch (direction) {
	case Robot.NORTH:
	    return world.checkEWWall(getAvenue(), getStreet());

	case Robot.EAST:
	    return world.checkNSWall(getAvenue(), getStreet());

	case Robot.SOUTH:
	    if (getStreet() == 1)
		return true;
	    else
		return world.checkEWWall(getAvenue(), getStreet() - 1);

	case Robot.WEST:
	    if (getAvenue() == 1)
		return true;
	    else
		return world.checkNSWall(getAvenue() - 1, getStreet());
	default:
	    throw new RuntimeException("Unknown direction " + direction);
	}
    }

    public boolean anyBeepersInBeeperBag() {
	return (beepers > 0);
    }

    public boolean facingEast() {
	return (direction == EAST);
    }

    public boolean facingNorth() {
	return (direction == NORTH);
    }

    public boolean facingSouth() {
	return (direction == SOUTH);
    }

    public boolean facingWest() {
	return (direction == WEST);
    }

    public boolean frontIsBlocked() {
	return isDirBlocked(direction);
    }

    public boolean frontIsClear() {
	return (! isDirBlocked(direction));
    }

    public boolean leftIsBlocked() {
	switch (direction) {
	case NORTH:
	    return isDirBlocked(WEST);
	case EAST:
	    return isDirBlocked(NORTH);
	case SOUTH:
	    return isDirBlocked(EAST);
	case WEST:
	    return isDirBlocked(SOUTH);
	default:
	    throw new RuntimeException("Unknown direction " + direction);
	}
    }

    public boolean leftIsClear() {
	return (! leftIsBlocked());
    }

    public boolean nextToABeeper() {
	return world.checkBeeper(getAvenue(), getStreet());
    }

    public boolean noBeepersInBeeperBag() {
	return (beepers == 0);
    }

    public boolean notFacingEast() {
	return (! facingEast());
    }

    public boolean notFacingNorth() {
	return (! facingNorth());
    }

    public boolean notFacingSouth() {
	return (! facingSouth());
    }

    public boolean notFacingWest() {
	return (! facingEast());
    }

    public boolean notNextToABeeper() {
	return (! nextToABeeper());
    }

    public boolean rightIsBlocked() {
	switch (direction) {
	case NORTH:
	    return isDirBlocked(EAST);
	case EAST:
	    return isDirBlocked(SOUTH);
	case SOUTH:
	    return isDirBlocked(WEST);
	case WEST:
	    return isDirBlocked(NORTH);
	default:
	    throw new RuntimeException("Unknown direction " + direction);
	}
    }

    public boolean rightIsClear() {
	return (! rightIsBlocked());
    }
}
