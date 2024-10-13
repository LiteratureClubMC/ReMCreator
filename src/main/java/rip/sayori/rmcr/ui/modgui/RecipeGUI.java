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

package rip.sayori.rmcr.ui.modgui;

import rip.sayori.rmcr.element.parts.MItemBlock;
import rip.sayori.rmcr.element.types.Recipe;
import rip.sayori.rmcr.minecraft.ElementUtil;
import rip.sayori.rmcr.minecraft.RegistryNameFixer;
import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.MCreatorApplication;
import rip.sayori.rmcr.ui.component.util.ComponentUtils;
import rip.sayori.rmcr.ui.component.util.PanelUtils;
import rip.sayori.rmcr.ui.datapack.recipe.BrewingRecipeMaker;
import rip.sayori.rmcr.ui.datapack.recipe.CraftingReciepeMaker;
import rip.sayori.rmcr.ui.datapack.recipe.SmeltingRecipeMaker;
import rip.sayori.rmcr.ui.help.HelpUtils;
import rip.sayori.rmcr.ui.init.L10N;
import rip.sayori.rmcr.ui.validation.AggregatedValidationResult;
import rip.sayori.rmcr.ui.validation.component.VComboBox;
import rip.sayori.rmcr.ui.validation.component.VTextField;
import rip.sayori.rmcr.ui.validation.validators.RegistryNameValidator;
import rip.sayori.rmcr.workspace.elements.ModElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class RecipeGUI extends ModElementGUI<Recipe> {

	private final JCheckBox recipeShapeless = L10N.checkbox("elementgui.recipe.is_shapeless");
	private final JSpinner xpReward = new JSpinner(new SpinnerNumberModel(1.0, 0, 256, 1));
	private final JSpinner cookingTime = new JSpinner(new SpinnerNumberModel(200, 0, 1000000, 1));
	private final JComboBox<String> namespace = new JComboBox<>(new String[] { "mod", "minecraft" });
	private final VComboBox<String> name = new VComboBox<>();
	private final VTextField group = new VTextField();
	private final JComboBox<String> recipeType = new JComboBox<>(new String[] { "Crafting", "Smelting", "Brewing" });
	private CraftingReciepeMaker rm;
	private SmeltingRecipeMaker fm;
	private BrewingRecipeMaker brm;

	public RecipeGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
		super(mcreator, modElement, editingMode);
		this.initGUI();
		super.finalizeGUI();
	}

	@Override protected void initGUI() {
		rm = new CraftingReciepeMaker(mcreator, ElementUtil::loadBlocksAndItemsAndTags,
				ElementUtil::loadBlocksAndItems);
		fm = new SmeltingRecipeMaker(mcreator, ElementUtil::loadBlocksAndItemsAndTags, ElementUtil::loadBlocksAndItems);
		brm = new BrewingRecipeMaker(mcreator, ElementUtil::loadBlocksAndItemsAndTags, ElementUtil::loadBlocksAndItems);

		rm.setOpaque(false);
		fm.setOpaque(false);

		brm.setOpaque(false);

		name.setValidator(new RegistryNameValidator(name, "Loot table").setValidChars(Arrays.asList('_', '/')));
		name.enableRealtimeValidation();

		name.addItem("crafting_table");
		name.addItem("diamond_block");

		name.setEditable(true);
		name.setOpaque(false);

		ComponentUtils.deriveFont(group, 16);

		if (isEditingMode()) {
			name.setEnabled(false);
			namespace.setEnabled(false);
		} else {
			name.getEditor().setItem(RegistryNameFixer.fromCamelCase(modElement.getName()));
		}

		JPanel pane5 = new JPanel(new BorderLayout(10, 10));

		CardLayout recipesPanelLayout = new CardLayout();
		JPanel recipesPanel = new JPanel(recipesPanelLayout);
		recipesPanel.setOpaque(false);

		JPanel crafting = new JPanel(new BorderLayout());
		crafting.setOpaque(false);

		crafting.add("West", rm);
		crafting.add("North", PanelUtils.join(FlowLayout.LEFT,
				HelpUtils.wrapWithHelpButton(this.withEntry("recipe/shapeless"), recipeShapeless)));

		recipeShapeless.setOpaque(false);
		recipeShapeless.addActionListener(event -> rm.setShapeless(recipeShapeless.isSelected()));

		recipesPanel.add(crafting, "crafting");
		recipesPanel.add(PanelUtils.totalCenterInPanel(fm), "smelting");
		recipesPanel.add(PanelUtils.totalCenterInPanel(brm), "brewing");

		JComponent recwrap = PanelUtils.maxMargin(recipesPanel, 10, true, true, true, true);
		recwrap.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
				"Recipe parameters", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, getFont(), Color.white));

		JPanel northPanel = new JPanel(new GridLayout(6, 2, 10, 2));
		northPanel.setOpaque(false);

		northPanel.add(
				HelpUtils.wrapWithHelpButton(this.withEntry("recipe/type"), L10N.label("elementgui.recipe.type")));
		northPanel.add(recipeType);

		northPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("recipe/registry_name"),
				L10N.label("elementgui.recipe.registry_name")));
		northPanel.add(name);

		northPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("recipe/namespace"),
				L10N.label("elementgui.recipe.name_space")));
		northPanel.add(namespace);

		northPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("recipe/group_name"),
				L10N.label("elementgui.recipe.group")));
		northPanel.add(group);

		northPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("recipe/xp_reward"),
				L10N.label("elementgui.recipe.xp_reward")));
		northPanel.add(xpReward);

		northPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("recipe/cooking_time"),
				L10N.label("elementgui.recipe.cooking_time")));
		northPanel.add(cookingTime);

		pane5.setOpaque(false);
		pane5.add(PanelUtils.totalCenterInPanel(
				PanelUtils.westAndEastElement(PanelUtils.join(FlowLayout.LEFT, northPanel), recwrap, 15, 15)));

		xpReward.setEnabled(false);
		cookingTime.setEnabled(false);

		recipeType.addActionListener(e -> {
			if (recipeType.getSelectedItem() != null) {
				xpReward.setEnabled(!recipeType.getSelectedItem().equals("Crafting") && !recipeType.getSelectedItem()
						.equals("Stone cutting") && !recipeType.getSelectedItem().equals("Smithing")
						&& !recipeType.getSelectedItem().equals("Brewing"));
				cookingTime.setEnabled(!recipeType.getSelectedItem().equals("Crafting") && !recipeType.getSelectedItem()
						.equals("Stone cutting") && !recipeType.getSelectedItem().equals("Smithing")
						&& !recipeType.getSelectedItem().equals("Brewing"));

				group.setEnabled(!recipeType.getSelectedItem().equals("Brewing"));

				if (!isEditingMode() && cookingTime.isEnabled()) {
					if (Objects.equals(recipeType.getSelectedItem(), "Smelting")) {
						cookingTime.setValue(200);
					} else {
						cookingTime.setValue(100);
					}
				}

				recipesPanelLayout.show(recipesPanel,
						Objects.requireNonNull(recipeType.getSelectedItem()).toString().toLowerCase(Locale.ENGLISH));
			}
		});

		group.enableRealtimeValidation();
		group.setValidator(new RegistryNameValidator(group, "Recipe group").setAllowEmpty(true).setMaxLength(128));

		addPage(pane5);
	}

	@Override protected AggregatedValidationResult validatePage(int page) {
		if ("Crafting".equals(recipeType.getSelectedItem())) {
			if (!rm.cb10.containsItem()) {
				return new AggregatedValidationResult.FAIL(L10N.t("elementgui.recipe.error_crafting_no_result"));
			} else if (!(rm.cb1.containsItem() || rm.cb2.containsItem() || rm.cb3.containsItem()
					|| rm.cb4.containsItem() || rm.cb5.containsItem() || rm.cb6.containsItem() || rm.cb7.containsItem()
					|| rm.cb8.containsItem() || rm.cb9.containsItem())) {
				return new AggregatedValidationResult.FAIL(L10N.t("elementgui.recipe.error_crafting_no_ingredient"));
			}
		} else if ("Smelting".equals(recipeType.getSelectedItem())) {
			if (!fm.cb1.containsItem() || !fm.cb2.containsItem()) {
				return new AggregatedValidationResult.FAIL(
						L10N.t("elementgui.recipe.error_smelting_no_ingredient_and_result"));
			}
		} else if ("Brewing".equals(recipeType.getSelectedItem())) {
			if (!brm.cb1.containsItem() || !brm.cb2.containsItem() || !brm.cb3.containsItem()) {
				return new AggregatedValidationResult.FAIL(
						L10N.t("elementgui.recipe.error_brewing_no_input_ingredient_and_result"));
			}
		}

		return new AggregatedValidationResult(name, group);
	}

	@Override public void openInEditingMode(Recipe recipe) {
		recipeType.setSelectedItem(recipe.recipeType);

		namespace.setSelectedItem(recipe.namespace);
		name.getEditor().setItem(recipe.name);

		group.setText(recipe.group);

		if ("Crafting".equals(recipe.recipeType)) {
			recipeShapeless.setSelected(recipe.recipeShapeless);
			rm.cb1.setBlock(recipe.recipeSlots[0]);
			rm.cb2.setBlock(recipe.recipeSlots[3]);
			rm.cb3.setBlock(recipe.recipeSlots[6]);
			rm.cb4.setBlock(recipe.recipeSlots[1]);
			rm.cb5.setBlock(recipe.recipeSlots[4]);
			rm.cb6.setBlock(recipe.recipeSlots[7]);
			rm.cb7.setBlock(recipe.recipeSlots[2]);
			rm.cb8.setBlock(recipe.recipeSlots[5]);
			rm.cb9.setBlock(recipe.recipeSlots[8]);
			rm.cb10.setBlock(recipe.recipeReturnStack);
			rm.sp.setValue(recipe.recipeRetstackSize);
			rm.setShapeless(recipeShapeless.isSelected());
		} else if ("Smelting".equals(recipe.recipeType)) {
			fm.cb1.setBlock(recipe.smeltingInputStack);
			fm.cb2.setBlock(recipe.smeltingReturnStack);
			xpReward.setValue(recipe.xpReward);
			cookingTime.setValue(recipe.cookingTime);
		} else if ("Brewing".equals(recipe.recipeType)) {
			brm.cb1.setBlock(recipe.brewingInputStack);
			brm.cb2.setBlock(recipe.brewingIngredientStack);
			brm.cb3.setBlock(recipe.brewingReturnStack);
		}
	}

	@Override public Recipe getElementFromGUI() {
		Recipe recipe = new Recipe(modElement);
		recipe.recipeType = (String) recipeType.getSelectedItem();

		if ("Crafting".equals(recipe.recipeType)) {
			MItemBlock[] recipeSlots = new MItemBlock[9];
			recipeSlots[0] = rm.cb1.getBlock();
			recipeSlots[3] = rm.cb2.getBlock();
			recipeSlots[6] = rm.cb3.getBlock();
			recipeSlots[1] = rm.cb4.getBlock();
			recipeSlots[4] = rm.cb5.getBlock();
			recipeSlots[7] = rm.cb6.getBlock();
			recipeSlots[2] = rm.cb7.getBlock();
			recipeSlots[5] = rm.cb8.getBlock();
			recipeSlots[8] = rm.cb9.getBlock();
			recipe.recipeRetstackSize = (int) rm.sp.getValue();
			recipe.recipeShapeless = recipeShapeless.isSelected();
			recipe.recipeReturnStack = rm.cb10.getBlock();
			recipe.recipeSlots = recipeSlots;
		} else if ("Smelting".equals(recipe.recipeType)) {
			recipe.smeltingInputStack = fm.getBlock();
			recipe.smeltingReturnStack = fm.getBlock2();
			recipe.xpReward = (double) xpReward.getValue();
			recipe.cookingTime = (int) cookingTime.getValue();
		} else if ("Brewing".equals(recipe.recipeType)) {
			recipe.brewingInputStack = brm.cb1.getBlock();
			recipe.brewingIngredientStack = brm.cb2.getBlock();
			recipe.brewingReturnStack = brm.cb3.getBlock();
		}

		recipe.namespace = (String) namespace.getSelectedItem();
		recipe.name = name.getEditor().getItem().toString();

		recipe.group = group.getText();

		return recipe;
	}

	@Override public @Nullable URI getContextURL() throws URISyntaxException {
		return new URI(MCreatorApplication.SERVER_DOMAIN + "/wiki/how-make-recipe");
	}

}
