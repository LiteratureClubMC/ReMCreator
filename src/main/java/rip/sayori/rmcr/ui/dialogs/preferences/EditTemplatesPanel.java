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

package rip.sayori.rmcr.ui.dialogs.preferences;

import rip.sayori.rmcr.io.FileIO;
import rip.sayori.rmcr.io.UserFolderManager;
import rip.sayori.rmcr.ui.component.JEmptyBox;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.dialogs.FileDialogs;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.init.UIRES;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Locale;

class EditTemplatesPanel {

	EditTemplatesPanel(PreferencesDialog preferencesDialog, String name, String templatesFolder, String templateExt) {
		preferencesDialog.model.addElement(name);

		JPanel sectionPanel = new JPanel(new BorderLayout(15, 15));

		sectionPanel.add("North",
				L10N.label("dialog.preferences.change_language", name.toLowerCase(Locale.ENGLISH), templateExt));
		sectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

		JToolBar opts = new JToolBar();
		opts.setFloatable(false);

		JButton add = L10N.button("dialog.preferences.add_language", name.toLowerCase(Locale.ENGLISH));
		add.setIcon(UIRES.get("16px.add.gif"));
		opts.add(add);

		opts.add(new JEmptyBox(5, 5));

		JButton remove = L10N.button("dialog.preferences.remove_selected_language");
		remove.setIcon(UIRES.get("16px.delete.gif"));
		opts.add(remove);

		DefaultListModel<String> tmodel = new DefaultListModel<>();
		JList<String> templates = new JList<>(tmodel);

		remove.addActionListener(e -> templates.getSelectedValuesList().forEach(el -> {
			new File(UserFolderManager.getFileFromUserFolder(templatesFolder), el).delete();
			tmodel.removeElement(el);
		}));

		add.addActionListener(e -> {
			File[] files = FileDialogs.getMultiOpenDialog(preferencesDialog, new String[] { templateExt });
			if (files != null) {
				Arrays.stream(files).forEach(f -> {
					FileIO.copyFile(f, new File(UserFolderManager.getFileFromUserFolder(templatesFolder), f.getName()));
					tmodel.addElement(f.getName());
				});
			}
		});

		File[] files = UserFolderManager.getFileFromUserFolder(templatesFolder).listFiles();
		if (files != null) {
			Arrays.stream(files).forEach(f -> {
				if (f.getName().toLowerCase(Locale.ENGLISH).endsWith(templateExt.toLowerCase(Locale.ENGLISH)))
					tmodel.addElement(f.getName());
			});
		}

		sectionPanel.add("Center", PanelUtils.northAndCenterElement(opts, new JScrollPane(templates), 5, 5));

		preferencesDialog.preferences.add(sectionPanel, name);
	}

}
