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
import rip.sayori.rmcr.element.parts.Sound;
import rip.sayori.rmcr.element.parts.TabEntry;
import rip.sayori.rmcr.element.types.interfaces.IEntityWithModel;
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

@SuppressWarnings("unused") public class RangedItem extends GeneratableElement
		implements IItemWithModel, IEntityWithModel, ITabContainedElement, IItemWithTexture {

	public int renderType;
	public String texture;
	public String customModelName;
	public String name;
	public List<String> specialInfo;
	public TabEntry creativeTab;
	public int stackSize;
	public MItemBlock ammoItem;
	public boolean shootConstantly;
	public int usageCount;
	public Sound actionSound;
	public boolean hasGlow;
	public Procedure glowCondition;
	public String animation;
	public boolean enableMeleeDamage;
	public double damageVsEntity;
	public Procedure onRangedItemUsed;
	public Procedure onEntitySwing;
	public Procedure useCondition;

	public double bulletPower;
	public double bulletDamage;
	public int bulletKnockback;
	public boolean bulletParticles;
	public boolean bulletIgnitesFire;
	public MItemBlock bulletItemTexture;
	public String bulletModel;
	public String customBulletModelTexture;

	public Procedure onBulletHitsBlock;
	public Procedure onBulletHitsPlayer;
	public Procedure onBulletHitsEntity;
	public Procedure onBulletFlyingTick;

	private RangedItem() {
		this(null);
	}

	public RangedItem(ModElement element) {
		super(element);

		this.stackSize = 1;
		this.animation = "bow";

		this.specialInfo = new ArrayList<>();
	}

	@Override public BufferedImage generateModElementPicture() {
		return ImageUtils.resizeAndCrop(getModElement().getFolderManager().getItemImageIcon(texture).getImage(), 32);
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

	@Override public Model getEntityModel() {
		Model.Type modelType = Model.Type.BUILTIN;
		if (!bulletModel.equals("Default"))
			modelType = Model.Type.JAVA;
		return Model.getModelByParams(getModElement().getWorkspace(), bulletModel, modelType);
	}

	@Override public TabEntry getCreativeTab() {
		return creativeTab;
	}

	@Override public String getTexture() {
		return texture;
	}
}
