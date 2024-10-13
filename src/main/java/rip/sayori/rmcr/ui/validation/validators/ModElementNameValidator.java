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

package rip.sayori.rmcr.ui.validation.validators;

import rip.sayori.rmcr.java.JavaConventions;
import rip.sayori.rmcr.ui.validation.Validator;
import rip.sayori.rmcr.ui.validation.component.VTextField;
import rip.sayori.rmcr.workspace.Workspace;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.jetbrains.annotations.NotNull;

public class ModElementNameValidator extends JavaMemeberNameValidator {

	private final VTextField textField;
	private final Workspace workspace;

	public ModElementNameValidator(@NotNull Workspace workspace, VTextField textField) {
		super(textField, true);
		this.textField = textField;
		this.workspace = workspace;
	}

	@Override public ValidationResult validate() {
		for (ModElement element : workspace.getModElements()) {
			if (element.getName().equalsIgnoreCase(JavaConventions.convertToValidClassName(textField.getText()))) {
				return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
						"Mod element with this name already exists in this workspace");
			}
		}
		return super.validate();
	}

}
