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

package rip.sayori.rmcr.element.converter.fv6;

import com.google.gson.JsonElement;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.element.converter.IConverter;
import rip.sayori.rmcr.element.types.Block;
import rip.sayori.rmcr.element.types.GUI;
import rip.sayori.rmcr.workspace.Workspace;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIBindingInverter implements IConverter {

	private static final Logger LOG = LogManager.getLogger("GUIBindingInverter");

	@Override
	public GeneratableElement convert(Workspace workspace, GeneratableElement input, JsonElement jsonElementInput) {
		GUI gui = (GUI) input;
		try {
			if (jsonElementInput.getAsJsonObject().get("definition").getAsJsonObject().get("containerBlock")
					!= null) { // treat as crafting
				String containerBlock = jsonElementInput.getAsJsonObject().get("definition").getAsJsonObject()
						.get("containerBlock").getAsString();
				ModElement blockelement = workspace.getModElementByName(containerBlock);
				if (blockelement != null) {
					Block block = (Block) blockelement.getGeneratableElement();
					if (block != null) {
						block.guiBoundTo = input.getModElement().getName();
						workspace.getModElementManager().storeModElement(block);
					}
				}
			}
		} catch (Exception e) {
			LOG.warn("Could not get bound block for " + input.getModElement().getName());
		}
		return gui;
	}

	@Override public int getVersionConvertingTo() {
		return 6;
	}

}