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

package rip.sayori.rmcr.element.converter;

import rip.sayori.rmcr.element.ModElementType;
import rip.sayori.rmcr.element.converter.fv10.BiomeSpawnListConverter;
import rip.sayori.rmcr.element.converter.fv11.GUICoordinateConverter;
import rip.sayori.rmcr.element.converter.fv11.OverlayCoordinateConverter;
import rip.sayori.rmcr.element.converter.fv12.BiomeDefaultFeaturesConverter;
import rip.sayori.rmcr.element.converter.fv13.ProcedureSpawnGemPickupDelayFixer;
import rip.sayori.rmcr.element.converter.fv14.BlockLuminanceFixer;
import rip.sayori.rmcr.element.converter.fv14.DimensionLuminanceFixer;
import rip.sayori.rmcr.element.converter.fv14.PlantLuminanceFixer;
import rip.sayori.rmcr.element.converter.fv15.DimensionPortalSelectedFixer;
import rip.sayori.rmcr.element.converter.fv16.BlockBoundingBoxFixer;
import rip.sayori.rmcr.element.converter.fv17.GameruleDisplayNameFixer;
import rip.sayori.rmcr.element.converter.fv18.BiomeFrozenTopLayerConverter;
import rip.sayori.rmcr.element.converter.fv4.RecipeTypeConverter;
import rip.sayori.rmcr.element.converter.fv5.AchievementFixer;
import rip.sayori.rmcr.element.converter.fv6.GUIBindingInverter;
import rip.sayori.rmcr.element.converter.fv7.ProcedureEntityDepFixer;
import rip.sayori.rmcr.element.converter.fv8.OpenGUIProcedureDepFixer;
import rip.sayori.rmcr.element.converter.fv9.ProcedureGlobalTriggerFixer;

import java.util.*;

public class ConverterRegistry {

	private static final Map<ModElementType, List<IConverter>> converters = new HashMap<ModElementType, List<IConverter>>() {{
		put(ModElementType.RECIPE, Collections.singletonList(new RecipeTypeConverter()));
		put(ModElementType.ACHIEVEMENT, Collections.singletonList(new AchievementFixer()));
		put(ModElementType.GUI, Arrays.asList(new GUIBindingInverter(), new GUICoordinateConverter()));
		put(ModElementType.PROCEDURE, Arrays.asList(new ProcedureEntityDepFixer(), new OpenGUIProcedureDepFixer(),
				new ProcedureGlobalTriggerFixer(), new ProcedureSpawnGemPickupDelayFixer()));
		put(ModElementType.BIOME, Arrays.asList(new BiomeSpawnListConverter(), new BiomeDefaultFeaturesConverter(),
				new BiomeFrozenTopLayerConverter()));
		put(ModElementType.OVERLAY, Collections.singletonList(new OverlayCoordinateConverter()));
		put(ModElementType.BLOCK, Arrays.asList(new BlockLuminanceFixer(), new BlockBoundingBoxFixer()));
		put(ModElementType.PLANT, Collections.singletonList(new PlantLuminanceFixer()));
		put(ModElementType.GAMERULE, Collections.singletonList(new GameruleDisplayNameFixer()));
		put(ModElementType.DIMENSION, Arrays.asList(new DimensionLuminanceFixer(), new DimensionPortalSelectedFixer()));
	}};

	public static List<IConverter> getConvertersForModElementType(ModElementType modElementType) {
		return converters.get(modElementType);
	}

}
