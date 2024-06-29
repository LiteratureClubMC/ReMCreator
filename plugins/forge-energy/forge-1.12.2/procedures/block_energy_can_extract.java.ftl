<#-- @formatter:off -->
(new Object(){
	public boolean canExtractEnergy(World world, BlockPos pos) {
		AtomicBoolean _retval = new AtomicBoolean(false);
		TileEntity _ent = world.getTileEntity(pos);
		if (_ent != null)
		{
			IEnergyStorage cap = _ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
			if (cap != null)
				_retval.set(capability.canExtract());
		}
		return _retval.get();
	}
}.canExtractEnergy(world, new BlockPos((int)${input$x},(int)${input$y},(int)${input$z})))
<#-- @formatter:on -->