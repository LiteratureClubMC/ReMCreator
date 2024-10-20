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

import java.util.List;

public class TextJoinBlock implements IBlockGenerator {

	@Override public void generateBlock(BlocklyToCode master, Element block) throws TemplateGeneratorException {
		Element mutation = XMLUtil.getFirstChildrenWithName(block, "mutation");
		if (mutation != null) {
			int sumnum = Integer.parseInt(mutation.getAttribute("items"));
			List<Element> elements = XMLUtil.getChildrenWithName(block, "value");

			if (sumnum == 0) {
				master.addCompileNote(new BlocklyCompileNote(BlocklyCompileNote.Type.ERROR,
						"Join as text block needs at least one element"));
				return;
			}

			if (sumnum == 1) {
				master.append("(\"\" + ");
			} else {
				master.append("(");
			}

			for (int i = 0; i < sumnum; i++) {
				Element element = null;
				for (Element candidate : elements) {
					if (candidate.getAttribute("name").equals("ADD" + i))
						element = candidate;
				}
				master.append("(");
				if (element != null)
					master.processOutputBlock(element);
				else {
					master.addCompileNote(
							new BlocklyCompileNote(BlocklyCompileNote.Type.WARNING, "Text join elements is empty."));
					master.append("null");
				}
				master.append(")");
				if (i < sumnum - 1)
					master.append("+\"\"+");
			}
			master.append(")");
		} else {
			master.addCompileNote(
					new BlocklyCompileNote(BlocklyCompileNote.Type.WARNING, "Skipping invalid text join."));
		}
	}

	@Override public String[] getSupportedBlocks() {
		return new String[] { "text_join" };
	}

	@Override public BlockType getBlockType() {
		return BlockType.OUTPUT;
	}
}
