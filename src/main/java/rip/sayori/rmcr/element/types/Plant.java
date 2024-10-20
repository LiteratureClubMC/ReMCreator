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

import rip.sayori.rmcr.element.GeneratableElement;
import rip.sayori.rmcr.element.parts.*;
import rip.sayori.rmcr.element.parts.Procedure;
import rip.sayori.rmcr.element.types.interfaces.IBlockWithBoundingBox;
import rip.sayori.rmcr.element.types.interfaces.IItemWithModel;
import rip.sayori.rmcr.element.types.interfaces.ITabContainedElement;
import rip.sayori.rmcr.util.image.ImageUtils;
import rip.sayori.rmcr.workspace.elements.ModElement;
import rip.sayori.rmcr.workspace.resources.Model;
import rip.sayori.rmcr.workspace.resources.TexturedModel;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused") public class Plant extends GeneratableElement
		implements IItemWithModel, ITabContainedElement, IBlockWithBoundingBox {

	public int renderType;
	public String texture;
	public String textureBottom;
	public String customModelName;

	public String itemTexture;
	public String particleTexture;

	public String tintType;
	public boolean isItemTinted;

	public String plantType;

	public String staticPlantGenerationType;

	public String growapableSpawnType;
	public int growapableMaxHeight;

	public String doublePlantGenerationType;

	public boolean customBoundingBox;
	public boolean disableOffset;
	public List<BoxEntry> boundingBoxes;

	public String name;
	public List<String> specialInfo;
	public TabEntry creativeTab;
	public double hardness;
	public double resistance;
	public int luminance;
	public boolean unbreakable;
	public StepSound soundOnStep;
	public boolean useLootTableForDrops;
	public MItemBlock customDrop;
	public int dropAmount;
	public boolean forceTicking;
	public boolean emissiveRendering;

	public boolean hasTileEntity;

	public boolean isReplaceable;
	public String colorOnMap;
	public MItemBlock creativePickItem;
	public String offsetType;
	public String aiPathNodeType;

	public int flammability;
	public int fireSpreadSpeed;
	public double jumpFactor;
	public double speedFactor;

	public List<MItemBlock> canBePlacedOn;
	public Procedure placingCondition;

	public int frequencyOnChunks;
	public List<String> spawnWorldTypes;
	public List<BiomeEntry> restrictionBiomes;
	public Procedure generateCondition;

	public Procedure onBlockAdded;
	public Procedure onNeighbourBlockChanges;
	public Procedure onTickUpdate;
	public Procedure onRandomUpdateEvent;
	public Procedure onDestroyedByPlayer;
	public Procedure onDestroyedByExplosion;
	public Procedure onStartToDestroy;
	public Procedure onEntityCollides;
	public Procedure onBlockPlacedBy;
	public Procedure onRightClicked;

	private Plant() {
		this(null);
	}

	public Plant(ModElement element) {
		super(element);

		this.canBePlacedOn = new ArrayList<>();
		this.spawnWorldTypes = new ArrayList<>();
		this.spawnWorldTypes.add("Surface");
		this.restrictionBiomes = new ArrayList<>();
		this.growapableSpawnType = "Plains";
		this.renderType = 12;
		this.customModelName = "Cross model";
		this.colorOnMap = "DEFAULT";
		this.aiPathNodeType = "DEFAULT";
		this.offsetType = "XZ";
		this.tintType = "No tint";

		this.jumpFactor = 1.0;
		this.speedFactor = 1.0;

		this.staticPlantGenerationType = "Flower";
		this.doublePlantGenerationType = "Flower";

		this.specialInfo = new ArrayList<>();
		this.boundingBoxes = new ArrayList<>();
	}

	@Override public Model getItemModel() {
		Model.Type modelType = Model.Type.BUILTIN;
		if (renderType == 2)
			modelType = Model.Type.JSON;
		else if (renderType == 3)
			modelType = Model.Type.OBJ;
		return Model.getModelByParams(getModElement().getWorkspace(), customModelName, modelType);
	}

	@Override public Map<String, String> getTextureMap() {
		Model model = getItemModel();
		if (model instanceof TexturedModel && ((TexturedModel) model).getTextureMapping() != null)
			return ((TexturedModel) model).getTextureMapping().getTextureMap();
		return null;
	}

	@Override public BufferedImage generateModElementPicture() {
		return ImageUtils.resizeAndCrop(getModElement().getFolderManager().getBlockImageIcon(texture).getImage(), 32);
	}

	@Override public TabEntry getCreativeTab() {
		return creativeTab;
	}

	public boolean isBlockTinted() {
		return !"No tint".equals(tintType);
	}

	@Override public @NotNull List<BoxEntry> getValidBoundingBoxes() {
		return boundingBoxes.stream().filter(BoxEntry::isNotEmpty).collect(Collectors.toList());
	}
}
