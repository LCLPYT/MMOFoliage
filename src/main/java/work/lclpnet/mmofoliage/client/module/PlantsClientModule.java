package work.lclpnet.mmofoliage.client.module;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import work.lclpnet.mmocontent.client.render.block.MMORenderLayers;
import work.lclpnet.mmofoliage.module.PlantsModule;

public class PlantsClientModule implements IClientModule {

    @Override
    public void register() {
        MMORenderLayers.setBlockRenderType(PlantsModule.bush, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex)
                        -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(),
                PlantsModule.bush);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            BlockState BlockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return MinecraftClient.getInstance().getBlockColors().getColor(BlockState, null, null, tintIndex);
        }, PlantsModule.bush);
    }
}
