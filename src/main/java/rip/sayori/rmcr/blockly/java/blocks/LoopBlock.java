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

import rip.sayori.rmcr.blockly.BlocklyBlockUtil;
import rip.sayori.rmcr.blockly.BlocklyCompileNote;
import rip.sayori.rmcr.blockly.BlocklyToCode;
import rip.sayori.rmcr.blockly.IBlockGenerator;
import rip.sayori.rmcr.generator.template.TemplateGeneratorException;
import rip.sayori.rmcr.util.XMLUtil;
import org.w3c.dom.Element;

public class LoopBlock implements IBlockGenerator {

	private int loopIndex = 0;

	@Override public void generateBlock(BlocklyToCode master, Element block) throws TemplateGeneratorException {
		Element value = XMLUtil.getFirstChildrenWithName(block, "value");
		Element statement = XMLUtil.getFirstChildrenWithName(block, "statement");

		String blocktype = block.getAttribute("type");

		if (value != null && statement != null) {
			int index = loopIndex++;

			if ("controls_while".equals(blocktype))
				master.append("while(");
			else if ("controls_repeat_ext".equals(blocktype))
				master.append("for (int index").append(index).append(" = 0; index").append(index).append("<(int)(");

			master.processOutputBlock(value);

			if ("controls_while".equals(blocktype))
				master.append(") {");
			else if ("controls_repeat_ext".equals(blocktype))
				master.append("); index").append(index).append("++) {");

			master.processBlockProcedure(BlocklyBlockUtil.getBlockProcedureStartingWithBlock(statement));
			master.append("}");
		} else {
			master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.WARNING,
					"Loop block has either empty condition or body. Skipping this block."));
		}
	}

	@Override public String[] getSupportedBlocks() {
		return new String[] { "controls_repeat_ext", "controls_while" };
	}

	@Override public BlockType getBlockType() {
		return BlockType.PROCEDURAL;
	}
}
