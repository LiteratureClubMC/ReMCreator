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

package rip.sayori.rmcr.ui.dialogs.tools;

import rip.sayori.rmcr.element.ModElementType;
import rip.sayori.rmcr.element.parts.MItemBlock;
import rip.sayori.rmcr.generator.GeneratorConfiguration;
import rip.sayori.rmcr.generator.GeneratorStats;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.action.ActionRegistry;
import rip.sayori.rmcr.ui.action.BasicAction;
import rip.sayori.rmcr.ui.component.JColor;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.dialogs.MCreatorDialog;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.validation.Validator;
import rip.sayori.rmcr.ui.validation.component.VTextField;
import rip.sayori.rmcr.ui.validation.validators.ModElementNameValidator;
import rip.sayori.rmcr.workspace.Workspace;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MaterialPackMakerTool {

	private static void open(MCreator mcreator) {
		MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.tools.material_pack_title"), true);
		dialog.setLayout(new BorderLayout(10, 10));

		dialog.setIconImage(UIRES.get("16px.materialpack").getImage());

		dialog.add("North", PanelUtils.centerInPanel(L10N.label("dialog.tools.material_pack_info")));

		JPanel props = new JPanel(new GridLayout(4, 2, 5, 5));

		VTextField name = new VTextField(25);
		JColor color = new JColor(mcreator);
		JSpinner power = new JSpinner(new SpinnerNumberModel(1, 0.1, 10, 0.1));
		JComboBox<String> type = new JComboBox<>(new String[] { "Gem based", "Dust based", "Ingot based" });

		color.setColor((Color) UIManager.get("MCreatorLAF.MAIN_TINT"));
		name.enableRealtimeValidation();

		props.add(L10N.label("dialog.tools.material_pack_name"));
		props.add(name);

		props.add(L10N.label("dialog.tools.material_pack_type"));
		props.add(type);

		props.add(L10N.label("dialog.tools.material_pack_color_accent"));
		props.add(color);

		props.add(L10N.label("dialog.tools.material_pack_power_factor"));
		props.add(power);

		name.setValidator(new ModElementNameValidator(mcreator.getWorkspace(), name));

		dialog.add("Center", PanelUtils.centerInPanel(props));
		JButton ok = L10N.button("dialog.tools.material_pack_create");
		JButton canecel = L10N.button(UIManager.getString("OptionPane.cancelButtonText"));
		canecel.addActionListener(e -> dialog.setVisible(false));
		dialog.add("South", PanelUtils.join(ok, canecel));

		ok.addActionListener(e -> {
			if (name.getValidationStatus().getValidationResultType() != Validator.ValidationResultType.ERROR) {
				dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				addMaterialPackToWorkspace(mcreator, mcreator.getWorkspace(), name.getText(),
						(String) Objects.requireNonNull(type.getSelectedItem()), color.getColor(),
						(Double) power.getValue());
				mcreator.mv.updateMods();
				dialog.setCursor(Cursor.getDefaultCursor());
				dialog.setVisible(false);
			}
		});

		dialog.setSize(600, 280);
		dialog.setLocationRelativeTo(mcreator);
		dialog.setVisible(true);
	}

	private static void addMaterialPackToWorkspace(MCreator mcreator, Workspace workspace, String name, String type,
			Color color, double factor) {
		MItemBlock gem = OrePackMakerTool.addOrePackToWorkspace(mcreator, workspace, name, type, color, factor);
		ToolPackMakerTool.addToolPackToWorkspace(mcreator, workspace, name, gem, color, factor);
		ArmorPackMakerTool.addArmorPackToWorkspace(mcreator, workspace, name, gem, color, factor);
	}

	public static BasicAction getAction(ActionRegistry actionRegistry) {
		return new BasicAction(actionRegistry, L10N.t("action.pack_tools.material"),
				e -> open(actionRegistry.getMCreator())) {
			@Override public boolean isEnabled() {
				GeneratorConfiguration gc = actionRegistry.getMCreator().getGeneratorConfiguration();
				return gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.RECIPE)
						!= GeneratorStats.CoverageStatus.NONE
						&& gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.ITEM)
						!= GeneratorStats.CoverageStatus.NONE
						&& gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.BLOCK)
						!= GeneratorStats.CoverageStatus.NONE
						&& gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.TOOL)
						!= GeneratorStats.CoverageStatus.NONE
						&& gc.getGeneratorStats().getModElementTypeCoverageInfo().get(ModElementType.ARMOR)
						!= GeneratorStats.CoverageStatus.NONE;
			}
		}.setIcon(UIRES.get("16px.materialpack"));
	}

}
