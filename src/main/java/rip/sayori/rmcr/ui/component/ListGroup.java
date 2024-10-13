/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2012-2020, Pylo
 * Copyright (C) 2020-2021, Pylo, opensource contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rip.sayori.rmcr.ui.component;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ListGroup<T> {

	private final ArrayList<JList<T>> lists = new ArrayList<>();

	private boolean multiSelect = false;

	public void addList(JList<T> list) {
		lists.add(list);
		list.addListSelectionListener(listSelectionEvent -> {
			if (list.getSelectedIndex() != -1 && !multiSelect)
				lists.stream().filter(e -> !e.equals(list)).forEach(JList::clearSelection);
		});
		list.addKeyListener(new KeyAdapter() {
			@Override public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				multiSelect = (e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK;
			}

			@Override public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				multiSelect = (e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK;
			}
		});
	}

	public T getSelectedItem() {
		for (JList<T> list : lists) {
			if (list.getSelectedValue() != null)
				return list.getSelectedValue();
		}
		return null;
	}

	public List<T> getSelectedItemsList() {
		List<T> items = new ArrayList<>();
		lists.forEach(list -> items.addAll(list.getSelectedValuesList()));
		return items;
	}

	public void clearSelection() {
		lists.forEach(JList::clearSelection);
	}

}
