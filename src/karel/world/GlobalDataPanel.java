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


public class GlobalDataPanel extends Panel {

    WorldMap map;

    BeeperGui beeperGui;

    SizeGui sizeGui;
	

    public GlobalDataPanel() {
	beeperGui = new BeeperGui(3);
	sizeGui = new SizeGui(10,10);
	setLayout( new GridLayout(1,0) );
	add( beeperGui );
	add( sizeGui );
    }

    public void setMap( WorldMap m ) {
	map = m;
    }

    public int getBeepersInBag() { return beeperGui.getBeepers(); }
    public void setBeepersInBag(int b) { beeperGui.setBeepers(b); }
    public Dimension getWorldSize() { return sizeGui.getSize(); }
    public void setWorldSize(int x, int y) { sizeGui.setSize(x,y); }

    public class BeeperGui extends Panel {
	TextField t;
	public BeeperGui(int b) {
	    add( new Label("Beepers in bag:") );
	    t = new TextField(5); add(t); t.setText(""+b);
			
	    t.addTextListener( new TextListener() {
		    public void textValueChanged(TextEvent e) {
			map.setRobotBeepers( getBeepers() );
		    }
		} );
	}
	public void setBeepers(int b) {
	    t.setText(""+b);
	}
	public int getBeepers() {
	    try { return Integer.parseInt( t.getText() ); }
	    catch (NumberFormatException x) { return 0; }
	}
    }

    public class SizeGui extends Panel {
	TextField w,h;
	public SizeGui(Dimension d) {
	    this( (int)d.getWidth(), (int)d.getHeight() );
	}
	public SizeGui(int x, int y) {
	    add( new Label("World size:") );
			
	    w = new TextField(2);
	    w.setText(""+x);
	    add(w);
			
	    h = new TextField(2);
	    h.setText(""+y);
	    add(h);
			
	    Button b = new Button("Resize now");
	    b.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			map.setWorldSize( getSize() );
		    }
		} );
	    add(b);
	}
	public void setSize(int x, int y) {
	    w.setText(""+x); h.setText(""+y);
	}
	public Dimension getSize() {
	    int width, height;
			
	    try { width = Integer.parseInt( w.getText() ); }
	    catch (NumberFormatException x) { width = 0; }
			
	    try { height = Integer.parseInt( h.getText() ); }
	    catch (NumberFormatException x) { height = 0; }
			
	    return new Dimension( width, height );
	}
    }

}

