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

package karel.swingui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.*;

public class Main {

    public static void main(String args[]) {
	File worldFile = null;
	File progFile = null;

	int i=0;
	while (i<args.length) {
	    if (args[i].equals("-w")) {
		worldFile = new File(args[++i]);
		i++;
	    } else if (args[i].equals("-p")) {
		progFile = new File(args[++i]);
		i++;
	    } else {
		System.out.println("usage: Main [-w world] [-p program]");
		System.exit(1);
	    }
	}

	KarelFrame frame = new KarelFrame("karel");

	// When the user clicks the close window button on the frame,
	// we need to shut down.
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent evt) {
	      System.exit(0);
	    }
	  }
				);

	frame.setSize(300, 300);
	frame.show();
	if (worldFile != null) frame.loadWorld(worldFile);
	if (progFile != null) frame.loadProgram(progFile);
    }
}
