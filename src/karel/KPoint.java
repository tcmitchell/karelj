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

public class KPoint implements Cloneable, java.io.Serializable {
    public int avenue;
    public int street;

    public KPoint() {
        avenue = 0;
        street = 0;
    }

    public KPoint(int a, int s) {
        avenue = a;
        street = s;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KPoint) {
            KPoint that = (KPoint) obj;
            return (this.avenue == that.avenue && this.street == that.street);
        }
        return false;
    }

    @Override
    public String toString() {
        return "#<KPoint " + avenue + "," + street + ">";
    }

    public void setAvenue(int a) {
        avenue = a;
    }

    public void setStreet(int s) {
        street = s;
    }

    public int getAvenue() {
        return avenue;
    }

    public int getStreet() {
        return street;
    }
}
