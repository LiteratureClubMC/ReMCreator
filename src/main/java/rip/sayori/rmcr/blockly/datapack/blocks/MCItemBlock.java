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

package rip.sayori.rmcr.blockly.datapack.blocks;

import rip.sayori.rmcr.blockly.BlocklyCompileNote;
import rip.sayori.rmcr.blockly.BlocklyToCode;
import rip.sayori.rmcr.blockly.IBlockGenerator;
import rip.sayori.rmcr.element.parts.MItemBlock;
import rip.sayori.rmcr.generator.template.TemplateGeneratorException;
import rip.sayori.rmcr.util.XMLUtil;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class MCItemBlock implements IBlockGenerator {

	@Override public void generateBlock(BlocklyToCode master, Element block) throws TemplateGeneratorException {
		Element element = XMLUtil.getFirstChildrenWithName(block, "field");
		if (element != null && element.getTextContent() != null && !element.getTextContent().equals("")
				&& !element.getTextContent().equals("null")) {
			if (master.getTemplateGenerator() != null) {
				Map<String, Object> dataModel = new HashMap<>();
				dataModel.put("block", new MItemBlock(master.getWorkspace(), element.getTextContent()));
				String code = master.getTemplateGenerator().generateFromTemplate("_mcitemblock.json.ftl", dataModel);
				master.append(code);
			}
		} else {
			master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
					"Empty Minecraft element block. You need to define the element."));
		}
	}

	@Override public String[] getSupportedBlocks() {
		return new String[] { "mcitem_all", "mcitem_allblocks" };
	}

	@Override public BlockType getBlockType() {
		return BlockType.OUTPUT;
	}
}
