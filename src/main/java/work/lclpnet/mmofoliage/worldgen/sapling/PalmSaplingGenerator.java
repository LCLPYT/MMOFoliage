package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class PalmSaplingGenerator extends AbstractTreeSaplingGenerator {

    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getFeature(Random random) {
        return AdditionalWoodModule.PALM_TREE;
    }
}
