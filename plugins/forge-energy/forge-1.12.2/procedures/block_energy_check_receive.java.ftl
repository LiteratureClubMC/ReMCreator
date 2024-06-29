<#-- @formatter:off -->
(new Object() {
    public int receiveEnergySimulate(World world, BlockPos pos, int amount) {
        AtomicInteger retval = new AtomicInteger(0);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            IEnergyStorage storage = tileEntity.getCapability(CapabilityEnergy.ENERGY, ${input$direction});
            if (storage != null) {
                retval.set(storage.receiveEnergy(amount, true));
            }
        }
        return retval.get();
    }
}).receiveEnergySimulate(world, new BlockPos((int)${input$x}, (int)${input$y}, (int)${input$z}), (int)${input$amount})
<#-- @formatter:on -->