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
import java.awt.*;

public class JModElementProgressPanel extends JPanel {

	private final CardLayout de = new CardLayout();
	private int slide = 0;
	private int pages = 0;

	public JModElementProgressPanel(Component... p1) {
		setOpaque(false);
		setLayout(de);

		for (Component pane : p1) {
			add("" + pages, pane);
			pages++;
		}
	}

	public void next() {
		if (slide + 1 < pages) {
			slide++;
			de.show(this, "" + slide);
		}
	}

	public void back() {
		if (slide > 0) {
			slide--;
			de.show(this, "" + slide);
		}
	}

	public int getPage() {
		return slide;
	}

	public void setPage(int page) {
		slide = page;
		de.show(this, "" + slide);
	}

	public int getPageCount() {
		return pages;
	}
}
