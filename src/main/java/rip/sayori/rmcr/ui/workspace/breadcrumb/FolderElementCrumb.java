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

package rip.sayori.rmcr.ui.workspace.breadcrumb;

import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.workspace.elements.FolderElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FolderElementCrumb extends JLabel {

	private final FolderElement element;

	public FolderElementCrumb(FolderElement element) {
		super(element.getName());

		this.element = element;

		setIcon(UIRES.get("laf.directory.gif"));

		setOpaque(false);
		setBackground((Color) UIManager.get("MCreatorLAF.DARK_ACCENT"));

		addMouseListener(new MouseAdapter() {
			@Override public void mouseExited(MouseEvent e) {
				setOpaque(false);
				setBackground((Color) UIManager.get("MCreatorLAF.DARK_ACCENT"));
			}
		});
	}

	public FolderElement getFolder() {
		return element;
	}
}
