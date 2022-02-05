package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class OrangeAutumnSaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected Feature<? extends TreeFeatureConfig> getFeature(Random random) {
        return random.nextInt(10) == 0 ? AdditionalWoodModule.BIG_ORANGE_AUTUMN_TREE : AdditionalWoodModule.ORANGE_AUTUMN_TREE;
    }
}
