package work.lclpnet.mmofoliage.module;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmocontent.block.MMOPottedPlantUtil;
import work.lclpnet.mmocontent.block.ext.MMOVineBlock;
import work.lclpnet.mmofoliage.MMOFoliage;
import work.lclpnet.mmofoliage.block.*;
import work.lclpnet.mmofoliage.worldgen.feature.HugeGlowshroomFeature;
import work.lclpnet.mmofoliage.worldgen.feature.HugeToadstoolFeature;

import static work.lclpnet.mmofoliage.MMOFoliage.ITEM_GROUP;
import static work.lclpnet.mmofoliage.MMOFoliage.identifier;

public class PlantsModule implements IModule {

    public static MFoliageBlock bush, sprout, duneGrass, deadGrass;
    public static FlowerPotBlock pottedSprout, pottedGlowshroom, pottedToadstool;
    public static MMOVineBlock willowVine;
    public static MTallPlantBlock barley;
    public static MTallWatersidePlantBlock cattail;
    public static MTallWaterPlantBlock reed;
    public static DeadBranchBlock deadBranch;
    public static MMushroomPlantBlock glowshroom, toadstool;

    public static Feature<DefaultFeatureConfig>
            HUGE_GLOWSHROOM,
            HUGE_TOADSTOOL;

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

        new MMOBlockRegistrar(barley = new MTallPlantBlock(getPlantSettings(Material.PLANT, MapColor.TERRACOTTA_YELLOW)))
                .register(identifier("barley"), ITEM_GROUP);

        new MMOBlockRegistrar(cattail = new MTallWatersidePlantBlock(getPlantSettings(Material.PLANT, MapColor.DIRT_BROWN)))
                .register(identifier("cattail"), ITEM_GROUP);

        new MMOBlockRegistrar(reed = new MTallWaterPlantBlock(getPlantSettings(Material.UNDERWATER_PLANT, MapColor.DIRT_BROWN)))
                .register(identifier("reed"), ITEM_GROUP);

        new MMOBlockRegistrar(deadBranch = new DeadBranchBlock())
                .register(identifier("dead_branch"), ITEM_GROUP);

        final String glowshroom = "glowshroom";
        new MMOBlockRegistrar(PlantsModule.glowshroom = new MMushroomPlantBlock(getPlantSettings(Material.PLANT, MapColor.DIAMOND_BLUE)
                .luminance(blockState -> 6)))
                .register(identifier(glowshroom), ITEM_GROUP);

        pottedGlowshroom = MMOPottedPlantUtil.addPottedPlant(PlantsModule.glowshroom, glowshroom, MMOFoliage::identifier);

        HUGE_GLOWSHROOM = register("huge_glowshroom", new HugeGlowshroomFeature(DefaultFeatureConfig.CODEC));

        final String toadstool = "toadstool";
        new MMOBlockRegistrar(PlantsModule.toadstool = new MMushroomPlantBlock(getPlantSettings(Material.PLANT, MapColor.DIAMOND_BLUE)))
                .register(identifier(toadstool), ITEM_GROUP);

        pottedToadstool = MMOPottedPlantUtil.addPottedPlant(PlantsModule.toadstool, toadstool, MMOFoliage::identifier);

        HUGE_TOADSTOOL = register("huge_toadstool", new HugeToadstoolFeature(DefaultFeatureConfig.CODEC));
    }

    private static Feature<DefaultFeatureConfig> register(String name, Feature<DefaultFeatureConfig> feature) {
        return Registry.register(Registry.FEATURE, identifier(name), feature);
    }

    protected AbstractBlock.Settings getPlantSettings() {
        return getPlantSettings(Material.REPLACEABLE_PLANT, Material.REPLACEABLE_PLANT.getColor());
    }

    protected AbstractBlock.Settings getPlantSettings(Material material, MapColor color) {
        return AbstractBlock.Settings.of(material, color)
                .noCollision()
                .breakInstantly()
                .sounds(BlockSoundGroup.GRASS);
    }
}
