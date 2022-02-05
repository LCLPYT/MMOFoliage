package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class DeadSaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected Feature<? extends TreeFeatureConfig> getFeature(Random random) {
        return random.nextInt(10) == 0 ? AdditionalWoodModule.DYING_TREE : AdditionalWoodModule.SMALL_DEAD_TREE;
    }
}
