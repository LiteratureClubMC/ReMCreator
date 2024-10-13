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

package rip.sayori.rmcr.generator.template.base;

import rip.sayori.rmcr.generator.Generator;
import rip.sayori.rmcr.generator.GeneratorWrapper;
import rip.sayori.rmcr.generator.template.TemplateHelper;
import rip.sayori.rmcr.java.JavaConventions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BaseDataModelProvider {

	private static final Logger TEMPLATE_LOG = LogManager.getLogger("Template Generator LOG");
	private final JavaConventions javaConventions;
	private final GeneratorWrapper generatorWrapper;
	private final Generator generator;
	private final TemplateHelper templateHelper;
	private final FileProvider fileProvider;

	public BaseDataModelProvider(Generator generator) {
		this.javaConventions = new JavaConventions();
		this.generatorWrapper = new GeneratorWrapper(generator);
		this.fileProvider = new FileProvider(generator);

		this.templateHelper = new TemplateHelper();

		this.generator = generator;
	}

	public Map<String, Object> provide() {
		Map<String, Object> retval = new HashMap<>();
		retval.put("generator", generatorWrapper);

		retval.put("w", generator.getWorkspace().getWorkspaceInfo());
		retval.put("modid", generator.getWorkspaceSettings().getModID());
		retval.put("JavaModName", generator.getWorkspaceSettings().getJavaModName());
		retval.put("package", generator.getWorkspaceSettings().getModElementsPackage());
		retval.put("settings", generator.getWorkspaceSettings());
		retval.put("fp", fileProvider);

		retval.put("mcc", generator.getMinecraftCodeProvider());
		retval.put("JavaConventions", javaConventions);
		retval.put("thelper", templateHelper);
		retval.put("Log", TEMPLATE_LOG);
		return retval;
	}

}
