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

package rip.sayori.rmcr.ui.laf.renderer;

import org.fife.rsta.ac.java.JavaCellRenderer;
import org.fife.ui.autocomplete.Completion;
import rip.sayori.rmcr.ui.init.BlockItemIcons;
import rip.sayori.rmcr.ui.laf.AbstractMCreatorTheme;
import rip.sayori.rmcr.util.image.ImageUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class MinecraftCompletionCellRenderer extends JavaCellRenderer {

	private final JTextComponent tc;

	public MinecraftCompletionCellRenderer(JTextComponent tc) {
		this.tc = tc;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected,
			boolean hasFocus) {

		super.getListCellRendererComponent(list, value, index, selected, hasFocus);

		if (tc != null) {
			Completion c = (Completion) value;
			if (c.getProvider().getAlreadyEnteredText(tc).contains("Blocks.")) {
				ImageIcon imageIcon = BlockItemIcons.getIconForItem(c.getInputText());
				if (imageIcon != null && imageIcon.getImage() != null)
					setIcon(new ImageIcon(ImageUtils.resize(imageIcon.getImage(), 16)));
			}
		}

		setFont(AbstractMCreatorTheme.console_font.deriveFont(12.0f));

		if (selected) {
			setBackground(new Color(0, 58, 80));
		} else {
			setBackground((Color) UIManager.get("MCreatorLAF.DARK_ACCENT"));
		}

		setOpaque(false);

		return this;
	}

}