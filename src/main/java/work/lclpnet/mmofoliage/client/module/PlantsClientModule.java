package work.lclpnet.mmofoliage.client.module;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;
import work.lclpnet.mmofoliage.module.PlantsModule;

import static work.lclpnet.mmocontent.client.render.block.MMORenderLayers.setBlockRenderType;

public class PlantsClientModule implements IClientModule {

    @Override
    public void register() {
        final RenderLayer cutout = RenderLayer.getCutout();

        setBlockRenderType(PlantsModule.bush, cutout);
        setBlockRenderType(PlantsModule.sprout, cutout);
        setBlockRenderType(PlantsModule.deadGrass, cutout);
        setBlockRenderType(PlantsModule.duneGrass, cutout);

        setBlockRenderType(PlantsModule.pottedSprout, cutout);

        setBlockRenderType(PlantsModule.willowVine, cutout);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex)
                        -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(),
                PlantsModule.bush, PlantsModule.sprout, PlantsModule.pottedSprout, AdditionalWoodModule.palm.leaves, PlantsModule.willowVine);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            BlockState BlockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return MinecraftClient.getInstance().getBlockColors().getColor(BlockState, null, null, tintIndex);
        }, PlantsModule.bush, PlantsModule.sprout, AdditionalWoodModule.palm.leaves, PlantsModule.willowVine);
    }
}
