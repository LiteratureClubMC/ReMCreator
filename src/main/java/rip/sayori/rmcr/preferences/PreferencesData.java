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

package rip.sayori.rmcr.preferences;

import rip.sayori.rmcr.io.OS;
import rip.sayori.rmcr.ui.laf.AbstractMCreatorTheme;

import java.awt.*;
import java.io.File;
import java.util.Locale;

public class PreferencesData {

	@PreferencesSection public UISettings ui = new UISettings();
	@PreferencesSection public BackupsSettings backups = new BackupsSettings();
	@PreferencesSection public BlocklySettings blockly = new BlocklySettings();
	@PreferencesSection public IDESettings ide = new IDESettings();
	@PreferencesSection public GradleSettings gradle = new GradleSettings();

	public HiddenPreferences hidden = new HiddenPreferences();

	public enum WorkspaceSortType {
		NAME, CREATED, TYPE, LOADORDER
	}

	public enum WorkspaceIconSize {
		LARGE, SMALL, LIST
	}

	public static class UISettings {

		@PreferencesEntry public Color interfaceAccentColor = AbstractMCreatorTheme.MAIN_TINT_DEFAULT;

		@PreferencesEntry public Locale language = new Locale("en", "US");

		@PreferencesEntry public boolean aatext = true;

		@PreferencesEntry(arrayData = { "on", "off", "gasp", "lcd", "lcd_hbgr", "lcd_vrgb", "lcd_vbgr" })
		public String textAntialiasingType = "on";

		@PreferencesEntry(arrayData = { "Dark theme", "Light theme" }) public String interfaceTheme = "Dark theme";

		@PreferencesEntry public boolean expandSectionsByDefault = false;
		@PreferencesEntry public boolean use2DAcceleration = false;
		@PreferencesEntry public boolean autoreloadTabs = true;
		@PreferencesEntry public boolean discordRichPresenceEnable = true;

	}

	public static class BackupsSettings {

		@PreferencesEntry(min = 10, max = 1800) public int workspaceAutosaveInterval = 30;
		@PreferencesEntry(min = 3, max = 120) public int automatedBackupInterval = 5;
		@PreferencesEntry(min = 2, max = 20) public int numberOfBackupsToStore = 10;
		@PreferencesEntry public boolean backupOnVersionSwitch = true;

	}

	public static class BlocklySettings {

		@PreferencesEntry(arrayData = { "Geras", "Thrasos" }) public String blockRenderer = "Thrasos";
		@PreferencesEntry public boolean useSmartSort = true;
		@PreferencesEntry public boolean enableComments = true;
		@PreferencesEntry public boolean enableCollapse = true;
		@PreferencesEntry public boolean enableTrashcan = true;
		@PreferencesEntry(min = 95, max = 200) public int maxScale = 100;
		@PreferencesEntry(min = 20, max = 95) public int minScale = 40;
		@PreferencesEntry(min = 0, max = 200) public int scaleSpeed = 105;
		@PreferencesEntry public boolean legacyFont = false;

	}

	public static class IDESettings {

		@PreferencesEntry(arrayData = { "MCreator", "Default", "Default-Alt", "Dark", "Eclipse", "Idea", "Monokai",
				"VS" }) public String editorTheme = "MCreator";

		@PreferencesEntry(min = 5, max = 48) public int fontSize = 12;
		@PreferencesEntry public boolean autocomplete = true;
		@PreferencesEntry(arrayData = { "Manual", "Trigger on dot", "Smart" }) public String autocompleteMode = "Smart";
		@PreferencesEntry public boolean autocompleteDocWindow = true;
		@PreferencesEntry public boolean lineNumbers = true;
		@PreferencesEntry public boolean errorInfoEnable = true;

	}

	public static class GradleSettings {

		@PreferencesEntry public boolean compileOnSave = true;
		@PreferencesEntry public boolean passLangToMinecraft = true;

		@PreferencesEntry(min = 128, meta = "max:maxram") public int xms =
				OS.getBundledJVMBits() == OS.BIT64 ? 625 : 512;

		@PreferencesEntry(min = 128, meta = "max:maxram") public int xmx =
				OS.getBundledJVMBits() == OS.BIT64 ? 2048 : 1500;

		@PreferencesEntry public boolean offline = false;
	}

	public static class HiddenPreferences {
		public WorkspaceIconSize workspaceIconSize = WorkspaceIconSize.LARGE;
		public boolean fullScreen = false;
		public int projectTreeSplitPos = 0;
		public boolean workspaceSortAscending = true;
		public WorkspaceSortType workspaceSortType = WorkspaceSortType.CREATED;
		public File java_home = null;
	}

}
