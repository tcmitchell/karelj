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


public class WaitingDialog extends Frame {

    Button selected;

    GridBagConstraints gbc;

    GridBagLayout gb;

    int buttonCount;

	
    public WaitingDialog( String title ) {
	super(title);
	setLayout( gb = new GridBagLayout() );
	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	selected = null;
	buttonCount = 0;
	addLabel(" "); // spacing above message(s)
    }

    public void addLabel( String s ) {
	Label m = new Label(s);
	gb.setConstraints( m, gbc );
	add( m );
    }

    public void addButton( String s ) {
	// add space between labels & buttons
	if (buttonCount == 0) { addLabel(" "); }
	gbc.insets = new Insets( 0, 10, 10, 10 );
		
	buttonCount += 1;
	Button b = new Button(s);
	gb.setConstraints( b, gbc );
	add( b );
	b.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    selectButton( (Button)e.getSource() );
		}
	    } );
    }

    public synchronized String getSelectedButton() {
	while (selected == null) {
	    try { wait(); }
	    catch (InterruptedException x) { return null; }
	}
	return selected.getLabel();
    }

    private synchronized void selectButton( Button b ) {
	selected = b;
	hide();
	notifyAll();
    }
}

