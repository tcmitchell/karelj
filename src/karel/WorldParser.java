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

public class WorldParser {

    World fWorld;
    StreamTokenizer fTokenizer;

    public WorldParser() {
    }
    
    public World parse(Reader r)
	throws IOException, WorldParserException {

	StreamTokenizer tizer = new StreamTokenizer(r);
  	tizer.commentChar('#');
	int ttype;

	while ((ttype = tizer.nextToken()) != StreamTokenizer.TT_EOF) {
	    if (ttype == StreamTokenizer.TT_WORD) {
		if ("World".equals(tizer.sval)) {
		    fWorld = readWorld(tizer);
		} else if ("Beepers".equals(tizer.sval)) {
		    if (fWorld == null) {
			throw new WorldParserException("Beepers command before World command at line " + tizer.lineno());
		    } else {
			readBeepers(tizer);
		    }
		} else if ("Robot".equals(tizer.sval)) {
		    if (fWorld == null) {
			throw new WorldParserException("Robot command before World command at line " + tizer.lineno());
		    } else {
			readRobot(tizer);
		    }
		} else if ("Wall".equals(tizer.sval)) {
		    if (fWorld == null) {
			throw new WorldParserException("Robot command before World command at line " + tizer.lineno());
		    } else {
			readWall(tizer);
		    }
		}
	    } else {
		throw new WorldParserException("Parser error at line "
					       + tizer.lineno());
	    }
	}
	return fWorld;
    }
	
    protected int getInteger(StreamTokenizer tizer,
				    String errorMessage)
	throws IOException, WorldParserException {

	int ttype;

	ttype = tizer.nextToken();
	if (ttype != StreamTokenizer.TT_NUMBER) {
	    throw new WorldParserException(errorMessage + " at line "
					   + tizer.lineno());
	} else {
	    return (int) tizer.nval;
	}
    }

    protected World readWorld(StreamTokenizer tizer)
	throws IOException, WorldParserException {
	int ttype;

	int avenues
	    = getInteger(tizer, "No avenues specified in World directive");

	int streets
	    = getInteger(tizer, "No streets specified in World directive");

	return new World(avenues, streets);
    }

    protected void readBeepers(StreamTokenizer tizer)
	throws IOException, WorldParserException {
	int ttype;

	int avenue = getInteger(tizer,
				"No avenue specified in Beepeers directive");

	int street = getInteger(tizer,
				"No street specified in Beepers directive");

	int nBeepers = getInteger(tizer,
				  "No beepers specified in Beepers directive");
	for (int i=0; i<nBeepers; i++) {
	    fWorld.putBeeper(avenue, street);
	}
    }

    protected void readRobot(StreamTokenizer tizer)
	throws IOException, WorldParserException {
	int ttype;

	int avenue
	    = getInteger(tizer, "No avenue specified in Robot directive");

	int street
	    = getInteger(tizer, "No street specified in Robot directive");

	int direction
	    = getInteger(tizer, "No direction specified in Robot directive");

	int beepers
	    = getInteger(tizer, "No beepers specified in Robot directive");

	fWorld.setRobotStartAvenue(avenue);
	fWorld.setRobotStartStreet(street);
	fWorld.setRobotStartDirection(direction);
	fWorld.setRobotStartBeepers(beepers);
    }

    protected void readWall(StreamTokenizer tizer)
	throws IOException, WorldParserException {
	int ttype;

	int avenue = getInteger(tizer,
				"No avenue specified in Wall directive");

	int street = getInteger(tizer,
				"No street specified in Wall directive");

	int direction = getInteger(tizer,
				  "No beepers specified in Wall directive");

	if (direction == Robot.NORTH) {
	    fWorld.addEWWall(avenue, street, 1);
	} else if (direction == Robot.EAST) {
	    fWorld.addNSWall(avenue, street, 1);
	} else {
	    throw new WorldParserException("Invalid Wall Location "
					   + direction + " at line "
					   + tizer.lineno());
	}
    }
}
