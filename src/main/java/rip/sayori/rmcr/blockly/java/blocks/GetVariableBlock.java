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

package rip.sayori.rmcr.blockly.java.blocks;

import rip.sayori.rmcr.blockly.BlocklyCompileNote;
import rip.sayori.rmcr.blockly.BlocklyToCode;
import rip.sayori.rmcr.blockly.IBlockGenerator;
import rip.sayori.rmcr.blockly.data.Dependency;
import rip.sayori.rmcr.blockly.data.StatementInput;
import rip.sayori.rmcr.blockly.java.BlocklyToProcedure;
import rip.sayori.rmcr.generator.template.TemplateGeneratorException;
import rip.sayori.rmcr.util.XMLUtil;
import rip.sayori.rmcr.workspace.elements.VariableElement;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetVariableBlock implements IBlockGenerator {

	@Override public void generateBlock(BlocklyToCode master, Element block) throws TemplateGeneratorException {
		String type;

		String blocktype = block.getAttribute("type");
		switch (blocktype) {
		case "variables_get_number":
			type = "NUMBER";
			break;
		case "variables_get_text":
			type = "STRING";
			break;
		case "variables_get_logic":
			type = "LOGIC";
			break;
		case "variables_get_itemstack":
			type = "ITEMSTACK";
			break;
		default:
			return;
		}

		Element variable = XMLUtil.getFirstChildrenWithName(block, "field");
		if (variable != null) {
			String[] varfield = variable.getTextContent().split(":");
			if (varfield.length == 2) {
				String scope = varfield[0];
				String name = varfield[1];

				if (scope.equals("global") && !master.getWorkspace().getVariableElements().stream()
						.map(VariableElement::getName).collect(Collectors.toList()).contains(name)) {
					master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
							"Variable get block is bound to a variable that does not exist. Remove this block!"));
					return;
				} else if (master instanceof BlocklyToProcedure && scope.equals("local")
						&& !((BlocklyToProcedure) master).getVariables()
						.contains(name)) { // check if local variable exists
					master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
							"Variable get block is bound to a local variable that does not exist. Remove this block!"));
					return;
				} else if (scope.equals("local") && !(master instanceof BlocklyToProcedure)) {
					master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
							"This editor does not support local variables!"));
					return;
				} else if (scope.equalsIgnoreCase("local")) {
					List<StatementInput> statementInputList = master.getStatementInputsMatching(
							statementInput -> statementInput.disable_local_variables);
					if (!statementInputList.isEmpty()) {
						for (StatementInput statementInput : statementInputList) {
							master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
									"Statement " + statementInput.name + " does not support local variables."));
							break;
						}
						return;
					}
				}

				if (scope.equals("global")) {
					scope = master.getWorkspace().getVariableElementByName(name).getScope().name();
					if (scope.equals("GLOBAL_MAP") || scope.equals("GLOBAL_WORLD")) {
						master.addDependency(new Dependency("world", "world"));
					} else if (scope.equals("PLAYER_LIFETIME") || scope.equals("PLAYER_PERSISTENT")) {
						master.addDependency(new Dependency("entity", "entity"));
					}
				}

				if (master.getTemplateGenerator() != null) {
					Map<String, Object> dataModel = new HashMap<>();
					dataModel.put("name", name);
					dataModel.put("type", type);
					dataModel.put("scope", scope);
					String code = master.getTemplateGenerator()
							.generateFromTemplate("_get_variable.java.ftl", dataModel);
					master.append(code);
				}
			}
		} else {
			master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
					"One of the get variable blocks is improperly defined."));
		}
	}

	@Override public String[] getSupportedBlocks() {
		return new String[] { "variables_get_logic", "variables_get_number", "variables_get_text",
				"variables_get_itemstack" };
	}

	@Override public BlockType getBlockType() {
		return BlockType.OUTPUT;
	}
}
