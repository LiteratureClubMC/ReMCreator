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

import javafx.application.Platform;
import rip.sayori.rmcr.Launcher;
import rip.sayori.rmcr.blockly.data.BlocklyLoader;
import rip.sayori.rmcr.generator.Generator;
import rip.sayori.rmcr.generator.GeneratorConfiguration;
import rip.sayori.rmcr.io.FileIO;
import rip.sayori.rmcr.io.OS;
import rip.sayori.rmcr.io.net.analytics.DeviceInfo;
import rip.sayori.rmcr.io.net.api.D8WebAPI;
import rip.sayori.rmcr.io.net.api.IWebAPI;
import rip.sayori.rmcr.minecraft.DataListLoader;
import rip.sayori.rmcr.minecraft.api.ModAPIManager;
import rip.sayori.rmcr.plugin.PluginLoader;
import rip.sayori.rmcr.preferences.PreferencesManager;
import rip.sayori.rmcr.ui.action.impl.AboutAction;
import rip.sayori.rmcr.ui.component.util.DiscordClient;
import rip.sayori.rmcr.ui.component.util.MacOSUIUtil;
import rip.sayori.rmcr.ui.dialogs.preferences.PreferencesDialog;
import rip.sayori.rmcr.ui.help.HelpLoader;
import rip.sayori.rmcr.ui.init.*;
import rip.sayori.rmcr.ui.laf.MCreatorLookAndFeel;
import rip.sayori.rmcr.ui.workspace.selector.RecentWorkspaceEntry;
import rip.sayori.rmcr.ui.workspace.selector.WorkspaceSelector;
import rip.sayori.rmcr.util.MCreatorVersionNumber;
import rip.sayori.rmcr.util.SoundUtils;
import rip.sayori.rmcr.workspace.CorruptedWorkspaceFileException;
import rip.sayori.rmcr.workspace.UnsupportedGeneratorException;
import rip.sayori.rmcr.workspace.Workspace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public final class MCreatorApplication {

	public static final String SERVER_DOMAIN = "https://mcreator.net";
	private static final Logger LOG = LogManager.getLogger("Application");
	public static IWebAPI WEB_API = new D8WebAPI();
	public static boolean isInternet = true;
	private static boolean applicationStarted = false;
	private final DeviceInfo deviceInfo;
	private final WorkspaceSelector workspaceSelector;

	private final List<MCreator> openMCreators = new ArrayList<>();

	private final DiscordClient discordClient;

	private MCreatorApplication(List<String> launchArguments) {
		final SplashScreen splashScreen = new SplashScreen();
		splashScreen.setVisible(true);

		splashScreen.setProgress(5, "Loading UI core");

		try {
			UIManager.setLookAndFeel(new MCreatorLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			LOG.error("Failed to set look and feel: " + e.getMessage());
		}

		SoundUtils.initSoundSystem();

		splashScreen.setProgress(20, "Preloading resources");

		UIRES.preloadImages();
		TiledImageCache.loadAndTileImages();

		splashScreen.setProgress(30, "Loading plugins");

		PluginLoader.initInstance();

		splashScreen.setProgress(40, "Loading interface components");

		// load translations after plugins are loaded
		L10N.initTranslations();

		// preload help entries cache
		HelpLoader.preloadCache();

		splashScreen.setProgress(45, "Loading plugin data");

		// load datalists and icons for them after plugins are loaded
		BlockItemIcons.init();
		DataListLoader.preloadCache();

		// load templates for image makers
		ImageMakerTexturesCache.init();
		ArmorMakerTexturesCache.init();

		// load apis defined by plugins after plugins are loaded
		ModAPIManager.initAPIs();

		// load blockly blocks after plugins are loaded
		BlocklyLoader.init();

		splashScreen.setProgress(55, "Loading generators");

		Set<String> fileNamesUnordered = PluginLoader.INSTANCE.getResources(Pattern.compile("generator\\.yaml"));
		List<String> fileNames = new ArrayList<>(fileNamesUnordered);
		Collections.sort(fileNames);
		int i = 0;
		for (String generator : fileNames) {
			splashScreen.setProgress(55 + i * ((85 - 55) / fileNames.size()),
					"Loading generators: " + generator.split("/")[0]);
			LOG.info("Loading generator: " + generator);
			generator = generator.replace("/generator.yaml", "");
			try {
				Generator.GENERATOR_CACHE.put(generator, new GeneratorConfiguration(generator));
			} catch (Exception e) {
				LOG.error("Failed to load generator: " + generator, e);
			}
			i++;
		}

		splashScreen.setProgress(88, "Initiating user session");

		deviceInfo = new DeviceInfo();

		isInternet = MCreatorApplication.WEB_API.initAPI();

		splashScreen.setProgress(100, "Loading MCreator windows");

		if (OS.getOS() == OS.MAC) {
			MacOSUIUtil.registerAboutHandler(() -> AboutAction.showDialog(null));
			MacOSUIUtil.registerPreferencesHandler(() -> new PreferencesDialog(null, null));
			MacOSUIUtil.registerQuitHandler(this::closeApplication);
		}

		discordClient = new DiscordClient();

		workspaceSelector = new WorkspaceSelector(this, this::openWorkspaceInMCreator);

		discordClient.updatePresence(L10N.t("dialog.discord_rpc.just_opened"),
				L10N.t("dialog.discord_rpc.version") + Launcher.version.getMajorString());

		boolean directLaunch = false;
		if (!launchArguments.isEmpty()) {
			String lastArg = launchArguments.get(launchArguments.size() - 1);
			if (lastArg.length() >= 2 && lastArg.charAt(0) == '"' && lastArg.charAt(lastArg.length() - 1) == '"')
				lastArg = lastArg.substring(1, lastArg.length() - 1);
			File passedFile = new File(lastArg);
			if (passedFile.isFile() && passedFile.getName().endsWith(".mcreator")) {
				splashScreen.setVisible(false);
				openWorkspaceInMCreator(passedFile);
				directLaunch = true;
			}
		}

		if (!directLaunch)
			showWorkspaceSelector();

		splashScreen.setVisible(false);
	}

	public static void createApplication(List<String> arguments) {
		if (!applicationStarted) {
			SwingUtilities.invokeLater(() -> new MCreatorApplication(arguments));
			applicationStarted = true;
		}
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public WorkspaceSelector getWorkspaceSelector() {
		return workspaceSelector;
	}

	public void openWorkspaceInMCreator(File workspaceFile) {
		this.workspaceSelector.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			Workspace workspace = Workspace.readFromFS(workspaceFile, this.workspaceSelector);
			if (workspace.getMCreatorVersion() > Launcher.version.versionlong
					&& !MCreatorVersionNumber.isBuildNumberDevelopment(workspace.getMCreatorVersion())) {
				JOptionPane.showMessageDialog(workspaceSelector, L10N.t("dialog.workspace.open_failed_message"),
						L10N.t("dialog.workspace.open_failed_title"), JOptionPane.ERROR_MESSAGE);
			} else {
				MCreator mcreator = new MCreator(this, workspace);
				if (!this.openMCreators.contains(mcreator)) {
					this.workspaceSelector.setVisible(false);
					this.openMCreators.add(mcreator);
					mcreator.setVisible(true);
					mcreator.requestFocusInWindow();
					mcreator.toFront();
				} else { // already open, just focus it
					LOG.warn("Trying to open already open workspace, bringing it to the front.");
					for (MCreator openmcreator : openMCreators) {
						if (openmcreator.equals(mcreator)) {
							openmcreator.requestFocusInWindow();
							openmcreator.toFront();
						}
					}
				}
				this.workspaceSelector.addOrUpdateRecentWorkspace(
						new RecentWorkspaceEntry(mcreator.getWorkspace(), workspaceFile));
			}
		} catch (CorruptedWorkspaceFileException corruptedWorkspaceFile) {
			LOG.fatal("Failed to open workspace!", corruptedWorkspaceFile);

			File backupsDir = new File(workspaceFile.getParentFile(), ".mcreator/workspaceBackups");
			if (backupsDir.isDirectory()) {
				String[] files = backupsDir.list();
				if (files != null) {
					String[] backups = Arrays.stream(files).filter(e -> e.contains(".mcreator-backup"))
							.sorted(Collections.reverseOrder()).toArray(String[]::new);
					String selected = (String) JOptionPane.showInputDialog(this.workspaceSelector,
							L10N.t("dialog.workspace.got_corrupted_message"),
							L10N.t("dialog.workspace.got_corrupted_title"), JOptionPane.QUESTION_MESSAGE, null, backups,
							"");
					if (selected != null) {
						File backup = new File(backupsDir, selected);
						FileIO.copyFile(backup, workspaceFile);
						openWorkspaceInMCreator(workspaceFile);
					} else {
						reportFailedWorkspaceOpen(
								new IOException("User canceled workspace backup restoration", corruptedWorkspaceFile));
					}
				}
			} else {
				reportFailedWorkspaceOpen(
						new IOException("Corrupted workspace file and no backups found", corruptedWorkspaceFile));
			}
		} catch (IOException | UnsupportedGeneratorException e) {
			reportFailedWorkspaceOpen(e);
		}
		this.workspaceSelector.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	private void reportFailedWorkspaceOpen(Exception e) {
		JOptionPane.showMessageDialog(this.workspaceSelector,
				L10N.t("dialog.workspace.is_not_valid_message") + e.getMessage(),
				L10N.t("dialog.workspace.is_not_valid_title"), JOptionPane.ERROR_MESSAGE);
	}

	public final void closeApplication() {
		LOG.debug("Closing any potentially open MCreator windows");
		List<MCreator> mcreatorsTmp = new ArrayList<>(
				openMCreators); // create list copy so we don't modify the list we iterate
		for (MCreator mcreator : mcreatorsTmp) {
			LOG.info("Attempting to close MCreator window with workspace: " + mcreator.getWorkspace());
			if (!mcreator.closeThisMCreator(false))
				return; // if we fail to close all windows, we cancel the application close
		}

		LOG.debug("Performing exit tasks");
		PreferencesManager.storePreferences(PreferencesManager.PREFERENCES); // store any potential preferences changes

		discordClient.close(); // close discord client

		SoundUtils.close();

		// we close all windows and exit fx platform
		try {
			LOG.debug("Stopping AWT and FX threads");
			Arrays.stream(Window.getWindows()).forEach(Window::dispose);
			Platform.exit();
		} catch (Exception ignored) {
		}

		try {
			PluginLoader.INSTANCE.close();
		} catch (IOException e) {
			LOG.warn("Failed to close plugin loader", e);
		}

		try {
			Thread.sleep(1000); // additional sleep for more robustness
		} catch (Exception ignored) {
		}

		LOG.debug("Exiting MCreator");
		System.exit(0); // actually exit MCreator
	}

	final void showWorkspaceSelector() {
		workspaceSelector.setVisible(true);
	}

	List<RecentWorkspaceEntry> getRecentWorkspaces() {
		return workspaceSelector.getRecentWorkspaces().getList();
	}

	public List<MCreator> getOpenMCreators() {
		return openMCreators;
	}

	public DiscordClient getDiscordClient() {
		return discordClient;
	}
}
