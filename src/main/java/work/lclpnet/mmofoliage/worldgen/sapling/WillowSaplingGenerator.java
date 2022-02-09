package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class WillowSaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected Feature<? extends TreeFeatureConfig> getFeature(Random random) {
        return AdditionalWoodModule.WILLOW_TREE;
    }
}