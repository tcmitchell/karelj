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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.*;

public class Tracer implements PropertyChangeListener {

    public static void main(String args[])
	throws IOException, ClassNotFoundException,
	       WorldParserException, KarelException
    {
	System.out.println("Hello, World!");

	Robot r = new Robot();
	Program p = null;
	World w = null;

	FileInputStream fis = new FileInputStream(args[0]);
	ObjectInputStream progIn = new ObjectInputStream(fis);
	p = (Program) progIn.readObject();
	progIn.close();
	p.print();

	FileReader fr = new FileReader(args[1]);
	WorldParser parser = new WorldParser();
	w = parser.parse(fr);

	r.addPropertyChangeListener(new Tracer());
	r.setWorld(w);
	r.execute(p);
    }

    public void propertyChange(PropertyChangeEvent evt) {
	String prop = evt.getPropertyName();

	if (Robot.DirectionProperty.equals(prop)) {
	    int oldVal = ((Integer) evt.getOldValue()).intValue();
	    int newVal = ((Integer) evt.getNewValue()).intValue();
	    System.out.println(prop + " changed from "
			       + Robot.directionToString(oldVal) + " to "
			       + Robot.directionToString(newVal));
	} else {
	    System.out.println(prop + " changed from "
			       + evt.getOldValue() + " to "
			       + evt.getNewValue());
	}
    }
}
