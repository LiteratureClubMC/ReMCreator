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

package rip.sayori.rmcr.ui.action.impl.gradle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rip.sayori.rmcr.preferences.PreferencesManager;
import rip.sayori.rmcr.ui.action.ActionRegistry;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.minecraft.MinecraftOptionsUtils;

import javax.swing.*;

public class RunClientAction extends GradleAction {

	private static final Logger LOG = LogManager.getLogger(RunClientAction.class);

	public RunClientAction(ActionRegistry actionRegistry) {
		super(actionRegistry, L10N.t("action.run_client"), evt -> new Thread(() -> {
			actionRegistry.getMCreator().getGradleConsole()
					.markRunning(); // so console gets locked while we generate code already
			try {
				actionRegistry.getMCreator().getGenerator().runResourceSetupTasks();
				actionRegistry.getMCreator().getGenerator().generateBase();

				if (PreferencesManager.PREFERENCES.gradle.passLangToMinecraft)
					MinecraftOptionsUtils.setLangTo(actionRegistry.getMCreator().getWorkspace(),
							L10N.getLocaleString());

				if (actionRegistry.getMCreator().getGeneratorConfiguration().getGradleTaskFor("run_client")
						.equals("@bedrock_run_client")) {
				} else {
					SwingUtilities.invokeLater(() -> actionRegistry.getMCreator().getGradleConsole()
							.exec(actionRegistry.getMCreator().getGeneratorConfiguration()
									.getGradleTaskFor("run_client")));
				}
			} catch (Exception e) { // if something fails, we still need to free the gradle console
				LOG.error(e.getMessage(), e);
				actionRegistry.getMCreator().getGradleConsole().markReady();
			}
		}).start());
	}

	@Override public boolean isEnabled() {
		return actionRegistry.getMCreator().getGeneratorConfiguration().getGradleTaskFor("run_client") != null
				&& super.isEnabled();
	}

}
