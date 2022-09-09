package work.lclpnet.mmofoliage.worldgen.sapling;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class AbstractTreeSaplingGenerator extends SaplingGenerator {

    @Nullable
    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bl) {
        return null;
    }

    protected abstract RegistryEntry<? extends ConfiguredFeature<?, ?>> getFeature(Random random);

    @Override
    public boolean generate(ServerWorld serverWorld, ChunkGenerator chunkGenerator, BlockPos blockPos, BlockState blockState, Random random) {
        ConfiguredFeature<?, ?> feature = this.getFeature(random).value();
        if (feature == null) {
            return false;
        } else {
            serverWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
            if (feature.generate(serverWorld, chunkGenerator, random, blockPos)) {
                return true;
            } else {
                serverWorld.setBlockState(blockPos, blockState, 4);
                return false;
            }
        }
    }
}
