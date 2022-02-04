package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class AbstractMMOTreeSaplingGenerator extends LargeTreeSaplingGenerator {

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createLargeTreeFeature(Random random) {
        return null;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return null;
    }

    protected abstract Feature<? extends TreeFeatureConfig> getFeature(Random random);

    protected abstract Feature<? extends TreeFeatureConfig> getBigFeature(Random random);

    @Override
    public boolean generate(ServerWorld serverWorld, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, Random random) {
        for(int i = 0; i >= -1; --i) {
            for(int j = 0; j >= -1; --j) {
                if (canGenerateLargeTree(blockState, serverWorld, blockPos, i, j)) {
                    return this.generateLargeTree(serverWorld, chunkGenerator, blockPos, blockState, random, i, j);
                }
            }
        }

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

    @Override
    public boolean generateLargeTree(ServerWorld serverWorld, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, Random random, int x, int z) {
        @SuppressWarnings("unchecked")
        Feature<TreeFeatureConfig> feature = (Feature<TreeFeatureConfig>)this.getBigFeature(random);

        if (feature == null) {
            return false;
        } else {
            BlockState blockstate = Blocks.AIR.getDefaultState();
            serverWorld.setBlockState(blockPos.add(x, 0, z), blockstate, 4);
            serverWorld.setBlockState(blockPos.add(x + 1, 0, z), blockstate, 4);
            serverWorld.setBlockState(blockPos.add(x, 0, z + 1), blockstate, 4);
            serverWorld.setBlockState(blockPos.add(x + 1, 0, z + 1), blockstate, 4);
            if (feature.generate(serverWorld, chunkGenerator, random, blockPos.add(x, 0, z), ConfiguredFeatures.OAK.getConfig())) {
                return true;
            } else {
                serverWorld.setBlockState(blockPos.add(x, 0, z), blockstate, 4);
                serverWorld.setBlockState(blockPos.add(x + 1, 0, z), blockstate, 4);
                serverWorld.setBlockState(blockPos.add(x, 0, z + 1), blockstate, 4);
                serverWorld.setBlockState(blockPos.add(x + 1, 0, z + 1), blockstate, 4);
                return false;
            }
        }
    }
}
