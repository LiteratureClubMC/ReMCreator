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

package rip.sayori.rmcr.ui.modgui;

import rip.sayori.rmcr.blockly.data.Dependency;
import rip.sayori.rmcr.element.types.Overlay;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.component.util.ComboBoxUtil;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.laf.renderer.WTextureComboBoxRenderer;
import rip.sayori.rmcr.ui.minecraft.ProcedureSelector;
import rip.sayori.rmcr.ui.validation.AggregatedValidationResult;
import rip.sayori.rmcr.ui.wysiwyg.WYSIWYGEditor;
import rip.sayori.rmcr.util.ListUtils;
import rip.sayori.rmcr.workspace.elements.ModElement;
import rip.sayori.rmcr.workspace.elements.VariableElementType;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

public class OverlayGUI extends ModElementGUI<Overlay> {

	private WYSIWYGEditor editor;

	private ProcedureSelector displayCondition;

	public OverlayGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
		super(mcreator, modElement, editingMode);
		this.initGUI();
		super.finalizeGUI(false);
	}

	@Override protected void initGUI() {
		JPanel pane5 = new JPanel(new BorderLayout(0, 0));

		displayCondition = new ProcedureSelector(this.withEntry("overlay/display_condition"), mcreator,
				L10N.t("elementgui.overlay.event_display_ingame"), ProcedureSelector.Side.CLIENT, true,
				VariableElementType.LOGIC,
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity"));

		editor = new WYSIWYGEditor(mcreator, false);
		editor.button.setVisible(false);
		editor.slot1.setVisible(false);
		editor.slot2.setVisible(false);
		editor.text.setVisible(false);

		editor.ovst.add(displayCondition);

		editor.overlayBaseTexture.setRenderer(new WTextureComboBoxRenderer.OtherTextures(mcreator.getWorkspace()));

		editor.setPreferredSize(new Dimension(5, 550));

		pane5.setOpaque(false);
		pane5.add("Center", PanelUtils.maxMargin(editor, 5, true, true, true, true));

		addPage(pane5);
	}

	@Override protected AggregatedValidationResult validatePage(int page) {
		return new AggregatedValidationResult.PASS();
	}

	@Override public void reloadDataLists() {
		super.reloadDataLists();

		ComboBoxUtil.updateComboBoxContents(editor.overlayBaseTexture, ListUtils.merge(Collections.singleton(""),
				mcreator.getFolderManager().getOtherTexturesList().stream().map(File::getName)
						.collect(Collectors.toList())), "");

		displayCondition.refreshListKeepSelected();
	}

	@Override public void openInEditingMode(Overlay overlay) {
		editor.priority.setSelectedItem(overlay.priority);
		editor.setComponentList(overlay.components);
		editor.overlayBaseTexture.setSelectedItem(overlay.baseTexture);
		displayCondition.setSelectedProcedure(overlay.displayCondition);
	}

	@Override public Overlay getElementFromGUI() {
		Overlay overlay = new Overlay(modElement);
		overlay.priority = (String) editor.priority.getSelectedItem();
		overlay.components = editor.getComponentList();
		overlay.baseTexture = editor.overlayBaseTexture.getSelectedItem();
		overlay.displayCondition = displayCondition.getSelectedProcedure();
		return overlay;
	}

}