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

package rip.sayori.rmcr.ui.minecraft;

import rip.sayori.rmcr.element.parts.Enchantment;
import rip.sayori.rmcr.minecraft.DataListEntry;
import rip.sayori.rmcr.minecraft.ElementUtil;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.component.JItemListField;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.init.L10N;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentListField extends JItemListField<Enchantment> {

	private final MCreator mcreator;

	public EnchantmentListField(MCreator mcreator) {
		this.mcreator = mcreator;
	}

	@Override protected List<Enchantment> getElementsToAdd() {
		JList<String> vlist = new JList<>(
				ElementUtil.loadAllEnchantments(mcreator.getWorkspace()).stream().map(DataListEntry::getName)
						.toArray(String[]::new));
		int option = JOptionPane.showOptionDialog(mcreator,
				PanelUtils.northAndCenterElement(L10N.label("dialog.list_field.enchantment_message"),
						new JScrollPane(vlist)), L10N.t("dialog.list_field.enchantment_title"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

		if (option == JOptionPane.OK_OPTION && vlist.getSelectedValue() != null) {
			return vlist.getSelectedValuesList().stream().map(e -> new Enchantment(mcreator.getWorkspace(), e))
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}
