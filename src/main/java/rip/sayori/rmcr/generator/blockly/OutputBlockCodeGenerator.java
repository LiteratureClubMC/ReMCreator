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

package rip.sayori.rmcr.generator.blockly;

import rip.sayori.rmcr.blockly.BlocklyToCode;
import rip.sayori.rmcr.blockly.IBlockGenerator;
import rip.sayori.rmcr.generator.template.TemplateGeneratorException;
import org.w3c.dom.Element;

public class OutputBlockCodeGenerator implements IBlockGenerator {

	private final BlocklyBlockCodeGenerator blocklyBlockCodeGenerator;

	public OutputBlockCodeGenerator(BlocklyBlockCodeGenerator blocklyBlockCodeGenerator) {
		this.blocklyBlockCodeGenerator = blocklyBlockCodeGenerator;
	}

	@Override public void generateBlock(BlocklyToCode master, Element block) throws TemplateGeneratorException {
		blocklyBlockCodeGenerator.generateBlock(master, block);
	}

	@Override public String[] getSupportedBlocks() {
		return blocklyBlockCodeGenerator.getSupportedBlocks(getBlockType());
	}

	@Override public BlockType getBlockType() {
		return BlockType.OUTPUT;
	}
}
