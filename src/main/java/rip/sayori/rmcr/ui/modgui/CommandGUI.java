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

import rip.sayori.rmcr.blockly.data.Dependency;
import rip.sayori.rmcr.element.types.Command;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.MCreatorApplication;
import rip.sayori.rmcr.ui.component.util.ComponentUtils;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.help.HelpUtils;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.minecraft.ProcedureSelector;
import rip.sayori.rmcr.ui.validation.AggregatedValidationResult;
import rip.sayori.rmcr.ui.validation.ValidationGroup;
import rip.sayori.rmcr.ui.validation.component.VTextField;
import rip.sayori.rmcr.ui.validation.validators.TextFieldValidator;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class CommandGUI extends ModElementGUI<Command> {

	private final VTextField commandName = new VTextField(25);
	private final JComboBox<String> permissionLevel = new JComboBox<>(
			new String[] { "No requirement", "1", "2", "3", "4" });
	private final ValidationGroup page1group = new ValidationGroup();
	private ProcedureSelector onCommandExecuted;

	public CommandGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
		super(mcreator, modElement, editingMode);
		this.initGUI();
		super.finalizeGUI();
	}

	@Override protected void initGUI() {
		onCommandExecuted = new ProcedureSelector(this.withEntry("command/when_executed"), mcreator,
				L10N.t("elementgui.command.when_command_executed"),
				Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/cmdparams:map"));

		JPanel pane5 = new JPanel(new BorderLayout(10, 10));

		ComponentUtils.deriveFont(commandName, 16);

		JPanel enderpanel = new JPanel(new GridLayout(2, 2, 10, 2));

		enderpanel.add(
				HelpUtils.wrapWithHelpButton(this.withEntry("command/name"), L10N.label("elementgui.command.name")));
		enderpanel.add(commandName);

		enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("command/permission_level"),
				L10N.label("elementgui.command.permission_level")));
		enderpanel.add(permissionLevel);

		enderpanel.setOpaque(false);
		pane5.setOpaque(false);

		JPanel evente = new JPanel();
		evente.setOpaque(false);
		evente.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 2),
				L10N.t("elementgui.command.on_command_executed"), 0, 0, getFont().deriveFont(12.0f),
				(Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
		evente.add(onCommandExecuted);

		JPanel merge = new JPanel(new BorderLayout(25, 25));
		merge.setOpaque(false);
		merge.add("North", PanelUtils.centerInPanel(enderpanel));
		merge.add("South", evente);

		pane5.add("Center", PanelUtils.totalCenterInPanel(PanelUtils.centerInPanel(merge)));

		commandName.setValidator(
				new TextFieldValidator(commandName, L10N.t("elementgui.command.warning.empty_string")));
		commandName.enableRealtimeValidation();

		page1group.addValidationElement(commandName);

		addPage(pane5);

		if (!isEditingMode()) {
			commandName.setText(modElement.getName().toLowerCase(Locale.ENGLISH));
		}
	}

	@Override public void reloadDataLists() {
		super.reloadDataLists();
		onCommandExecuted.refreshListKeepSelected();
	}

	@Override protected AggregatedValidationResult validatePage(int page) {
		return new AggregatedValidationResult(page1group);
	}

	@Override public void openInEditingMode(Command command) {
		onCommandExecuted.setSelectedProcedure(command.onCommandExecuted);
		commandName.setText(command.commandName);
		permissionLevel.setSelectedItem(command.permissionLevel);
	}

	@Override public Command getElementFromGUI() {
		Command command = new Command(modElement);
		command.commandName = commandName.getText();
		command.onCommandExecuted = onCommandExecuted.getSelectedProcedure();
		command.permissionLevel = (String) permissionLevel.getSelectedItem();
		return command;
	}

	@Override public @Nullable URI getContextURL() throws URISyntaxException {
		return new URI(MCreatorApplication.SERVER_DOMAIN + "/wiki/making-command");
	}

}
