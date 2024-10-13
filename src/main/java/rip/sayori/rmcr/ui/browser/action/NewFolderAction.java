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

package rip.sayori.rmcr.ui.browser.action;

import rip.sayori.rmcr.ui.action.ActionRegistry;
import rip.sayori.rmcr.ui.action.BasicAction;
import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.validation.Validator;
import rip.sayori.rmcr.ui.validation.component.VTextField;
import rip.sayori.rmcr.ui.validation.optionpane.OptionPaneValidatior;
import rip.sayori.rmcr.ui.validation.optionpane.VOptionPane;
import rip.sayori.rmcr.ui.validation.validators.RegistryNameValidator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class NewFolderAction extends BasicAction {

	public NewFolderAction(ActionRegistry actionRegistry) {
		super(actionRegistry, "Folder", actionEvent -> {
			String foldername = VOptionPane.showInputDialog(actionRegistry.getMCreator(), "Enter the folder name:",
					"Package name", null, new OptionPaneValidatior() {
						@Override public Validator.ValidationResult validate(JComponent component) {
							return new RegistryNameValidator((VTextField) component, "Folder").validate();
						}
					});
			if (foldername != null) {
				if (actionRegistry.getMCreator().getProjectBrowser().tree.getLastSelectedPathComponent() != null) {
					Object selection = ((DefaultMutableTreeNode) actionRegistry.getMCreator()
							.getProjectBrowser().tree.getLastSelectedPathComponent()).getUserObject();
					if (selection instanceof File) {
						File filesel = ((File) selection);
						if (filesel.isFile())
							filesel = filesel.getParentFile();

						if (filesel.isDirectory()) {
							new File(filesel, foldername).mkdirs();
							actionRegistry.getMCreator().getProjectBrowser().reloadTree();
						}
					}
				}
			}
		});
		setIcon(UIRES.get("16px.directory.gif"));
	}

}
