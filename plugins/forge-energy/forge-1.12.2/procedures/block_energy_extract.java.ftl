<#-- @formatter:off -->
{
	TileEntity _ent = world.getTileEntity(new BlockPos((int)${input$x}, (int)${input$y}, (int)${input$z}));
	int _amount = (int)${input$amount};
	if (_ent != null && _ent.hasCapability(CapabilityEnergy.ENERGY, ${input$direction})) {
		IEnergyStorage capability = _ent.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
		if (capability != null && capability.canExtract()) {
			capability.extractEnergy(_amount, false);
		}
	}
}
<#-- @formatter:on -->