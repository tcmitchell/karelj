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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import karel.*;

public class WorldPanel
    extends JPanel
    implements ComponentListener, PropertyChangeListener
{

    int scale_x;

    int scale_y;

    int translate_x;

    int translate_y;

    int cell_width;

    int cell_height;

    int half_cell_width;

    int half_cell_height;

    World fWorld;

    Robot fRobot;

    Color beeperColor = new Color(0x66FF33);

    public WorldPanel() {
	addComponentListener(this);
	fWorld = null;
	fRobot = null;
    }

    public void setRobot(Robot r) {
	if (fRobot != null) fRobot.removePropertyChangeListener(this);

	fRobot = r;
	if (fWorld != null) fRobot.setWorld(fWorld);
	fRobot.addPropertyChangeListener(this);
    }

    public void setWorld(World w) {
	fWorld = w;
	computeScale();
	if (fRobot != null) fRobot.setWorld(fWorld);
    }

    protected int getWorldAvenues() {
	return fWorld.getAvenues();
    }

    protected int getWorldStreets() {
	return fWorld.getStreets();
    }

    protected void computeScale() {

	if (fWorld == null) return;

	Dimension size = getSize();

	scale_x = size.width / (getWorldAvenues() + 1);
	scale_y = size.height / (getWorldStreets() + 1);
	scale_x = scale_y = Math.min(scale_x, scale_y);

	cell_width = scale_x;
	cell_height = scale_y;

	half_cell_width = cell_width/2;
	half_cell_height = cell_height/2;
    }

    protected Point worldToScreen(int avenue, int street) {
	Point pt = new Point();
	pt.x = (scale_x * avenue) + translate_x;
	pt.y = (scale_y * street) + translate_y;
	pt.y = this.getSize().height - pt.y; // convert to y-up
	return pt;
    }

    protected void drawRobot(Graphics g) {
	int h = half_cell_height;
	int w = half_cell_width;
	int face = h/3;

	if (fRobot == null) return;
	g.setColor(Color.blue);
	Point pt = worldToScreen(fRobot.getAvenue(), fRobot.getStreet());
	pt.x -= h/2;
	pt.y -= w/2;
	g.fillRect(pt.x, pt.y, w, h);

	g.setColor(Color.red);
	switch (fRobot.getDirection()) {
	case Robot.EAST:
	    g.fillRect(pt.x+w-face, pt.y, face, h);
	    break;
	case Robot.WEST:
	    g.fillRect(pt.x, pt.y, face, h);
	    break;
	case Robot.SOUTH:
	    g.fillRect(pt.x, pt.y+h-face, w, face);
	    break;
	case Robot.NORTH:
	    g.fillRect(pt.x, pt.y, w, face);
	    break;
	}
    }

    protected void drawBeepers(Graphics g) {
	if (fWorld == null) return;

	int beeper_width = half_cell_width/2;
	int beeper_height = half_cell_height/2;
	int x_offset = beeper_width/2;
	int y_offset = beeper_height/2;

	for (int a=1; a<=getWorldAvenues(); a++) {
	    for (int s=1; s<=getWorldStreets(); s++) {
		if (fWorld.checkBeeper(a, s)) {
		    Point pt = worldToScreen(a, s);
		    g.setColor(beeperColor);
		    g.fillOval(pt.x - x_offset, pt.y - y_offset,
			       beeper_width, beeper_height);
 		    g.setColor(Color.black);
 		    g.drawOval(pt.x - x_offset, pt.y - y_offset,
 			       beeper_width, beeper_height);
 		    g.drawOval(pt.x - x_offset, pt.y - y_offset,
 			       beeper_width+1, beeper_height+1);
		}
	    }
	}
    }

    protected void drawWideVerticalLine(Graphics g, int x1, int y1,
					int x2, int y2) {
	g.drawLine(x1, y1, x2, y2);
	g.drawLine(x1+1, y1, x2+1, y2);
	g.drawLine(x1-1, y1, x2-1, y2);
    }

    protected void drawWideHorizontalLine(Graphics g, int x1, int y1,
					  int x2, int y2) {
	g.drawLine(x1, y1, x2, y2);
	g.drawLine(x1, y1+1, x2, y2+1);
	g.drawLine(x1, y1-1, x2, y2-1);
    }

    protected void drawWalls(Graphics g) {
	Point pt1, pt2;
	g.setColor(Color.black);

	// Draw the west and south walls automatically

	// Draw the west wall
	pt1 = worldToScreen(1, 1);
	pt2 = worldToScreen(1, getWorldStreets());
	drawWideVerticalLine(g,
			     pt1.x - half_cell_width,
			     pt2.y - half_cell_height,
			     pt1.x - half_cell_width,
			     pt1.y + half_cell_height);

	// Draw the south wall
	pt2 = worldToScreen(getWorldAvenues(), 1);
	drawWideHorizontalLine(g,
			       pt1.x - half_cell_width,
			       pt1.y + half_cell_height,
			       pt2.x + half_cell_width,
			       pt1.y + half_cell_height);

	for (int s=1; s<=getWorldStreets(); s++) {
	    for (int a=1; a<=getWorldAvenues(); a++) {
		if (fWorld.checkNSWall(a, s)) {
		    pt1 = worldToScreen(a, s);
		    drawWideVerticalLine(g,
					 pt1.x + half_cell_width,
					 pt1.y - half_cell_height,
					 pt1.x + half_cell_width,
					 pt1.y + half_cell_height);
		}
		if (fWorld.checkEWWall(a, s)) {
		    pt1 = worldToScreen(a, s);
		    drawWideHorizontalLine(g,
					   pt1.x - half_cell_width,
					   pt1.y - half_cell_height,
					   pt1.x + half_cell_width,
					   pt1.y - half_cell_height);
		}
	    }
	}
    }

    protected void drawGrid(Graphics g) {
	Point pt1, pt2;

	// Draw the streets
	g.setColor(Color.gray);
	for (int s=1; s<=getWorldStreets(); s++) {
	    pt1 = worldToScreen(1, s);
	    pt2 = worldToScreen(getWorldAvenues(), s);
	    g.drawLine(pt1.x - half_cell_width, pt1.y,
		       pt2.x + half_cell_width, pt2.y);
	}

	// Draw the avenues
  	for (int a=1; a<=getWorldAvenues(); a++) {
	    pt1 = worldToScreen(a, 1);
	    pt2 = worldToScreen(a, getWorldStreets());
	    g.drawLine(pt1.x, pt1.y + half_cell_height,
		       pt2.x, pt2.y - half_cell_height);
  	}
    }

    protected void drawBoundingBox(Graphics g) {
	// Draw the bounding box
	g.setColor(Color.black);
	Point pt1 = worldToScreen(1, 1); // Origin
	int originX = pt1.x - half_cell_width;
	int originY = pt1.y + half_cell_height;
	Point pt2 = worldToScreen(getWorldAvenues(), getWorldStreets());
	int maxX = pt2.x + half_cell_width;
	int maxY = pt2.y - half_cell_height;

	// draws a rect from upperLeft corner to lowerRight corner
	g.drawRect(originX, maxY, maxX - originX, originY - maxY);
	
    }

    public void paint(Graphics g) {

	// First, fill the panel with white
	g.setColor(Color.white);
	Dimension size = getSize();
	g.fillRect(0, 0, size.width, size.height);

	if (fWorld != null) {
//  	    drawBoundingBox(g);
	    drawGrid(g);
	    drawWalls(g);
	    drawBeepers(g);
	    drawRobot(g);
	}
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
	computeScale();
    }

    public void componentShown(ComponentEvent e) {
    }

    public void propertyChange(PropertyChangeEvent evt) {
	if (Robot.LocationProperty.equals(evt.getPropertyName())) {
	    repaint();
	}
	if (Robot.DirectionProperty.equals(evt.getPropertyName())) {
	    repaint();
	}
    }
}
