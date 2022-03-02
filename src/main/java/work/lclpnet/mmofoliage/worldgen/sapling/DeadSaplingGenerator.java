package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class DeadSaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected ConfiguredFeature<?, ?> getFeature(Random random) {
        return random.nextInt(10) == 0 ? AdditionalWoodModule.DYING_TREE : AdditionalWoodModule.SMALL_DEAD_TREE;
    }
}
