package work.lclpnet.mmofoliage.client.module;

import net.minecraft.client.render.RenderLayer;
import work.lclpnet.mmocontent.client.render.block.MMORenderLayers;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

public class AdditionalWoodClientModule implements IClientModule {

    @Override
    public void register() {
        MMORenderLayers.setBlockRenderType(AdditionalWoodModule.fir.door, RenderLayer.getCutout());
        MMORenderLayers.setBlockRenderType(AdditionalWoodModule.fir.trapdoor, RenderLayer.getCutout());
        MMORenderLayers.setBlockRenderType(AdditionalWoodModule.fir.sapling, RenderLayer.getCutout());
    }
}
