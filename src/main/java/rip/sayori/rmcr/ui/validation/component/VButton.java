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

package rip.sayori.rmcr.ui.validation.component;

import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.validation.IValidable;
import rip.sayori.rmcr.ui.validation.Validator;

import javax.swing.*;
import java.awt.*;

public class VButton extends JButton implements IValidable {

	//validation code
	private Validator validator = null;
	private Validator.ValidationResult currentValidationResult = null;

	public VButton(String text) {
		super(text);
	}

	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (validator != null && currentValidationResult != null) {
			if (currentValidationResult.getValidationResultType() == Validator.ValidationResultType.WARNING) {
				g.drawImage(UIRES.get("18px.warning").getImage(), getWidth() - 16, getHeight() - 16, 16, 16, null);
			} else if (currentValidationResult.getValidationResultType() == Validator.ValidationResultType.ERROR) {
				g.drawImage(UIRES.get("18px.remove").getImage(), getWidth() - 16, getHeight() - 16, 16, 16, null);
			}
		}

	}

	@Override public Validator.ValidationResult getValidationStatus() {
		Validator.ValidationResult validationResult = validator == null ? null : validator.validateIfEnabled(this);

		this.currentValidationResult = validationResult;

		//repaint as new validation status might have to be rendered
		repaint();

		return validationResult;
	}

	@Override public Validator getValidator() {
		return validator;
	}

	@Override public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
