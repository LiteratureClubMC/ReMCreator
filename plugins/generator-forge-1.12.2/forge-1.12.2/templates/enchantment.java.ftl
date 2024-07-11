<#-- @formatter:off -->
<#include "mcitems.ftl">
<#include "procedures.java.ftl">

package ${package}.enchantment;


@Elements${JavaModName}.ModElement.Tag public class ${name}Enchantment extends Elements${JavaModName}.ModElement{

	public static Enchantment enchantment = null;

	public ${name}Enchantment(Elements${JavaModName} instance) {
		super(instance, ${data.getModElement().getSortID()});
	}

	@Override public void initElements() {
		elements.enchantments.add(() -> (enchantment = new CustomEnchantment(EntityEquipmentSlot.MAINHAND)
		                                            .setRegistryName("${modid}:${registryname}")
		                                            .setName("${modid}.${registryname}")
		                                            ));
	}

	public static class CustomEnchantment extends Enchantment {

		public CustomEnchantment(EntityEquipmentSlot... slots) {
			super(Enchantment.Rarity.${data.rarity}, EnumEnchantmentType.${generator.map(data.type, "enchantmenttypes")}, slots);
        }

		@Override public int getMinLevel() {
			return ${data.minLevel};
		}

		@Override public int getMaxLevel() {
			return ${data.maxLevel};
		}

		<#if data.damageModifier != 0>
		@Override public int calcModifierDamage(int level, DamageSource source) {
			return level * ${data.damageModifier};
		}
		</#if>

        <#if data.compatibleEnchantments?has_content>
		@Override protected boolean canApplyTogether(Enchantment ench) {
			<#list data.compatibleEnchantments as compatibleEnchantment>
			    if(ench == ${compatibleEnchantment})
			    	return true;
            </#list>
			return false;
		}
        </#if>

        <#if data.compatibleItems?has_content>
		@Override public boolean canApplyAtEnchantingTable(ItemStack stack) {
            <#list data.compatibleItems as compatibleItem>
			    if(stack.getItem() == ${mappedMCItemToItem(compatibleItem)})
					return true;
            </#list>
			return false;
		}
        </#if>

		@Override public boolean isTreasureEnchantment() {
			return ${data.isTreasureEnchantment};
		}

		@Override public boolean isCurse() {
			return ${data.isCurse};
		}

		@Override public boolean isAllowedOnBooks() {
			return ${data.isAllowedOnBooks};
		}

	}

}
<#-- @formatter:on -->