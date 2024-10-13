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

package rip.sayori.rmcr.element.parts;

import rip.sayori.rmcr.blockly.data.Dependency;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.workspace.Workspace;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class Procedure {

	public transient boolean exists = false;
	private String name;

	public Procedure(String name) {
		this.name = name;
	}

	public static boolean isElementUsingProcedure(Object element, String procedureName) {
		boolean isCallingThisProcedure = false;

		for (Field field : element.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object value = field.get(element);
				if (value instanceof Procedure) {
					if (((Procedure) value).getName().equals(procedureName)) {
						isCallingThisProcedure = true;
						break;
					}
				}
			} catch (IllegalAccessException ignored) {
			}
		}

		return isCallingThisProcedure;
	}

	public String getName() {
		return name;
	}

	public List<Dependency> getDependencies(Workspace workspace) {
		GeneratableElement generatableElement = workspace.getModElementByName(name).getGeneratableElement();
		if (generatableElement instanceof rip.sayori.rmcr.element.types.Procedure) {
			this.exists = true;
			return ((rip.sayori.rmcr.element.types.Procedure) generatableElement).getDependencies();
		}

		this.exists = false;
		return Collections.emptyList();
	}

}
