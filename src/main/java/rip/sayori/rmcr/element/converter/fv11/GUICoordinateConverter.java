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

package rip.sayori.rmcr.element.converter.fv11;

import com.google.gson.JsonElement;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.element.converter.IConverter;
import rip.sayori.rmcr.element.parts.gui.GUIComponent;
import rip.sayori.rmcr.element.parts.gui.SizedComponent;
import rip.sayori.rmcr.element.types.GUI;
import rip.sayori.rmcr.workspace.Workspace;

public class GUICoordinateConverter implements IConverter {

	@Override
	public GeneratableElement convert(Workspace workspace, GeneratableElement input, JsonElement jsonElementInput) {
		GUI gui = (GUI) input;

		gui.width = convert(gui.width);
		gui.height = convert(gui.height);

		for (GUIComponent component : gui.components) {
			component.x = convert(component.getX());
			component.y = convert(component.getY());
			if (component instanceof SizedComponent) {
				((SizedComponent) component).width = convert(((SizedComponent) component).width);
				((SizedComponent) component).height = convert(((SizedComponent) component).height);
			}
		}

		return gui;
	}

	private int convert(int original) {
		return (int) Math.round(original / 2.0);
	}

	@Override public int getVersionConvertingTo() {
		return 11;
	}

}
