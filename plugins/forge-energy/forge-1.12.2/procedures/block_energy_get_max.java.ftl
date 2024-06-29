<#-- @formatter:off -->
(new Object(){
	public int getMaxEnergyStored(World world, BlockPos pos) {
		AtomicInteger _retval = new AtomicInteger(0);
		TileEntity _ent = world.getTileEntity(pos);
		if (_ent != null && _ent.hasCapability(CapabilityEnergy.ENERGY, ${input$direction})) {
			_retval.set(_ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction}).getMaxEnergyStored());
		}
		return _retval.get();
	}
}.getMaxEnergyStored(world, new BlockPos((int)${input$x},(int)${input$y},(int)${input$z})))
<#-- @formatter:on -->
