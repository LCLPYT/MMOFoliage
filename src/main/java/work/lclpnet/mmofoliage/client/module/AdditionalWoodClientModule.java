package work.lclpnet.mmofoliage.client.module;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import work.lclpnet.mmocontent.client.entity.MMOClientEntities;
import work.lclpnet.mmofoliage.entity.MBoatEntity;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import static work.lclpnet.mmocontent.client.render.block.MMORenderLayers.setBlockRenderType;
import static work.lclpnet.mmofoliage.module.AdditionalWoodModule.*;

public class AdditionalWoodClientModule implements IClientModule {

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(AdditionalWoodModule.boatEntityType, (manager, context) -> new BoatEntityRenderer(manager));

        @SuppressWarnings("unchecked")
        MMOClientEntities.EntityFactory<MBoatEntity> factory = (type, world) -> new MBoatEntity((EntityType<? extends BoatEntity>) type, world);
        MMOClientEntities.registerNonLiving(AdditionalWoodModule.boatEntityType, factory);

        final RenderLayer cutout = RenderLayer.getCutout();

        // fir
        setBlockRenderType(fir.door, cutout);
        setBlockRenderType(fir.trapdoor, cutout);
        setBlockRenderType(fir.sapling, cutout);

        // cherry
        setBlockRenderType(cherry.door, cutout);
        setBlockRenderType(cherry.trapdoor, cutout);
        setBlockRenderType(whiteCherrySapling, cutout);
        setBlockRenderType(pinkCherrySapling, cutout);
    }
}
