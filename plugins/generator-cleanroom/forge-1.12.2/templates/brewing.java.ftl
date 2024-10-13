<#-- @formatter:off -->
<#include "mcitems.ftl">

package ${package}.item.crafting;

import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

@Elements${JavaModName}.ModElement.Tag public class BrewingRecipe${name} extends Elements${JavaModName}.ModElement{

	public BrewingRecipe${name} (Elements${JavaModName} instance) {
		super(instance, ${data.getModElement().getSortID()});
	}

	@Override public void init(FMLInitializationEvent event) {
		BrewingRecipeRegistry.addRecipe(${mappedMCItemToItemStackCode(data.brewingInputStack,1)}
        	                                    ,${mappedMCItemToItemStackCode(data.brewingIngredientStack,1)}
        	                                    ,${mappedMCItemToItemStackCode(data.brewingReturnStack,1)}
        	                                    );
	}

}
<#-- @formatter:on -->