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

package karel.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

public class RecordingPropertyChangeListener implements PropertyChangeListener {
    LinkedList events;

    public RecordingPropertyChangeListener() {
        events = new LinkedList();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        events.add(
                new PropertyChangeEvent(evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));
    }

    public void clear() {
        events.clear();
    }

    public PropertyChangeEvent getFirstEvent() {
        return (PropertyChangeEvent) events.getFirst();
    }
}
