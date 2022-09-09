package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class WhiteCherrySaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getFeature(Random random) {
        return random.nextInt(10) == 0 ? AdditionalWoodModule.BIG_WHITE_CHERRY_TREE : AdditionalWoodModule.WHITE_CHERRY_TREE;
    }
}
