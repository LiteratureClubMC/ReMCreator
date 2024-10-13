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

package rip.sayori.rmcr.blockly.data;

public class BlocklyLoader {

	public static BlocklyLoader INSTANCE;
	private final ExternalBlockLoader procedureBlockLoader;
	private final ExternalBlockLoader jsonTriggerLoader;
	private final ExternalBlockLoader aitaskBlockLoader;
	private final ExternalTriggerLoader externalTriggerLoader;
	private BlocklyLoader() {
		procedureBlockLoader = new ExternalBlockLoader("procedures");
		aitaskBlockLoader = new ExternalBlockLoader("aitasks");
		externalTriggerLoader = new ExternalTriggerLoader("triggers");
		jsonTriggerLoader = new ExternalBlockLoader("jsontriggers");
	}

	public static void init() {
		INSTANCE = new BlocklyLoader();
	}

	public ExternalBlockLoader getProcedureBlockLoader() {
		return procedureBlockLoader;
	}

	public ExternalBlockLoader getAITaskBlockLoader() {
		return aitaskBlockLoader;
	}

	public ExternalTriggerLoader getExternalTriggerLoader() {
		return externalTriggerLoader;
	}

	public ExternalBlockLoader getJSONTriggerLoader() {
		return jsonTriggerLoader;
	}

}
