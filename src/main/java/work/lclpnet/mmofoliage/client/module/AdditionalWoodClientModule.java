package work.lclpnet.mmofoliage.client.module;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import work.lclpnet.mmocontent.client.entity.MMOClientEntities;
import work.lclpnet.mmocontent.client.render.block.MMORenderLayers;
import work.lclpnet.mmofoliage.entity.MBoatEntity;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

public class AdditionalWoodClientModule implements IClientModule {

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(AdditionalWoodModule.boatEntityType, (manager, context) -> new BoatEntityRenderer(manager));

        @SuppressWarnings("unchecked")
        MMOClientEntities.EntityFactory<MBoatEntity> factory = (type, world) -> new MBoatEntity((EntityType<? extends BoatEntity>) type, world);
        MMOClientEntities.registerNonLiving(AdditionalWoodModule.boatEntityType, factory);

        MMORenderLayers.setBlockRenderType(AdditionalWoodModule.fir.door, RenderLayer.getCutout());
        MMORenderLayers.setBlockRenderType(AdditionalWoodModule.fir.trapdoor, RenderLayer.getCutout());
        MMORenderLayers.setBlockRenderType(AdditionalWoodModule.fir.sapling, RenderLayer.getCutout());
    }
}
