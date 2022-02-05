package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class WhiteCherrySaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected Feature<? extends TreeFeatureConfig> getFeature(Random random) {
        return AdditionalWoodModule.BIG_WHITE_CHERRY_TREE;
//        return random.nextInt(10) == 0 ? AdditionalWoodModule.BIG_WHITE_CHERRY_TREE : AdditionalWoodModule.WHITE_CHERRY_TREE;
    }
}
