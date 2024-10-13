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

package rip.sayori.rmcr.ui.blockly;

import rip.sayori.rmcr.blockly.java.ProcedureTemplateIO;
import rip.sayori.rmcr.io.ResourcePointer;
import rip.sayori.rmcr.ui.component.JScrollablePopupMenu;
import rip.sayori.rmcr.ui.component.util.ComponentUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class AITaskTemplateDropdown extends JScrollablePopupMenu {

	private static final Logger LOG = LogManager.getLogger(AITaskTemplateDropdown.class);

	public AITaskTemplateDropdown(BlocklyPanel blocklyPanel, List<ResourcePointer> templatesSorted) {
		setMaximumVisibleRows(20);
		for (ResourcePointer template : templatesSorted) {
			try {
				JMenuItem modTypeButton = new JMenuItem(template.toString());

				if (template.identifier instanceof String)
					modTypeButton.addActionListener(actionEvent -> blocklyPanel.addBlocksFromXML(
							ProcedureTemplateIO.importBlocklyXML("/" + template.identifier)));
				else
					modTypeButton.addActionListener(actionEvent -> blocklyPanel.addBlocksFromXML(
							ProcedureTemplateIO.importBlocklyXML((File) template.identifier)));
				modTypeButton.setOpaque(true);
				ComponentUtils.deriveFont(modTypeButton, 12);
				add(modTypeButton);
			} catch (Exception e) {
				LOG.info("Failed to load template: " + template);
			}
		}
	}

}
