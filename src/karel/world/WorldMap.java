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
import java.util.*;


public class WorldMap extends Panel implements MouseListener {

    int width, height, // size of world in number of roads
	kx, ky, kd, kb; // karel position (x,y), direction, # beepers
    Intersection location[][];
    Dimension preferredSize;
    GlobalDataPanel gdp;
    boolean changed;
	
    public WorldMap() {
	width = height = 10;
	kx = ky = kd = 1;
	kb = 0;
	location = newGrid(width,height);
	addMouseListener( this );
	preferredSize = new Dimension(512,512);
	changed = false;
    }

    public void setGlobalDataPanel( GlobalDataPanel p ) {
	gdp = p;
	gdp.setWorldSize(width,height);
	gdp.setBeepersInBag(kb);
    }

    public void paint(Graphics g) {
	int x, x0, x1, y, y0, y1; // position, min, max, on each axis
	x0 = 0; x1 = getWidth();
	y0 = 0; y1 = getHeight();
	int w = x1 / width;  // width of one column, in pixels
	int h = y1 / height; // height of one row, in pixels
		
	// background is white
	setBackground( Color.white );
		
	// draw roads
	g.setColor( Color.gray );
	for (int i=1; i<=width; i++) {
	    x = x0 + (int)((i-0.5)*w);
	    g.drawLine( x,y0, x,y1 );
	}
	for (int i=1; i<=height; i++) {
	    y = y0 + (int)(((height+1-i)-0.5)*h);
	    g.drawLine( x0,y, x1,y );
	}
		
	// draw walls
	g.setColor( Color.black );
	for (int i=1; i<=width; i++) {
	    for (int j=1; j<=height; j++) {
		if (location[i][j].wallNorth) {
		    x = x0 + (i-1)*w;
		    y = y0 + (height-j)*h;
		    g.fillRect( x,y, w,(h+19)/20 );
		}
		if (location[i][j].wallEast) {
		    x = x0 + (i)*w;
		    y = y0 + (height-j)*h;
		    g.fillRect( x,y, (w+19)/20,h );
		}
	    }
	}
		
	// draw karel
	if ( kx >= 1 && kx <= width
	     &&   ky >= 1 && ky <= height
	     &&   kd >= 1 && kd <= 4 ) {
	    x = x0 + (int)((kx-0.5)*w);
	    y = y0 + (int)(((height+1-ky)-0.5)*h);
	    g.setColor( Color.blue );
	    g.fillRect( (x-w/4),(y-h/4), w/2, h/2 );
	    g.setColor( Color.red );
	    switch (kd) {
	    case 1: // facing North
		g.fillRect( (x-w/4),(y-h/4), w/2, h/7 );
		break;
	    case 2: // facing West
		g.fillRect( (x-w/4),(y-h/4), w/7, h/2 );
		break;
	    case 3: // facing South
		g.fillRect( (x-w/4),(y+h/8), w/2, h/7 );
		break;
	    case 4: // facing East
		g.fillRect( (x+w/8),(y-h/4), w/7, h/2 );
		break;
	    }
	}
		
	// draw # of beepers
	for (int i=1; i<=width; i++) {
	    x = x0 + (int)((i-0.5)*w);
	    for (int j=1; j<=height; j++) {
		y = y0 + (int)((height+1-j-0.5)*h);
		int n = location[i][j].beepers;
		if (n != 0) {
		    g.setColor( Color.orange );
		    g.fillOval( x-w/8,y-h/8, w/4,h/4 );
		    g.setColor( Color.black );
		    g.drawString( ""+n, x-4,y+4 );
		}
	    }
	}
    }

    private Intersection[][] newGrid(int w, int h) {
	Intersection m[][];
	m = new Intersection[w+1][];
	for (int i=0; i<=w; i++) {
	    m[i] = new Intersection[h+1];
	    for (int j=0; j<=h; j++) {
		m[i][j] = new Intersection();
	    }
	}
	return m;
    }

    public Dimension getPreferredSize() { return preferredSize; }

    // the world size changed; make a new grid
    public void setWorldSize( Dimension d ) {
	int w, h;
	Intersection[][] m;
		
	w = (int)d.getWidth();
	h = (int)d.getHeight();
	if (w == width && h == height) { return; }
		
	m = newGrid(w,h);
		
	for (int i=1; i<=w; i++) {
	    for (int j=1; j<=h; j++) {
		m[i][j] =
		    (i<=width && j<=height)
		    ? location[i][j]
		    : new Intersection();
	    }
	}
		
	if (kx > w) { kx = w; }
	if (ky > h) { ky = h; }
		
	width = w;
	height = h;
	location = m;
	changed = true;
		
	repaint();
    }

    // the number of beepers changed; note it
    public void setRobotBeepers( int b ) {
	if (kb == b) { return; }
		
	kb = b;
	changed = true;
    }

    public boolean unsavedChanges() {
	return changed;
    }

    public void save( PrintStream ps ) {
	try {
	    ps.println("World "+width+" "+height );
	    ps.println("Robot "+kx+" "+ky+" "+kd+" "+kb );
	    Intersection x;
	    for (int i=0; i<=width; i++) {
		for (int j=0; j<=height; j++) {
		    x = location[i][j];
		    if (x.wallNorth) {
			ps.println("Wall "+i+" "+j+" "
				   +1);
		    }
		    if (x.wallEast) {
			ps.println("Wall "+i+" "+j+" "
				   +4);
		    }
		    if (x.beepers > 0) {
			ps.println( "Beepers "+i+" "+j
				    +" "+x.beepers );
		    }
		}
	    }
	    changed = false;
	} catch (Exception ex) {
	    System.out.println(ex.toString());
	}
    }

    public void load( BufferedReader br ) {
	int w,h, x,y,d,n, rx,ry,rd,rb;
	Intersection[][] m;
	String line;
		
	w = h = 10;
	rx = ry = rd = rb = 1;
	m = null;
	line = null;
		
	try {
	    while (br.ready()) {
		line = br.readLine();
		StringTokenizer t = new StringTokenizer(line);
		if (t.hasMoreTokens()) {
		    String w1 = t.nextToken();
		    if (w1.equals("World")) {
			w = parseInt( t.nextToken() );
			h = parseInt( t.nextToken() );
			m = newGrid(w,h);
		    } else if (w1.equals("Robot")) {
			rx = parseInt( t.nextToken() );
			ry = parseInt( t.nextToken() );
			rd = parseInt( t.nextToken() );
			rb = parseInt( t.nextToken() );
		    } else if (w1.equals("Wall")) {
			x = parseInt( t.nextToken() );
			y = parseInt( t.nextToken() );
			d = parseInt( t.nextToken() );
			if (d==1) {
			    m[x][y].wallNorth
				= true;
			} else if (d==4) {
			    m[x][y].wallEast
				= true;
			}
		    } else if (w1.equals("Beepers")) {
			x = parseInt( t.nextToken() );
			y = parseInt( t.nextToken() );
			n = parseInt( t.nextToken() );
			m[x][y].beepers = n;
		    }
		}
	    }
			
	    // got through all that; plug in the new world
	    location = m;
	    width = w;
	    height = h;
	    kx = rx; ky = ry; kd = rd; kb = rb;
	    gdp.setWorldSize(width,height);
	    gdp.setBeepersInBag(kb);
			
	    repaint();
	} catch (Exception ex) {
	    // System.out.println(ex.toString());
	    System.err.println( "Error loading file"
				+ ((line != null)? ", in line: "+line : "") );
	    // ex.printStackTrace();
	}
	changed = false;
    }


    // MouseListener
    public void mouseClicked(MouseEvent e) {
	// System.out.println("clicked");
	int x, x0, x1, y, y0, y1; // position, min, max, on each axis
	x0 = 0; x1 = getWidth();
	y0 = 0; y1 = getHeight();
	int w = x1 / width;  // width of one column, in pixels
	int h = y1 / height; // height of one row, in pixels
	x = e.getX();
	y = e.getY();
	// Find the nearest roads; round so that clicks east of X are
	// associated with X, and clicks North of Y are associated
	// with Y.  The road numbers we get here are in graphic space,
	// numbered 0 through width-1 (or height-1).
	int nearx = (x-w/4)/w; // shift West before rounding down
	int neary = (y+h/4)/h; // shift South before rounding down
	// find the difference from the chosen road locations
	double diffx = (x - (nearx+0.5) * w ) / w;
	double diffy = (y - (neary+0.5) * h ) / h;
	// Karel numbers roads from South to North
	int i = nearx + 1;
	int j = height - neary;
	// System.out.println("  x="+x+"  y="+y
	// 	+"  nx="+nearx+"  ny="+neary
	// 	+"  dx="+diffx+"  dy="+diffy
	// 	+"  i="+i+"  j="+j );
	// decide if they clicked on an intersection or a wall position
	if ( diffx >= -0.25 && diffx <= 0.25
	     &&   diffy >= -0.25 && diffy <= 0.25 ) {
	    // clicked on the intersection
	    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
		if (kx != i || ky != j) {
		    // put karel here
		    kx = i; ky = j; kd = 1;
		} else {
		    // rotate karel
		    kd += 1; if (kd > 4) { kd = 1; }
		}
		changed = true;
	    }
	    if ((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
		location[i][j].beepers += 1;
		changed = true;
	    }
	    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
		if (location[i][j].beepers > 0) {
		    location[i][j].beepers -= 1;
		}
		changed = true;
	    }
	    repaint();
	}
	if ( diffx >= -0.25 && diffx <= 0.25
	     &&   diffy >= -0.75 && diffy <= -0.25 ) {
	    // clicked on the North wall position
	    location[i][j].wallNorth = ! location[i][j].wallNorth;
	    repaint();
	    changed = true;
	}
	if ( diffx >= 0.25 && diffx <= 0.75
	     &&   diffy >= -0.25 && diffy <= 0.25 ) {
	    // clicked on the East wall position
	    location[i][j].wallEast = ! location[i][j].wallEast;
	    repaint();
	    changed = true;
	}
		
    }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }


    private int parseInt( String n ) {
	try { return Integer.parseInt(n); }
	catch (NumberFormatException x) { return 0; }
    }
}

