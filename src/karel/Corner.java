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

public class Corner implements java.io.Serializable {

    static final long serialVersionUID = 3510713888408106866L;

    int avenue;
    int street;
    int numBeepers;
    boolean wallToNorth;
    boolean wallToEast;

    public Corner(int a, int s) {
	avenue = a;
	street = s;
    }
    
    public boolean equals(Object obj) {
	if (obj instanceof Corner) {
	    Corner that = (Corner) obj;
	    return (this.avenue == that.avenue
		    && this.street == that.street
		    && this.numBeepers == that.numBeepers
		    && this.wallToNorth == that.wallToNorth
		    && this.wallToEast == that.wallToEast);
	}
	return false;
    }

    public void setBeepers(int n) {
	numBeepers = n;
    }

    public int getBeepers() {
	return numBeepers;
    }

    public boolean hasNorthWall() {
	return wallToNorth;
    }

    public void setNorthWall(boolean wall) {
	wallToNorth = wall;
    }

    public boolean hasEastWall() {
	return wallToEast;
    }

    public void setEastWall(boolean wall) {
	wallToEast = wall;
    }
}
