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
import rip.sayori.rmcr.element.parts.MItemBlock;
import rip.sayori.rmcr.element.parts.Procedure;
import rip.sayori.rmcr.element.parts.TabEntry;
import rip.sayori.rmcr.element.types.interfaces.IItemWithModel;
import rip.sayori.rmcr.element.types.interfaces.IItemWithTexture;
import rip.sayori.rmcr.element.types.interfaces.ITabContainedElement;
import rip.sayori.rmcr.util.image.ImageUtils;
import rip.sayori.rmcr.workspace.elements.ModElement;
import rip.sayori.rmcr.workspace.resources.Model;
import rip.sayori.rmcr.workspace.resources.TexturedModel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused") public class Food extends GeneratableElement
		implements IItemWithModel, ITabContainedElement, IItemWithTexture {

	public int renderType;
	public String texture;
	public String customModelName;

	public String name;
	public String rarity;
	public List<String> specialInfo;
	public TabEntry creativeTab;
	public int stackSize;

	public int nutritionalValue;
	public double saturation;

	public int eatingSpeed;
	public MItemBlock resultItem;
	public boolean forDogs;
	public boolean isAlwaysEdible;
	public String animation;
	public boolean hasGlow;
	public Procedure glowCondition;

	public Procedure onRightClicked;
	public Procedure onRightClickedOnBlock;
	public Procedure onEaten;
	public Procedure onEntityHitWith;
	public Procedure onItemInInventoryTick;
	public Procedure onItemInUseTick;
	public Procedure onCrafted;
	public Procedure onEntitySwing;
	public Procedure onDroppedByPlayer;

	private Food() {
		this(null);
	}

	public Food(ModElement element) {
		super(element);

		this.rarity = "COMMON";
		this.eatingSpeed = 32;
		this.saturation = 0.3f;
		this.animation = "eat";

		this.renderType = 0;
		this.customModelName = "Normal";

		this.specialInfo = new ArrayList<>();
	}

	@Override public Model getItemModel() {
		Model.Type modelType = Model.Type.BUILTIN;
		if (renderType == 1)
			modelType = Model.Type.JSON;
		else if (renderType == 2)
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
		return ImageUtils.resizeAndCrop(getModElement().getFolderManager().getItemImageIcon(texture).getImage(), 32);
	}

	@Override public TabEntry getCreativeTab() {
		return creativeTab;
	}

	@Override public String getTexture() {
		return texture;
	}

}
