/*
  Copyright (C) 2002 William J. Yakowenko

  This file is part of the Karel World Editor.

  The Karel World Editor is free software; you can redistribute it 
  and/or modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  The Karel World Editor is distributed in the hope that it will be 
  useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
  of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with the Karel World Editor; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package karel.world;

import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class WorldEditor extends Frame {

    GlobalDataPanel gdp;

    WorldMap map;

    String directory;

    Frame helpFrame;

	
    public static void main( String argv[] ) {
	if (argv.length > 0) { new WorldEditor( argv[0] ); }
	else                 { new WorldEditor( null    ); }
    }

    public WorldEditor( String filename ) {
	super("Karel world editor");
		
	gdp = new GlobalDataPanel();
	map = new WorldMap();
	directory = null;
	helpFrame = null;
		
	gdp.setMap(map);
	map.setGlobalDataPanel(gdp);
		
	add( gdp, BorderLayout.NORTH );
	add( map, BorderLayout.CENTER );
		
	MenuBar mb = new MenuBar();
	Menu m = new Menu("File");
	MenuItem mi;
		
	mi = new MenuItem("Load",new MenuShortcut(KeyEvent.VK_L));
	mi.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) { load(); }
	    } );
	m.add(mi);
		
	mi = new MenuItem("Save",new MenuShortcut(KeyEvent.VK_S));
	mi.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) { save(); }
	    } );
	m.add(mi);
		
	mi = new MenuItem("Quit",new MenuShortcut(KeyEvent.VK_Q));
	mi.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) { quit(); }
	    } );
	m.add(mi);
		
	mb.add(m);
		
		
	m = new Menu("Help");
		
	mi = new MenuItem("Instructions",
			  new MenuShortcut(KeyEvent.VK_I));
	mi.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) { help(); }
	    } );
	m.add(mi);
		
	mi = new MenuItem("About",new MenuShortcut(KeyEvent.VK_A));
	mi.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) { about(); }
	    } );
	m.add(mi);
		
	mb.setHelpMenu(m);
		
	setMenuBar( mb );
		
	pack(); show();
		
	if ( filename != null ) { loadFile(filename); }
    }

    public void quit() {
	// Do this in a new thread, to avoid blocking
	// the GUI thread with the unsaved-changes
	// dialog.
	Thread t = new Thread() { public void run() { quit2(); } };
	t.start();
    }
    private void quit2() {
	if ( ! continueWithoutSaving("quit") ) { return; }
		
	System.exit(0);
    }

    public void load() {
	// Do this in a new thread, to avoid blocking
	// the GUI thread with the unsaved-changes
	// dialog.
	Thread t = new Thread() { public void run() { load2(); } };
	t.start();
    }
    private void load2() {
	if ( ! continueWithoutSaving("load another file") ) { return; }
		
	FileDialog d = new FileDialog(
				      this, "Load Karel World", FileDialog.LOAD );
	if (directory != null) {
	    d.setDirectory( directory );
	}
	d.setFilenameFilter(
			    new FilenameFilter() {
				public boolean accept(File dir, String name) {
				    return name.endsWith(".wld");
				}
			    }
			    );
	d.show();
	directory  = d.getDirectory();
	String filename = d.getFile();
	if (filename != null) {
	    if (directory != null) {
		filename = directory + "/" + filename;
	    }
	    loadFile( filename );
	}
    }
    private void loadFile( String filename ) {
	try {
	    File f = new File(filename);
	    FileReader fr = new FileReader(f);
	    BufferedReader br = new BufferedReader(fr);
	    map.load( br );
	    br.close();
	    fr.close();
	} catch (IOException ex) {
	    System.out.println(ex.toString());
	}
    }

    public void save() {
	FileDialog d = new FileDialog(
				      this, "Save Karel World", FileDialog.SAVE );
	d.show();
	d.setFilenameFilter(
			    new FilenameFilter() {
				public boolean accept(File dir, String name) {
				    return name.endsWith(".wld");
				}
			    }
			    );
	String filename = d.getFile();
	if (filename != null) {
	    try {
		File f = new File(filename);
		FileOutputStream fs = new FileOutputStream(f);
		PrintStream ps = new PrintStream( fs );
		map.save( ps );
		ps.close();
		fs.close();
	    } catch (IOException ex) {
		System.out.println(ex.toString());
	    }
	}
    }

    public boolean continueWithoutSaving( String op ) {
	// If there are no unsaved changes, there is no need to save
	if ( ! map.unsavedChanges() ) { return true; }
		
	// If there are unsaved changes, ask about it
	WaitingDialog d = new WaitingDialog("Unsaved changes");
	d.setBackground( Color.red );
	d.addLabel( "The world has unsaved changes." );
	d.addLabel( "Do you want to discard them?" );
	d.addButton( "Discard changes & "+op );   // ie: continue
	d.addButton( "Keep changes; don't "+op+" yet" );
	d.pack(); d.show();
	String answer = d.getSelectedButton();
	if (answer == null) { return false; }
	return answer.startsWith("Discard");
    }

    public void help() {
	showHelp(
		 "INSTRUCTIONS\n" +
		 "------------\n" +
		 "\n" +
			
		 "To add or remove a wall, just click on its center\n" +
		 "location. That will always be on a road, midway\n" +
		 "between two other roads.  If there was a wall\n" +
		 "there, it will be removed; if there was no wall\n" +
		 "there, it will be created.\n" +
		 "\n" +
			
		 "To move Karel to a new location, just click on the\n" +
		 "intersection. You can change Karel's direction by\n" +
		 "clicking on it again.\n" +
		 "\n" +
			
		 "To add a beeper, click on the intersection with\n" +
		 "the second mouse button. To remove one, click\n" +
		 "with the third.\n" +
		 "\n" +
			
		 "To change the size of the world (number of roads)\n" +
		 "or the number of beepers in Karel's beeper-bag,\n" +
		 "just enter the new numbers in the boxes at the top\n" +
		 "of the screen. Changes to the size of the world\n" +
		 "don't take effect until you click on the \"resize\n" +
		 "now\" button.  (Otherwise, the world size would\n" +
		 "change to 2 as soon as you began to enter \"24\").\n" +
		 "\n" +
			
		 "Finally, save the results in a file by choosing\n" +
		 "\"Save\" from the \"File\" menu and then selecting\n" +
		 "a filename; load a different file by choosing\n" +
		 "\"Load\" from that menu and selecting a filename;\n" +
		 "or end the program by selecting \"Quit\" from that\n" +
		 "menu. If you have made changes and not saved them,\n" +
		 "\"Load\" and \"Quit\" will ask you about it before\n" +
		 "continuing.\n" +
		 "\n"
		 );
    }

    public void about() {
	showHelp(
		 "ABOUT\n" +
		 "-----\n" +
		 "\n" +
			
		 "The Karel World Editor is free software; you can\n" +
		 "redistribute it and/or modify it under the terms\n" +
		 "of the GNU General Public License as published by\n" +
		 "the Free Software Foundation; either version 2\n" +
		 "of the License, or (at your option) any later\n" +
		 "version.\n" +
		 "\n" +
			
		 "The Karel World Editor is distributed in the hope\n" +
		 "that it will be useful, but WITHOUT ANY WARRANTY;\n" +
		 "without even the implied warranty of MERCHANTABILITY" +
		 "\nor FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
		 "GNU General Public License for more details.\n" +
		 "\n" +
			
		 "You should have received a copy of the GNU General\n" +
		 "Public License along with the Karel World Editor;\n" +
		 "if not, write to the Free Software Foundation,\n" +
		 "Inc., 59 Temple Place - Suite 330, Boston, MA\n" +
		 "02111-1307, USA.\n" +
		 "\n"
		 );
    }

    public void showHelp( String help ) {
	if (helpFrame != null) { hideHelp(); }
	helpFrame = new Frame("Help");
	helpFrame.removeAll();
	helpFrame.add( new TextArea(help,15,60), BorderLayout.CENTER );
		
	Button dismiss = new Button("dismiss");
	dismiss.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    hideHelp();
		}
	    } );
	helpFrame.add( dismiss, BorderLayout.SOUTH );
	helpFrame.pack();
	helpFrame.show();
    }

    public void hideHelp() {
	if (helpFrame != null) {
	    helpFrame.hide();
	    helpFrame.dispose();
	    helpFrame = null;
	}
    }

}

