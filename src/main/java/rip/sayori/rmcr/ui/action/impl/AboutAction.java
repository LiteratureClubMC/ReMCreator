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

package rip.sayori.rmcr.ui.action.impl;

import rip.sayori.rmcr.Launcher;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.io.FileIO;
import rip.sayori.rmcr.io.OS;
import rip.sayori.rmcr.ui.MCreatorApplication;
import rip.sayori.rmcr.ui.action.ActionRegistry;
import rip.sayori.rmcr.ui.action.BasicAction;
import rip.sayori.rmcr.ui.component.util.ComponentUtils;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.laf.AbstractMCreatorTheme;
import rip.sayori.rmcr.util.DesktopUtils;
import rip.sayori.rmcr.util.image.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AboutAction extends BasicAction {

	public AboutAction(ActionRegistry actionRegistry) {
		super(actionRegistry, L10N.t("action.about"), evt -> showDialog(actionRegistry.getMCreator()));
	}

	public static void showDialog(Window parent) {
		Object[] options = { L10N.t("dialog.about.option.website"), L10N.t("dialog.about.option.support"),
				L10N.t("dialog.about.option.eula"), L10N.t("dialog.about.option.third_party_licenses") };
		int n = JOptionPane.showOptionDialog(parent,
				L10N.t("dialog.about.message", Launcher.version.major, Launcher.version.getFullString(), "",
						GeneratableElement.formatVersion, OS.getSystemBits(), OS.getBundledJVMBits()),
				L10N.t("dialog.about.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(generateMCreatorLogoForAboutDialog()), options, options[0]);
		if (n == 0) {
			DesktopUtils.browseSafe("https://github.com/LiteratureClubMC/ReMCreator");
		} else if (n == 1) {
			DesktopUtils.browseSafe(MCreatorApplication.SERVER_DOMAIN + "/support");
		} else if (n == 2) {
			showLicenseWindow(parent);
		} else if (n == 3) {
			try {
				Desktop.getDesktop().open(new File("./license"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(parent, L10N.t("dialog.about.third_party.message"),
						L10N.t("dialog.about.third_party.title"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private static Image generateMCreatorLogoForAboutDialog() {
		BufferedImage image = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.drawImage(UIRES.get("icon").getImage(), 54, 24, 128, 128, null);
		g.drawImage(ImageUtils.colorize(UIRES.get("logo"), new Color(0x2F2F2F), true).getImage(), 22, 170, 200, 36,
				null);
		return image;
	}

	public static void showLicenseWindow(Window parent) {
		JTextArea licenseText = new JTextArea();
		JScrollPane gradlesp = new JScrollPane(licenseText);
		licenseText.setEditable(false);
		licenseText.setLineWrap(true);
		licenseText.setFont(AbstractMCreatorTheme.console_font);
		ComponentUtils.deriveFont(licenseText, 12);
		licenseText.setWrapStyleWord(true);
		licenseText.setText(FileIO.readFileToString(new File("./LICENSE.txt")));
		gradlesp.setPreferredSize(new Dimension(570, 570));
		gradlesp.getHorizontalScrollBar().setValue(0);
		gradlesp.getVerticalScrollBar().setValue(0);
		licenseText.setCaretPosition(0);
		JOptionPane.showOptionDialog(parent, gradlesp, L10N.t("dialog.about.eula.title"), JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { L10N.t("dialog.about.eula.close") },
				L10N.t("dialog.about.eula.close"));
	}

}
