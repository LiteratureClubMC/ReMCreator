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

package rip.sayori.rmcr.ui.action.impl.vcs;

import rip.sayori.rmcr.ui.action.ActionRegistry;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.vcs.VCSSetupDialogs;
import rip.sayori.rmcr.vcs.VCSInfo;

import java.io.File;

public class VCSInfoSettingsAction extends VCSAction {

	public VCSInfoSettingsAction(ActionRegistry actionRegistry) {
		super(actionRegistry, L10N.t("action.vcs.settings"), e -> {
			VCSInfo vcsInfo = actionRegistry.getMCreator().getWorkspace().getVCS().getInfo();
			VCSInfo newInfo;
			if (vcsInfo != null) {
				newInfo = VCSSetupDialogs.getVCSInfoDialog(actionRegistry.getMCreator(),
						L10N.t("dialog.vcs.settings.help.message"), vcsInfo.getRemote(), vcsInfo.getUsername(),
						vcsInfo.isPromptForPassword(), false);
			} else {
				newInfo = VCSSetupDialogs.getVCSInfoDialog(actionRegistry.getMCreator(),
						L10N.t("dialog.vcs.settings.help.message"));
			}

			if (newInfo != null) {
				actionRegistry.getMCreator().getWorkspace().getVCS().setInfo(newInfo);
				VCSInfo.saveToFile(newInfo,
						new File(actionRegistry.getMCreator().getFolderManager().getWorkspaceCacheDir(), "vcsInfo"));
			}
		});
		setIcon(UIRES.get("16px.vcs"));
	}

}
