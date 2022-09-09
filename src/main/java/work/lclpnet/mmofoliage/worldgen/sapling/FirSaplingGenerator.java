package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class FirSaplingGenerator extends AbstractBigTreeSaplingGenerator {

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getFeature(Random random) {
        return AdditionalWoodModule.FIR_TREE;
    }

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getBigFeature(Random random) {
        return AdditionalWoodModule.FIR_TREE_LARGE;
    }
}
