package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class FirSaplingGenerator extends AbstractMMOTreeSaplingGenerator {

    @Override
    protected Feature<? extends TreeFeatureConfig> getFeature(Random random) {
        return AdditionalWoodModule.FIR_TREE;
    }

    @Override
    protected Feature<? extends TreeFeatureConfig> getBigFeature(Random random) {
        return AdditionalWoodModule.FIR_TREE_LARGE;
    }
}
