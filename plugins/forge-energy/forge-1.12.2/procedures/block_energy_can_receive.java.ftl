<#-- @formatter:off -->
(new Object(){
	public boolean canReceiveEnergy(World world, BlockPos pos) {
		AtomicBoolean _retval = new AtomicBoolean(false);
		TileEntity _ent = world.getTileEntity(pos);
		if (_ent != null)
		{
			IEnergyStorage capability = _ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
			if (capability != null)
			{
				_retval.set(capability.canReceive());
			}
		}
		return _retval.get();
	}
}.canReceiveEnergy(world, new BlockPos((int)${input$x},(int)${input$y},(int)${input$z})))
<#-- @formatter:on -->
