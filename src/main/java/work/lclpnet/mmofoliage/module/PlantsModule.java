package work.lclpnet.mmofoliage.module;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.MMOPottedPlantUtil;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.MFoliageBlock;

public class PlantsModule implements IModule {

    public static MFoliageBlock bush, sprout, dune_grass, dead_grass;
    public static FlowerPotBlock pottedSprout;

    @Override
    public void register() {
        new MMOBlockRegistrar(bush = new MFoliageBlock(getPlantSettings()))
                .register(MMOFoliage.identifier("bush"), MMOFoliage.ITEM_GROUP);

        new MMOBlockRegistrar(sprout = new MFoliageBlock(getPlantSettings()))
                .register(MMOFoliage.identifier("sprout"), MMOFoliage.ITEM_GROUP);

        pottedSprout = MMOPottedPlantUtil.addPottedPlant(sprout, "sprout", MMOFoliage::identifier);

        new MMOBlockRegistrar(dune_grass = new MFoliageBlock(getPlantSettings()))
                .register(MMOFoliage.identifier("dune_grass"), MMOFoliage.ITEM_GROUP);

        new MMOBlockRegistrar(dead_grass = new MFoliageBlock(getPlantSettings()))
                .register(MMOFoliage.identifier("dead_grass"), MMOFoliage.ITEM_GROUP);
    }

    protected AbstractBlock.Settings getPlantSettings() {
        return AbstractBlock.Settings.of(Material.REPLACEABLE_PLANT)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS);
    }
}
