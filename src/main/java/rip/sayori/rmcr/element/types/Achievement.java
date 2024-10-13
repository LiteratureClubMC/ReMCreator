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

package rip.sayori.rmcr.element.types;

import rip.sayori.rmcr.blockly.data.BlocklyLoader;
import rip.sayori.rmcr.blockly.datapack.BlocklyToJSONTrigger;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.element.parts.AchievementEntry;
import rip.sayori.rmcr.element.parts.MItemBlock;
import rip.sayori.rmcr.generator.blockly.BlocklyBlockCodeGenerator;
import rip.sayori.rmcr.generator.blockly.ProceduralBlockCodeGenerator;
import rip.sayori.rmcr.generator.template.IAdditionalTemplateDataProvider;
import rip.sayori.rmcr.minecraft.MinecraftImageGenerator;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.List;

@SuppressWarnings("unused") public class Achievement extends GeneratableElement {

	public String achievementName;
	public String achievementDescription;

	public MItemBlock achievementIcon;

	public String background;

	public boolean disableDisplay;

	public boolean showPopup;
	public boolean announceToChat;
	public boolean hideIfNotCompleted;

	public List<String> rewardLoot;
	public List<String> rewardRecipes;
	public String rewardFunction;
	public int rewardXP;

	public String achievementType;
	public AchievementEntry parent;

	public String triggerxml;

	public Achievement(ModElement element) {
		super(element);
	}

	@Override public BufferedImage generateModElementPicture() {
		return MinecraftImageGenerator.Preview.generateAchievementPreviewPicture(getModElement().getWorkspace(),
				achievementIcon, achievementName);
	}

	@Override public @Nullable IAdditionalTemplateDataProvider getAdditionalTemplateData() {
		return additionalData -> {
			BlocklyBlockCodeGenerator blocklyBlockCodeGenerator = new BlocklyBlockCodeGenerator(
					BlocklyLoader.INSTANCE.getJSONTriggerLoader().getDefinedBlocks(),
					this.getModElement().getGenerator().getJSONTriggerGenerator(), additionalData).setTemplateExtension(
					"json");

			// load blocklytojava with custom generators loaded
			BlocklyToJSONTrigger blocklyToJSONTrigger = new BlocklyToJSONTrigger(this.getModElement().getWorkspace(),
					this.triggerxml, this.getModElement().getGenerator().getJSONTriggerGenerator(),
					new ProceduralBlockCodeGenerator(blocklyBlockCodeGenerator));

			String triggerCode = blocklyToJSONTrigger.getGeneratedCode();
			if (triggerCode.equals(""))
				triggerCode = "{\"trigger\": \"minecraft:impossible\"}";

			additionalData.put("triggercode", triggerCode);
		};
	}

}