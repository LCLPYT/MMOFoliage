package work.lclpnet.mmofoliage.client.module;

import net.minecraft.client.render.RenderLayer;
import work.lclpnet.mmocontent.client.entity.MMOBoatClientUtility;

import static work.lclpnet.mmocontent.client.render.block.MMORenderLayers.setBlockRenderType;
import static work.lclpnet.mmofoliage.module.AdditionalWoodModule.*;

public class AdditionalWoodClientModule implements IClientModule {

    @Override
    public void register() {
        MMOBoatClientUtility.enableMMOBoatClientIntegration();

        final RenderLayer cutout = RenderLayer.getCutout();

        // fir
        setBlockRenderType(fir.door, cutout);
        setBlockRenderType(fir.trapdoor, cutout);
        setBlockRenderType(fir.sapling, cutout);
        setBlockRenderType(fir.pottedSapling, cutout);

        // cherry
        setBlockRenderType(cherry.door, cutout);
        setBlockRenderType(cherry.trapdoor, cutout);
        setBlockRenderType(whiteCherrySapling, cutout);
        setBlockRenderType(pottedWhiteCherrySapling, cutout);
        setBlockRenderType(pinkCherrySapling, cutout);
        setBlockRenderType(pottedPinkCherrySapling, cutout);

        // dead
        setBlockRenderType(dead.door, cutout);
        setBlockRenderType(dead.trapdoor, cutout);
        setBlockRenderType(dead.sapling, cutout);
        setBlockRenderType(dead.pottedSapling, cutout);

        // hellbark
        setBlockRenderType(hellbark.door, cutout);
        setBlockRenderType(hellbark.trapdoor, cutout);
        setBlockRenderType(hellbark.sapling, cutout);
        setBlockRenderType(hellbark.pottedSapling, cutout);

        // jacaranda
        setBlockRenderType(jacaranda.door, cutout);
        setBlockRenderType(jacaranda.trapdoor, cutout);
        setBlockRenderType(jacaranda.sapling, cutout);
        setBlockRenderType(jacaranda.pottedSapling, cutout);

        // palm
        setBlockRenderType(palm.door, cutout);
        setBlockRenderType(palm.trapdoor, cutout);
        setBlockRenderType(palm.sapling, cutout);
        setBlockRenderType(palm.pottedSapling, cutout);

        // willow
        setBlockRenderType(willow.door, cutout);
        setBlockRenderType(willow.trapdoor, cutout);
        setBlockRenderType(willow.sapling, cutout);
        setBlockRenderType(willow.pottedSapling, cutout);

        // yellow autumn
        setBlockRenderType(yellowAutumnSapling, cutout);
        setBlockRenderType(pottedYellowAutumnSapling, cutout);

        // orange autumn
        setBlockRenderType(orangeAutumnSapling, cutout);
        setBlockRenderType(pottedOrangeAutumnSapling, cutout);

        // maple
        setBlockRenderType(mapleSapling, cutout);
        setBlockRenderType(pottedMapleSapling, cutout);
    }
}
