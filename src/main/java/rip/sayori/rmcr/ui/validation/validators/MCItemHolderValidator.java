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

import rip.sayori.rmcr.ui.minecraft.MCItemHolder;
import rip.sayori.rmcr.ui.validation.Validator;

import javax.swing.*;

public class MCItemHolderValidator implements Validator {

	private final MCItemHolder holder;
	private JToggleButton requirement;

	public MCItemHolderValidator(MCItemHolder holder) {
		this.holder = holder;
	}

	public MCItemHolderValidator(MCItemHolder holder, JToggleButton requirement) {
		this.holder = holder;
		this.requirement = requirement;
	}

	@Override public ValidationResult validate() {
		if (holder.containsItem() || (requirement != null && !requirement.isSelected()))
			return Validator.ValidationResult.PASSED;
		else
			return new Validator.ValidationResult(Validator.ValidationResultType.ERROR, "Please select element");
	}
}
