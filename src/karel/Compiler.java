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

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/*
 * To Do:
 *
 * - accept '-d' as a command line arg indicating output directory
 *     kc -d testdata/results testdata/programs/iterate.k
 *
 *     yields testdata/results/iterate.ser
 *
 *
 * - Make this class friendly to run from another class.  Invoke it
 *   from CompilerTest instead of having CompilerTest invoke the parser.
 *
 */

/**
 *
 */
public class Compiler {
    public static void main (String[] args) {
	String infile = args[0];
	String outfile = args[1];

	Parser parser = new Parser();
	
	try {
	    Program prog = parser.parse(infile);
//  	    prog.print();
	    FileOutputStream fos = new FileOutputStream(outfile);
	    ObjectOutputStream out = new ObjectOutputStream(fos);
	    out.writeObject(prog);
	    out.flush();
	    out.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
