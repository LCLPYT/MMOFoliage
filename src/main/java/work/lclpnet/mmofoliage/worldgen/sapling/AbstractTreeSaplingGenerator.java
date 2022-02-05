package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class AbstractTreeSaplingGenerator extends SaplingGenerator {

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return null;
    }

    protected abstract Feature<? extends TreeFeatureConfig> getFeature(Random random);

    @Override
    public boolean generate(ServerWorld serverWorld, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, Random random) {
        @SuppressWarnings("unchecked")
        Feature<TreeFeatureConfig> feature = (Feature<TreeFeatureConfig>) this.getFeature(random);
        if (feature == null) {
            return false;
        } else {
            serverWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
            if (feature.generate(serverWorld, chunkGenerator, random, blockPos, ConfiguredFeatures.OAK.getConfig())) {
                return true;
            } else {
                serverWorld.setBlockState(blockPos, blockState, 4);
                return false;
            }
        }
    }
}
