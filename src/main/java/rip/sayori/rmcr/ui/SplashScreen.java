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

package rip.sayori.rmcr.ui;

import rip.sayori.rmcr.Launcher;
import rip.sayori.rmcr.ui.component.ImagePanel;
import rip.sayori.rmcr.ui.component.ProgressBar;
import rip.sayori.rmcr.ui.component.util.EDTUtils;
import rip.sayori.rmcr.ui.init.UIRES;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

	private final ProgressBar initloadprogress = new ProgressBar();
	private final JLabel loadstate = new JLabel();

	public SplashScreen() {
		Font splashFont = new Font("Sans-Serif", Font.PLAIN, 13);

		JPanel imagePanel = new ImagePanel(UIRES.get("splash").getImage());
		imagePanel.setLayout(null);
		imagePanel.setBackground(new Color(50, 50, 50));

		JLabel logo = new JLabel(UIRES.get("logo"));
		logo.setBounds(24 + 8 - 4, 70, 350, 63);
		imagePanel.add(logo);

		JLabel version = new JLabel(
				"VERSION " + (Launcher.version != null ? Launcher.version.getMajorString() : "1234.5"));
		version.setFont(splashFont.deriveFont(18f));
		version.setForeground(Color.white);
		version.setBounds(30 + 10 - 4, 129, 500, 45);
		imagePanel.add(version);

		if (Launcher.version != null && Launcher.version.isSnapshot()) {
			JLabel snpashot = new JLabel("Snapshot - not for production use!");
			snpashot.setFont(splashFont.deriveFont(14f));
			snpashot.setForeground(new Color(255, 92, 82));
			snpashot.setBounds(30 + 10 - 4, 165, 500, 45);
			imagePanel.add(snpashot);
		}

		initloadprogress.setEmptyColor(null);
		initloadprogress.setOpaque(false);
		initloadprogress.setForeground(Color.white);
		initloadprogress.setMaximalValue(100);
		initloadprogress.init();
		initloadprogress.setBounds(30 + 10 - 4, 283 - 10, 568, 3);
		imagePanel.add(initloadprogress);

		loadstate.setFont(splashFont.deriveFont(12f));
		loadstate.setForeground(Color.white);
		loadstate.setBounds(30 + 10 - 4, 283 - 39 - 10, 500, 45);
		imagePanel.add(loadstate);

		add(imagePanel);
		setSize(640, 380);
		setLocationRelativeTo(null);
		setVisible(true);
		requestFocus();
		requestFocusInWindow();
		toFront();
	}

	public void setProgress(int percentage, String message) {
		initloadprogress.setCurrentValue(percentage);
		loadstate.setText(message);
		EDTUtils.requestNonBlockingUIRefresh();
	}

}
