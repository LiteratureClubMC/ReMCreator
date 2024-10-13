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

package rip.sayori.rmcr.ui.dialogs.imageeditor;

import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.component.JColor;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.dialogs.MCreatorDialog;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.views.editor.image.canvas.Canvas;
import rip.sayori.rmcr.ui.views.editor.image.layer.Layer;
import rip.sayori.rmcr.ui.views.editor.image.tool.component.ColorSelector;
import rip.sayori.rmcr.ui.views.editor.image.versioning.VersionManager;
import rip.sayori.rmcr.ui.views.editor.image.versioning.change.Modification;
import rip.sayori.rmcr.util.image.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RecolorDialog extends MCreatorDialog {

	public RecolorDialog(MCreator window, Canvas canvas, Layer layer, ColorSelector colorSelector,
			VersionManager versionManager) {
		super(window, L10N.t("dialog.imageeditor.recolor_layer"), true);

		JPanel settings = new JPanel(new GridBagLayout());
		JPanel controls = new JPanel(new BorderLayout());

		JPanel colorSettings = new JPanel();
		colorSettings.setLayout(new BoxLayout(colorSettings, BoxLayout.X_AXIS));
		JColor colorChoser = new JColor(window);
		colorChoser.setColor(colorSelector.getForegroundColor());
		colorSettings.add(L10N.label("dialog.imageeditor.color"));
		colorSettings.add(Box.createRigidArea(new Dimension(25, 0)));
		colorSettings.add(colorChoser);

		JCheckBox lock = L10N.checkbox("dialog.imageeditor.saturation_brightness_lock");

		JButton cancel = L10N.button(UIManager.getString("OptionPane.cancelButtonText"));
		JButton ok = L10N.button("dialog.imageeditor.recolor_action");
		ok.setBackground((Color) UIManager.get("MCreatorLAF.MAIN_TINT"));
		ok.setForeground((Color) UIManager.get("MCreatorLAF.BLACK_ACCENT"));
		getRootPane().setDefaultButton(ok);

		GridBagConstraints layoutConstraints = new GridBagConstraints();

		cancel.addActionListener(e -> setVisible(false));

		ok.addActionListener(e -> {
			BufferedImage bim = ImageUtils.deepCopy(layer.getRaster());
			Graphics2D g2d = layer.createGraphics();
			g2d.setBackground(new Color(0, 0, 0, 0));
			g2d.clearRect(0, 0, getWidth(), getHeight());
			g2d.drawImage(ImageUtils.toBufferedImage(
							ImageUtils.colorize(new ImageIcon(bim), colorChoser.getColor(), !lock.isSelected()).getImage()),
					null, 0, 0);
			g2d.dispose();
			layer.mergeOverlay();
			versionManager.addRevision(new Modification(canvas, layer));
			setVisible(false);
		});

		layoutConstraints.gridx = 0;
		layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		layoutConstraints.weightx = 1.0;
		layoutConstraints.weighty = 1.0;
		layoutConstraints.insets = new Insets(2, 2, 2, 2);
		layoutConstraints.gridheight = 1;

		settings.add(colorSettings, layoutConstraints);
		settings.add(lock, layoutConstraints);

		controls.add(cancel, BorderLayout.WEST);
		controls.add(ok, BorderLayout.EAST);
		add(PanelUtils.maxMargin(settings, 5, true, true, true, true), BorderLayout.CENTER);
		add(PanelUtils.maxMargin(controls, 5, true, true, true, true), BorderLayout.SOUTH);
		setSize(300, 150);
		setResizable(false);
		setLocationRelativeTo(window);
	}
}