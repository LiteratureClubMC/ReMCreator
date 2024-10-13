<#-- @formatter:off -->
package ${package}.world;

import ${package}.gamerules;

@Elements${JavaModName}.ModElement.Tag
public class ${name}GameRule extends Elements${JavaModName}.ModElement {

	public static final String gamerule = "${registryname}";

	public ${name}GameRule (Elements${JavaModName} instance) {
		super(instance, ${data.getModElement().getSortID()});
	}

	<#if data.type == "Number">
		public void initElements(int defaultValue) {
		    MinecraftForge.EVENT_BUS.register(new Object(){
		        @SubscribeEvent
                public void onEventFired(WorldEvent.Load event) {
                    event.getWorld().getGameRules().addGameRule(gamerule,"${data.defaultValueNumber}", net.minecraft.world.GameRules.ValueType.NUMERICAL_VALUE);
                }
		    });
		}
	<#else>
		public void initElements(boolean defaultValue) {
		    MinecraftForge.EVENT_BUS.register(new Object(){
                @SubscribeEvent
                public void onEventFired(WorldEvent.Load event) {
                    event.getWorld().getGameRules().addGameRule(gamerule,"${data.defaultValueLogic}", net.minecraft.world.GameRules.ValueType.BOOLEAN_VALUE);
                }
            });
		}
	</#if>
}
<#-- @formatter:on -->