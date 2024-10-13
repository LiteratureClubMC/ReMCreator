	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
                                            if (event != null && event.getEntityPlayer() != null) {
			EntityPlayer entity = event.getEntityPlayer();
                                                ItemStack itemStack = event.getItemStack();
                                                List<String> tooltip = event.getToolTip();
                                                ITooltipFlag flag = event.getFlags();
                                                World world = entity.world;
                                                double i=entity.posX;
	                                double j=entity.posY;
	                                double k=entity.posZ;
			Map<String, Object> dependencies = new HashMap<>();
                                                dependencies.put("x",i);
	                                dependencies.put("y",j);
	                                dependencies.put("z",k);
	                                dependencies.put("world",world);
			dependencies.put("entity",entity);
                                                dependencies.put("itemstack",itemStack);
                                                dependencies.put("flag", flag);
                                                dependencies.put("tooltip",tooltip);
			dependencies.put("event",event);
			this.executeProcedure(dependencies);
                                             }
	}