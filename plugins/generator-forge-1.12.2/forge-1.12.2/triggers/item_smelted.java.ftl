@SubscribeEvent public void onItemSmelted(net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent event){
	Entity entity = event.player;
	World world = entity.world;
	int i=(int)entity.posX;
	int j=(int)entity.posY;
	int k=(int)entity.posZ;
	ItemStack itemStack = event.smelting;
	java.util.HashMap<String, Object> dependencies=new java.util.HashMap<>();
	dependencies.put("x",i);
	dependencies.put("y",j);
	dependencies.put("z",k);
	dependencies.put("world",world);
	dependencies.put("entity",entity);
	dependencies.put("itemstack",itemStack);
	dependencies.put("event",event);
	this.executeProcedure(dependencies);
}