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
import rip.sayori.rmcr.blockly.data.Dependency;
import rip.sayori.rmcr.blockly.data.ExternalTrigger;
import rip.sayori.rmcr.blockly.java.BlocklyToProcedure;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.generator.blockly.BlocklyBlockCodeGenerator;
import rip.sayori.rmcr.generator.blockly.OutputBlockCodeGenerator;
import rip.sayori.rmcr.generator.blockly.ProceduralBlockCodeGenerator;
import rip.sayori.rmcr.generator.template.IAdditionalTemplateDataProvider;
import rip.sayori.rmcr.generator.template.TemplateGenerator;
import rip.sayori.rmcr.minecraft.MinecraftImageGenerator;
import rip.sayori.rmcr.workspace.WorkspaceFileManager;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Procedure extends GeneratableElement {

	public String procedurexml;

	private transient List<Dependency> dependencies = null;

	public Procedure(ModElement element) {
		super(element);
	}

	public List<Dependency> getDependencies() {
		if (dependencies == null)
			reloadDependencies();

		return dependencies;
	}

	public void reloadDependencies() {
		dependencies = new ArrayList<>();
		List<?> dependenciesList = (List<?>) getModElement().getMetadata("dependencies");
		for (Object depobj : dependenciesList) {
			Dependency dependency = WorkspaceFileManager.gson.fromJson(
					WorkspaceFileManager.gson.toJsonTree(depobj).getAsJsonObject(), Dependency.class);
			dependencies.add(dependency);
		}
	}

	@Override public BufferedImage generateModElementPicture() {
		return MinecraftImageGenerator.Preview.generateProcedurePreviewPicture(procedurexml, getDependencies());
	}

	@Override public @Nullable IAdditionalTemplateDataProvider getAdditionalTemplateData() {
		return additionalData -> {
			BlocklyBlockCodeGenerator blocklyBlockCodeGenerator = new BlocklyBlockCodeGenerator(
					BlocklyLoader.INSTANCE.getProcedureBlockLoader().getDefinedBlocks(),
					getModElement().getGenerator().getProcedureGenerator(), additionalData);

			// load blocklytojava with custom generators loaded
			BlocklyToProcedure blocklyToJava = new BlocklyToProcedure(this.getModElement().getWorkspace(),
					this.procedurexml, getModElement().getGenerator().getProcedureGenerator(),
					new ProceduralBlockCodeGenerator(blocklyBlockCodeGenerator),
					new OutputBlockCodeGenerator(blocklyBlockCodeGenerator));

			List<ExternalTrigger> externalTriggers = BlocklyLoader.INSTANCE.getExternalTriggerLoader()
					.getExternalTrigers();
			ExternalTrigger trigger = null;
			for (ExternalTrigger externalTrigger : externalTriggers) {
				if (externalTrigger.getID().equals(blocklyToJava.getExternalTrigger()))
					trigger = externalTrigger;
			}

			// we update the dependency list of the procedure
			List<Dependency> dependenciesArrayList = blocklyToJava.getDependencies();

			this.getModElement().clearMetadata().putMetadata("dependencies", dependenciesArrayList)
					.putMetadata("return_type",
							blocklyToJava.getReturnType() == null ? null : blocklyToJava.getReturnType().name());

			reloadDependencies();

			String triggerCode = "";
			if (trigger != null) {
				TemplateGenerator templateGenerator = getModElement().getGenerator().getTriggerGenerator();
				triggerCode = templateGenerator.generateFromTemplate(trigger.getID() + ".java.ftl", additionalData);
			}

			additionalData.put("procedurecode", blocklyToJava.getGeneratedCode());
			additionalData.put("return_type", blocklyToJava.getReturnType());
			additionalData.put("has_trigger", trigger != null);
			additionalData.put("trigger_code", triggerCode);
			additionalData.put("dependencies", dependenciesArrayList);
		};
	}

}
