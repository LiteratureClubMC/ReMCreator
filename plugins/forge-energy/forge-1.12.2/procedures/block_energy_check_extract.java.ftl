<#-- @formatter:off -->
(new Object(){
	public int extractEnergySimulate(World world, BlockPos pos, int _amount) {
		AtomicInteger _retval = new AtomicInteger(0);
		TileEntity _ent = world.getTileEntity(pos);
		if (_ent != null)
		{
			IEnergyStorage capability = _ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
			if (capability != null)
				_retval.set(capability.extractEnergy(_amount, true));
		}
		return _retval.get();
	}
}.extractEnergySimulate(world, new BlockPos((int)${input$x},(int)${input$y},(int)${input$z}),(int)${input$amount}))
<#-- @formatter:on -->