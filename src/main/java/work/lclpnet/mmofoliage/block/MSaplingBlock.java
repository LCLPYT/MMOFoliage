package work.lclpnet.mmofoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherrackBlock;
import net.minecraft.block.SandBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import work.lclpnet.mmocontent.block.ext.MMOSaplingBlock;
import work.lclpnet.mmofoliage.asm.mixin.common.SaplingBlockAccessor;
import work.lclpnet.mmofoliage.module.AdditionalWoodModule;

import java.util.Random;

public class MSaplingBlock extends MMOSaplingBlock {

    public MSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);

        final int range = 1;
        boolean loaded = world.isRegionLoaded(pos.add(-range, -range, -range), pos.add(range, range, range));

        if (loaded && world.getLightLevel(pos.up()) >= 9 && random.nextInt(7) == 0) {
            this.grow(world, random, pos, state);
        }
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(STAGE) == 0) {
            world.setBlockState(pos, state.cycle(STAGE), 4);
        } else {
            ((SaplingBlockAccessor) this).getGenerator().generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
        }
    }

    @Override
    public void generate(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState, Random random) {
        this.grow(serverWorld, random, blockPos, blockState);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Block ground = world.getBlockState(pos.down()).getBlock();
        if (this.equals(AdditionalWoodModule.palm.sapling)) {
            return ground instanceof SandBlock || super.canPlaceAt(state, world, pos);
        } else if (this.equals(AdditionalWoodModule.hellbark.sapling)) {
            return ground instanceof NetherrackBlock || super.canPlaceAt(state, world, pos);
        } else {
            return super.canPlaceAt(state, world, pos);
        }
    }
}
