package work.lclpnet.mmofoliage.module;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.MMOPottedPlantUtil;
import work.lclpnet.mmocontent.block.ext.MMOVineBlock;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.MFoliageBlock;
import work.lclpnet.mmofoliage.block.MTallPlantBlock;
import work.lclpnet.mmofoliage.block.MTallWaterPlantBlock;
import work.lclpnet.mmofoliage.block.MTallWatersidePlantBlock;

import static work.lclpnet.mmofoliage.MMOFoliage.ITEM_GROUP;
import static work.lclpnet.mmofoliage.MMOFoliage.identifier;

public class PlantsModule implements IModule {

    public static MFoliageBlock bush, sprout, duneGrass, deadGrass;
    public static FlowerPotBlock pottedSprout;
    public static MMOVineBlock willowVine;
    public static MTallPlantBlock barley;
    public static MTallWatersidePlantBlock cattail;
    public static MTallWaterPlantBlock reed;

    @Override
    public void register() {
        new MMOBlockRegistrar(bush = new MFoliageBlock(getPlantSettings()))
                .register(identifier("bush"), ITEM_GROUP);

        new MMOBlockRegistrar(sprout = new MFoliageBlock(getPlantSettings()))
                .register(identifier("sprout"), ITEM_GROUP);

        pottedSprout = MMOPottedPlantUtil.addPottedPlant(sprout, "sprout", MMOFoliage::identifier);

        new MMOBlockRegistrar(duneGrass = new MFoliageBlock(getPlantSettings()))
                .register(identifier("dune_grass"), ITEM_GROUP);

        new MMOBlockRegistrar(deadGrass = new MFoliageBlock(getPlantSettings()))
                .register(identifier("dead_grass"), ITEM_GROUP);

        new MMOBlockRegistrar(willowVine = new MMOVineBlock())
                .register(identifier("willow_vine"), ITEM_GROUP);

        new MMOBlockRegistrar(barley = new MTallPlantBlock(getPlantSettings(Material.PLANT, MaterialColor.YELLOW_TERRACOTTA)))
                .register(identifier("barley"), ITEM_GROUP);

        new MMOBlockRegistrar(cattail = new MTallWatersidePlantBlock(getPlantSettings(Material.PLANT, MaterialColor.DIRT)))
                .register(identifier("cattail"), ITEM_GROUP);

        new MMOBlockRegistrar(reed = new MTallWaterPlantBlock(getPlantSettings(Material.UNDERWATER_PLANT, MaterialColor.DIRT)))
                .register(identifier("reed"), ITEM_GROUP);
    }

    protected AbstractBlock.Settings getPlantSettings() {
        return getPlantSettings(Material.REPLACEABLE_PLANT, Material.REPLACEABLE_PLANT.getColor());
    }

    protected AbstractBlock.Settings getPlantSettings(Material material, MaterialColor color) {
        return AbstractBlock.Settings.of(material, color)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS);
    }
}
