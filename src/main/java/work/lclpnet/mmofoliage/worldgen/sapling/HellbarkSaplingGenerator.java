package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class HellbarkSaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected ConfiguredFeature<?, ?> getFeature(Random random) {
        return AdditionalWoodModule.HELLBARK_TREE;
    }
}
