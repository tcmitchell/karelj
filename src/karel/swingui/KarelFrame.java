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

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import karel.*;

public class KarelFrame
    extends JFrame
    implements ActionListener
{

    protected static final String fQuitCommand = "Quit";
    protected static final String fLoadWorldCommand = "Load World...";
    protected static final String fLoadProgramCommand = "Load Program...";
    protected static final String fRunCommand = "Run";

    /**
     * public temporarily for silk.
     */
    public WorldPanel fWorldPanel;

    private JFileChooser fWorldFileChooser;

    private JFileChooser fProgramFileChooser;

    /**
     * public temporarily for silk.
     */
    public Robot fRobot;

    public void loadWorld() {
	if (fWorldFileChooser == null) {
	    fWorldFileChooser
		= new JFileChooser(System.getProperty("user.dir"));
	    fWorldFileChooser.setDialogTitle("Choose A Karel World To Open");
	    ExampleFileFilter filter = new ExampleFileFilter();
	    filter.addExtension("wld");
	    filter.setDescription("Karel World Files");
	    fWorldFileChooser.setFileFilter(filter);
	}

	// Note: source for ExampleFileFilter can be found in FileChooserDemo,
	// under the demo/jfc directory in the Java 2 SDK, Standard Edition.
	int returnVal = fWorldFileChooser.showOpenDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    loadWorld(fWorldFileChooser.getSelectedFile());
	}
    }

    public void loadWorld(File f) {
	try {
	    loadWorld(new FileReader(f));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    public void loadWorld(Reader r) {
	try {
	    fWorldPanel.setWorld(new WorldParser().parse(r));
	    fWorldPanel.repaint();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WorldParserException e) {
	    e.printStackTrace();
	}
    }

    public void loadProgram() {
	if (fProgramFileChooser == null) {
	    fProgramFileChooser
		= new JFileChooser(System.getProperty("user.dir"));
	    fProgramFileChooser.setDialogTitle("Choose A Karel Program To Open");
	    ExampleFileFilter filter = new ExampleFileFilter();
	    filter.addExtension("ser");
	    filter.setDescription("Karel Program Files");
	    fProgramFileChooser.setFileFilter(filter);
	}

	int returnVal = fProgramFileChooser.showOpenDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    loadProgram(fProgramFileChooser.getSelectedFile());
	}
    }

    public void loadProgram(File f) {
	try {
	    loadProgram(new FileInputStream(f));
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    public void loadProgram(InputStream is) {
	try {
	    ObjectInputStream in = new ObjectInputStream(is);
	    Program prog = (Program) in.readObject();
	    in.close();
	    fRobot.setProgram(prog);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    protected void initMenuBar() {
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");

	JMenuItem loadWorld = new JMenuItem(fLoadWorldCommand);
	loadWorld.addActionListener(this);
	fileMenu.add(loadWorld);

	JMenuItem loadProgram = new JMenuItem(fLoadProgramCommand);
	loadProgram.addActionListener(this);
	fileMenu.add(loadProgram);

	JMenuItem run = new JMenuItem(fRunCommand);
	run.addActionListener(this);
	fileMenu.add(run);

	JMenuItem quit = new JMenuItem(fQuitCommand);
	quit.addActionListener(this);
	fileMenu.add(quit);

	menuBar.add(fileMenu);
	setJMenuBar(menuBar);
    }

    public KarelFrame(String name) {
	super(name);
	initMenuBar();
	fWorldPanel = new WorldPanel();
	getContentPane().add(fWorldPanel);

	fRobot = new Robot();
	fWorldPanel.setRobot(fRobot);
    }

    public void actionPerformed(ActionEvent evt) {
	String cmd = evt.getActionCommand();
	if (fLoadWorldCommand.equals(cmd)) {
	    loadWorld();
	} else if (fLoadProgramCommand.equals(cmd)) {
	    loadProgram();
	} else if (fRunCommand.equals(cmd)) {
	    fRobot.start();
	} else if (fQuitCommand.equals(cmd)) {
	    System.exit(0);
	} else {
	    System.out.println("actionPerformed: unknown command " + cmd);
	}
    }
}
