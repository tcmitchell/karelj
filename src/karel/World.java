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

import java.io.*;

public class World implements java.io.Serializable {

    static final long serialVersionUID = 8951314414527333696L;

    int numAvenues;
    int numStreets;
    Corner corners[][];
    int robotStartAve;
    int robotStartSt;
    int robotStartDir;
    int robotStartBeepers;

    public World(int avenues, int streets) {
	numAvenues = avenues;
	numStreets = streets;
	corners = new Corner[avenues][streets];
	for (int a=0; a<avenues; a++)
	    for (int s=0; s<streets; s++)
		corners[a][s] = new Corner(a+1, s+1);
	addEWWall(1, streets, avenues);
	addNSWall(avenues, 1, streets);
    }

    public boolean equals(Object obj) {
	if (obj instanceof World) {
  	    World that = (World) obj;
  	    if (this.numAvenues == that.numAvenues
		&& this.numStreets == that.numStreets) {

		for (int a=1; a<=numAvenues; a++)
		    for (int s=1; s<=numStreets; s++)
			if (! this.getCorner(a, s).equals(that.getCorner(a,s))) {
			    return false;
			}
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public int getAvenues() {
	return numAvenues;
    }

    public int getStreets() {
	return numStreets;
    }

    protected Corner getCorner(int avenue, int street) {
	return corners[avenue-1][street-1];
    }

    public void setRobotStartAvenue(int avenue) {
	robotStartAve = avenue;
    }

    public int getRobotStartAvenue() {
	return robotStartAve;
    }

    public void setRobotStartStreet(int street) {
	robotStartSt = street;
    }

    public int getRobotStartStreet() {
	return robotStartSt;
    }

    public void setRobotStartDirection(int dir) {
	robotStartDir = dir;
    }

    public int getRobotStartDirection() {
	return robotStartDir;
    }

    public void setRobotStartBeepers(int beepers) {
	robotStartBeepers = beepers;
    }

    public int getRobotStartBeepers() {
	return robotStartBeepers;
    }

    public boolean checkEWWall(int atAvenue, int northOfStreet) {
	return getCorner(atAvenue, northOfStreet).hasNorthWall();
    }

    public boolean checkNSWall(int eastOfAvenue, int atStreet) {
	return getCorner(eastOfAvenue, atStreet).hasEastWall();
    }

    public void addEWWall(int startAvenue, int northOfStreet, int nBlocks) {
	for (int i=0; i<nBlocks; i++) {
	    Corner c = getCorner(startAvenue + i, northOfStreet);
	    c.setNorthWall(true);
	}
    }

    public void addNSWall(int eastOfAvenue, int startStreet, int nBlocks) {
	for (int i=0; i<nBlocks; i++) {
	    Corner c = getCorner(eastOfAvenue, startStreet + i);
	    c.setEastWall(true);
	}
    }

    public boolean isInBounds(int avenue, int street) {
	if (0 < street && street <= numStreets
	    && 0 < avenue && avenue <= numAvenues) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean checkBeeper(int avenue, int street) {
	Corner c = getCorner(avenue, street);
	return (c.getBeepers() > 0);
    }

    public boolean pickBeeper(int avenue, int street)
    throws KarelNoBeeperAtCornerException {
	Corner c = getCorner(avenue, street);
	int nBeepers = c.getBeepers();
	if (nBeepers > 0) {
	    c.setBeepers(nBeepers-1);
	    return true;
	} else {
	    throw new KarelNoBeeperAtCornerException();
	}
    }

    public boolean putBeeper(int avenue, int street) {
	Corner c = getCorner(avenue, street);
	c.setBeepers(c.getBeepers()+1);
	return true;
    }
}
