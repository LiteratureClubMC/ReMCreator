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

import rip.sayori.rmcr.generator.mapping.MappableElement;
import rip.sayori.rmcr.generator.mapping.NameMapper;
import rip.sayori.rmcr.minecraft.DataListEntry;
import rip.sayori.rmcr.workspace.Workspace;
import org.jetbrains.annotations.NotNull;

public class AchievementEntry extends MappableElement {

	private AchievementEntry() {
		super(new NameMapper(null, "achievements"));
	}

	public AchievementEntry(Workspace owner, String name) {
		this();
		mapper.setWorkspace(owner);
		setValue(name);
	}

	public AchievementEntry(@NotNull Workspace owner, DataListEntry name) {
		this(owner, name.getName());
	}

}
