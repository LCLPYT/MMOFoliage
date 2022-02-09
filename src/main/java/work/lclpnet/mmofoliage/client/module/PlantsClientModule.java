package work.lclpnet.mmofoliage.client.module;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import static work.lclpnet.mmocontent.client.render.block.MMORenderLayers.setBlockRenderType;
import static work.lclpnet.mmofoliage.module.PlantsModule.*;

public class PlantsClientModule implements IClientModule {

    @Override
    public void register() {
        final RenderLayer cutout = RenderLayer.getCutout();

        setBlockRenderType(bush, cutout);
        setBlockRenderType(sprout, cutout);
        setBlockRenderType(deadGrass, cutout);
        setBlockRenderType(duneGrass, cutout);

        setBlockRenderType(pottedSprout, cutout);

        setBlockRenderType(willowVine, cutout);
        setBlockRenderType(barley, cutout);
        setBlockRenderType(cattail, cutout);
        setBlockRenderType(reed, cutout);
        setBlockRenderType(deadBranch, cutout);

        setBlockRenderType(glowshroom, cutout);
        setBlockRenderType(toadstool, cutout);
        setBlockRenderType(pottedGlowshroom, cutout);
        setBlockRenderType(pottedToadstool, cutout);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex)
                        -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(),
                bush, sprout, pottedSprout, AdditionalWoodModule.palm.leaves, willowVine, AdditionalWoodModule.willow.leaves);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            BlockState BlockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return MinecraftClient.getInstance().getBlockColors().getColor(BlockState, null, null, tintIndex);
        }, bush, sprout, AdditionalWoodModule.palm.leaves, willowVine, AdditionalWoodModule.willow.leaves);
    }
}
