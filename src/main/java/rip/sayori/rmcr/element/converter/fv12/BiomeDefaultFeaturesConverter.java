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

package rip.sayori.rmcr.element.converter.fv12;

import com.google.gson.JsonElement;
import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.element.converter.IConverter;
import rip.sayori.rmcr.element.types.Biome;
import rip.sayori.rmcr.util.StringUtils;
import rip.sayori.rmcr.workspace.Workspace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeDefaultFeaturesConverter implements IConverter {
	private static final Logger LOG = LogManager.getLogger(BiomeDefaultFeaturesConverter.class);

	@Override
	public GeneratableElement convert(Workspace workspace, GeneratableElement input, JsonElement jsonElementInput) {
		Biome biome = (Biome) input;

		try {
			biome.defaultFeatures.add("Caves");
			biome.defaultFeatures.add("MonsterRooms");
			biome.defaultFeatures.add("Ores");

			biome.foliageColor = biome.grassColor;
			biome.waterFogColor = biome.waterColor;

			if (jsonElementInput.getAsJsonObject().get("definition").getAsJsonObject().get("generateLakes") != null) {
				biome.defaultFeatures.add("Lakes");
			}
			biome.name = StringUtils.machineToReadableName(input.getModElement().getName());
		} catch (Exception e) {
			LOG.warn("Could not convert: " + biome.getModElement().getName());
		}

		return biome;
	}

	@Override public int getVersionConvertingTo() {
		return 12;
	}
}
