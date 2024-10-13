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
import rip.sayori.rmcr.generator.template.TemplateGeneratorException;
import rip.sayori.rmcr.util.XMLUtil;
import org.w3c.dom.Element;

public class LogicNegateBlock implements IBlockGenerator {

	@Override public void generateBlock(BlocklyToCode master, Element block) throws TemplateGeneratorException {
		Element negated_output_block = XMLUtil.getFirstChildrenWithName(block, "value");
		master.append("(");
		if (negated_output_block != null) {
			master.append("!");
			master.processOutputBlock(negated_output_block);
		} else {
			master.append("false");
			master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.WARNING,
					"Found not block without input. Its value will always be false."));
		}
		master.append(")");
	}

	@Override public String[] getSupportedBlocks() {
		return new String[] { "logic_negate" };
	}

	@Override public BlockType getBlockType() {
		return BlockType.OUTPUT;
	}
}
