<#-- @formatter:off -->
{
	TileEntity _ent = world.getTileEntity(new BlockPos((int)${input$x}, (int)${input$y}, (int)${input$z}));
	int _amount = (int)${input$amount};
	if (_ent != null)
	{
		IEnergyStorage capability = _ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
		if (capability != null)
			if (capability.canReceive() && capability.getEnergyStored() < capability.getMaxEnergyStored()-5)
				capability.receiveEnergy(_amount, false);
	}
}

<#-- @formatter:on -->