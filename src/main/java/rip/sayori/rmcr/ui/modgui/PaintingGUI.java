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

import rip.sayori.rmcr.element.types.Painting;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.MCreatorApplication;
import rip.sayori.rmcr.ui.component.SearchableComboBox;
import rip.sayori.rmcr.ui.component.util.ComboBoxUtil;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.dialogs.TextureImportDialogs;
import rip.sayori.rmcr.ui.help.HelpUtils;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.laf.renderer.WTextureComboBoxRenderer;
import rip.sayori.rmcr.ui.validation.AggregatedValidationResult;
import rip.sayori.rmcr.ui.validation.ValidationGroup;
import rip.sayori.rmcr.ui.validation.Validator;
import rip.sayori.rmcr.ui.validation.component.VComboBox;
import rip.sayori.rmcr.util.ListUtils;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.stream.Collectors;

public class PaintingGUI extends ModElementGUI<Painting> {

	private final JSpinner width = new JSpinner(new SpinnerNumberModel(16, 16, 64000, 16));
	private final JSpinner height = new JSpinner(new SpinnerNumberModel(16, 16, 64000, 16));

	private final VComboBox<String> texture = new SearchableComboBox<>();

	private final ValidationGroup page1group = new ValidationGroup();

	public PaintingGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
		super(mcreator, modElement, editingMode);
		this.initGUI();
		super.finalizeGUI();
	}

	@Override protected void initGUI() {
		texture.setRenderer(new WTextureComboBoxRenderer.OtherTextures(mcreator.getWorkspace()));
		texture.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");

		JPanel pane3 = new JPanel(new BorderLayout());
		pane3.setOpaque(false);

		JPanel selp = new JPanel(new GridLayout(3, 2, 50, 2));
		selp.setOpaque(false);

		JButton importicontexture = new JButton(UIRES.get("18px.add"));
		importicontexture.setToolTipText(L10N.t("elementgui.painting.import_painting"));
		importicontexture.setOpaque(false);
		importicontexture.addActionListener(e -> {
			TextureImportDialogs.importOtherTextures(mcreator);
			texture.removeAllItems();
			texture.addItem("");
			mcreator.getFolderManager().getOtherTexturesList().forEach(el -> texture.addItem(el.getName()));
		});

		selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("painting/texture"),
				L10N.label("elementgui.painting.painting_texture")));
		selp.add(PanelUtils.centerAndEastElement(texture, importicontexture));

		selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("painting/width"),
				L10N.label("elementgui.painting.painting_width")));
		selp.add(width);

		selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("painting/height"),
				L10N.label("elementgui.painting.painting_height")));
		selp.add(height);

		pane3.add("Center", PanelUtils.totalCenterInPanel(selp));

		texture.setValidator(() -> {
			if (texture.getSelectedItem() == null || texture.getSelectedItem().equals(""))
				return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
						L10N.t("elementgui.painting.error_painting_needs_texture"));
			return Validator.ValidationResult.PASSED;
		});
		page1group.addValidationElement(texture);

		addPage(L10N.t("elementgui.common.page_properties"), pane3);
	}

	@Override public void reloadDataLists() {
		super.reloadDataLists();

		ComboBoxUtil.updateComboBoxContents(texture, ListUtils.merge(Collections.singleton(""),
				mcreator.getFolderManager().getOtherTexturesList().stream().map(File::getName)
						.collect(Collectors.toList())), "");
	}

	@Override protected AggregatedValidationResult validatePage(int page) {
		if (page == 0)
			return new AggregatedValidationResult(page1group);
		return new AggregatedValidationResult.PASS();
	}

	@Override public void openInEditingMode(Painting painting) {
		width.setValue(painting.width);
		height.setValue(painting.height);
		texture.setSelectedItem(painting.texture);
	}

	@Override public Painting getElementFromGUI() {
		Painting painting = new Painting(modElement);
		painting.width = (int) width.getValue();
		painting.height = (int) height.getValue();
		painting.texture = texture.getSelectedItem();
		return painting;
	}

	@Override public @Nullable URI getContextURL() throws URISyntaxException {
		return new URI(MCreatorApplication.SERVER_DOMAIN + "/wiki/how-make-painting");
	}

}
