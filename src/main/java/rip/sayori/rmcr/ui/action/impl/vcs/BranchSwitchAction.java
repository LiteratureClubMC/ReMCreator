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

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import rip.sayori.rmcr.generator.Generator;
import rip.sayori.rmcr.generator.setup.WorkspaceGeneratorSetup;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.dialogs.workspace.WorkspaceGeneratorSetupDialog;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.util.GSONClone;
import rip.sayori.rmcr.vcs.WorkspaceVCS;
import rip.sayori.rmcr.workspace.Workspace;
import rip.sayori.rmcr.workspace.settings.WorkspaceSettings;

import javax.swing.*;
import java.util.stream.Collectors;

public class BranchSwitchAction {

	private static final Logger LOG = LogManager.getLogger(BranchSwitchAction.class);

	public static void switchBranch(MCreator mcreator, WorkspaceVCS workspaceVCS, String branchToSwitchTo) {
		int n = JOptionPane.showConfirmDialog(mcreator, L10N.t("dialog.vcs.switch_branch.message", branchToSwitchTo),
				L10N.t("dialog.vcs.switch_branch.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (n == 0) {
			Git git = workspaceVCS.getGit();

			CredentialsProvider credentialsProvider = workspaceVCS.getCredentialsProvider(mcreator.getWorkspaceFolder(),
					mcreator);

			try {
				Workspace localWorkspace = mcreator.getWorkspace();
				WorkspaceSettings preSwitchSettings = GSONClone.deepClone(localWorkspace.getWorkspaceSettings(),
						WorkspaceSettings.class);

				try { // first try to fetch remote changes
					git.fetch().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
				} catch (Exception ignored) {
				}

				git.reset().setMode(ResetCommand.ResetType.HARD).call();
				git.checkout().setCreateBranch(
								!git.branchList().call().stream().map(Ref::getName).collect(Collectors.toList())
										.contains("refs/heads/" + FilenameUtils.getName(branchToSwitchTo)))
						.setName(FilenameUtils.getName(branchToSwitchTo)).call();

				// possible refactor after sync start
				mcreator.getWorkspace().reloadFromFS();

				localWorkspace = mcreator.getWorkspace();

				// if version changed, switch the generator
				if (!localWorkspace.getWorkspaceSettings().getCurrentGenerator()
						.equals(preSwitchSettings.getCurrentGenerator())) {
					LOG.debug("Switching local workspace generator to " + localWorkspace.getWorkspaceSettings()
							.getCurrentGenerator());
					WorkspaceGeneratorSetup.cleanupGeneratorForSwitchTo(localWorkspace,
							Generator.GENERATOR_CACHE.get(localWorkspace.getWorkspaceSettings().getCurrentGenerator()));
					localWorkspace.switchGenerator(localWorkspace.getWorkspaceSettings().getCurrentGenerator());
					WorkspaceGeneratorSetupDialog.runSetup(mcreator, false);
				}
				// possible refactor after sync end

				mcreator.statusBar.setPersistentMessage(
						L10N.t("statusbar.vcs.switched_working_branch", FilenameUtils.getName(branchToSwitchTo)));

				mcreator.statusBar.reloadVCSStatus();

				mcreator.mv.updateMods();
			} catch (GitAPIException e) {
				LOG.error("Failed to switch branch!", e);
				JOptionPane.showMessageDialog(mcreator,
						L10N.t("dialog.vcs.switch_branch_fail.message", branchToSwitchTo, e.getMessage()),
						L10N.t("dialog.vcs.switch_branch_fail.push_fail.title"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
