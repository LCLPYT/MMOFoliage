package work.lclpnet.mmofoliage.client.module;

import net.minecraft.client.render.RenderLayer;

import static work.lclpnet.mmocontent.client.render.block.MMORenderLayers.setBlockRenderType;
import static work.lclpnet.mmofoliage.module.FlowersModule.*;

public class FlowersClientModule implements IClientModule {

    @Override
    public void register() {
        final RenderLayer cutout = RenderLayer.getCutout();

        setBlockRenderType(violet, cutout);
        setBlockRenderType(lavender, cutout);
        setBlockRenderType(wildflower, cutout);
        setBlockRenderType(pinkDaffodil, cutout);
        setBlockRenderType(pinkHibiscus, cutout);
        setBlockRenderType(glowflower, cutout);
        setBlockRenderType(wiltedLily, cutout);
        setBlockRenderType(blueHydrangea, cutout);
        setBlockRenderType(goldenrod, cutout);

        setBlockRenderType(pottedViolet, cutout);
        setBlockRenderType(pottedLavender, cutout);
        setBlockRenderType(pottedWildflower, cutout);
        setBlockRenderType(pottedPinkDaffodil, cutout);
        setBlockRenderType(pottedPinkHibiscus, cutout);
        setBlockRenderType(pottedGlowflower, cutout);
        setBlockRenderType(pottedWiltedLily, cutout);
    }
}
