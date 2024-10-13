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

package rip.sayori.rmcr.ui.dialogs;

import rip.sayori.rmcr.element.ModElementType;
import rip.sayori.rmcr.element.ModElementTypeRegistry;
import rip.sayori.rmcr.java.JavaConventions;
import rip.sayori.rmcr.minecraft.RegistryNameFixer;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.init.TiledImageCache;
import rip.sayori.rmcr.ui.modgui.ModElementGUI;
import rip.sayori.rmcr.ui.validation.Validator;
import rip.sayori.rmcr.ui.validation.component.VTextField;
import rip.sayori.rmcr.ui.validation.optionpane.OptionPaneValidatior;
import rip.sayori.rmcr.ui.validation.optionpane.VOptionPane;
import rip.sayori.rmcr.ui.validation.validators.ModElementNameValidator;
import rip.sayori.rmcr.workspace.elements.ModElement;

import javax.swing.*;
import java.awt.*;

public class NewModElementDialog {

	public static void showNameDialog(MCreator mcreator, ModElementType type) {
		JLabel regName = L10N.label("dialog.new_modelement.registry_name_default");
		regName.setForeground((Color) UIManager.get("MCreatorLAF.GRAY_COLOR"));
		regName.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		String modName = VOptionPane.showInputDialog(mcreator,
				L10N.t("dialog.new_modelement.desc", type.getReadableName()),
				L10N.t("dialog.new_modelement.title_window", type.getReadableName()),
				TiledImageCache.getModTypeIcon(type), new OptionPaneValidatior() {
					@Override public Validator.ValidationResult validate(JComponent component) {
						regName.setText("Registry name: " + RegistryNameFixer.fromCamelCase(
								((VTextField) component).getText()));
						return new ModElementNameValidator(mcreator.getWorkspace(), (VTextField) component).validate();
					}
				}, L10N.t("dialog.new_modelement.create_new", type.getReadableName()),
				L10N.t("dialog.new_modelement.cancel"), null, regName);

		if (modName != null && !modName.equals("")) {
			modName = JavaConventions.convertToValidClassName(modName);

			ModElement element = new ModElement(mcreator.getWorkspace(), modName, type);

			// if we are not in the root folder, specify the folder of the mod element
			if (!mcreator.mv.currentFolder.equals(mcreator.getWorkspace().getFoldersRoot()))
				element.setParentFolder(mcreator.mv.currentFolder);

			ModElementGUI<?> newGUI = ModElementTypeRegistry.REGISTRY.get(type).getModElement(mcreator, element, false);
			if (newGUI != null) {
				newGUI.showView();
			}
		}
	}

}
