<#-- @formatter:off -->
(new Object(){
	public int getMaxEnergyStored(World world, BlockPos pos) {
		AtomicInteger _retval = new AtomicInteger(0);
		TileEntity _ent = world.getTileEntity(pos);
		if (_ent != null)
		{
			IEnergyStorage capability = _ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
			if (capability != null)
				_retval.set(capability.getMaxEnergyStored());
		}
		return _retval.get();
	}
}.getMaxEnergyStored(world, new BlockPos((int)${input$x},(int)${input$y},(int)${input$z})))
<#-- @formatter:on -->
