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

package rip.sayori.rmcr.ui.ide.action;

import rip.sayori.rmcr.ui.action.ActionRegistry;
import rip.sayori.rmcr.ui.action.BasicAction;
import rip.sayori.rmcr.ui.ide.CodeEditorView;

import javax.swing.*;

public class ShowReplaceAction extends BasicAction {

	public ShowReplaceAction(ActionRegistry actionRegistry) {
		super(actionRegistry, "Replace in code", actionEvent -> {
			JPanel pan = actionRegistry.getMCreator().mcreatorTabs.getCurrentTab().getContent();
			if (pan instanceof CodeEditorView) {
				CodeEditorView codeEditorView = (CodeEditorView) pan;
				codeEditorView.sed.setVisible(false);
				codeEditorView.rep.setVisible(true);
				codeEditorView.disableJumpToMode();
			}
		});
		actionRegistry.getMCreator().mcreatorTabs.addTabShownListener(
				tab -> setEnabled(tab.getContent() instanceof CodeEditorView));
	}

}
