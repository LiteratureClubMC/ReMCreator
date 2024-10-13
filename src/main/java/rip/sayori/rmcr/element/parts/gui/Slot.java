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

package rip.sayori.rmcr.element.parts.gui;

import rip.sayori.rmcr.element.parts.Procedure;
import rip.sayori.rmcr.minecraft.MinecraftImageGenerator;
import rip.sayori.rmcr.ui.wysiwyg.WYSIWYGEditor;
import rip.sayori.rmcr.util.image.ImageUtils;
import rip.sayori.rmcr.workspace.Workspace;

import javax.swing.*;
import java.awt.Image;
import java.awt.*;

public abstract class Slot extends GUIComponent {

	private static transient final Image itemSlot = MinecraftImageGenerator.generateItemSlot();
	public Color color;
	public int id;
	public boolean disableStackInteraction;
	public boolean dropItemsWhenNotBound;
	public Procedure onSlotChanged;
	public Procedure onTakenFromSlot;
	public Procedure onStackTransfer;

	// for deserialization use only, to specify default values
	@SuppressWarnings("unused") Slot() {
		super();
		this.dropItemsWhenNotBound = true;
	}

	public Slot(int id, String name, int x, int y, Color color, boolean disableStackInteraction,
			boolean dropItemsWhenNotBound, Procedure onSlotChanged, Procedure onTakenFromSlot,
			Procedure onStackTransfer) {
		super(name, x, y);
		this.color = color;
		this.id = id;
		this.disableStackInteraction = disableStackInteraction;
		this.dropItemsWhenNotBound = dropItemsWhenNotBound;
		this.onSlotChanged = onSlotChanged;
		this.onTakenFromSlot = onTakenFromSlot;
		this.onStackTransfer = onStackTransfer;
	}

	@Override public int getWeight() {
		return 4;
	}

	@Override public final int getWidth(Workspace workspace) {
		return 18;
	}

	@Override public final int getHeight(Workspace workspace) {
		return 18;
	}

	@Override public void paintComponent(int cx, int cy, WYSIWYGEditor wysiwygEditor, Graphics2D g) {
		g.setFont(g.getFont().deriveFont(5f));
		g.setColor(Color.gray.brighter().brighter().brighter());

		if (wysiwygEditor.renderBgLayer.isSelected()) {
			if (this.color == null)
				g.drawImage(itemSlot, cx, cy, 18, 18, wysiwygEditor);
			else
				g.drawImage(ImageUtils.colorize(new ImageIcon(itemSlot), this.color, true).getImage(), cx, cy, 18, 18,
						wysiwygEditor);
		}

		g.drawString(String.format("%02d", this.id), cx + 10, cy + 6);
	}

}
