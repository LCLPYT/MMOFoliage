package work.lclpnet.mmofoliage.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SandBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import work.lclpnet.mmocontent.block.ext.MMOFlowerBlock;
import work.lclpnet.mmofoliage.module.FlowersModule;

public class MFlowerBlock extends MMOFlowerBlock {

    protected static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public MFlowerBlock(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
        super(suspiciousStewEffect, effectDuration, settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Block block = state.getBlock();
        if (FlowersModule.lavender.equals(block) || FlowersModule.pinkHibiscus.equals(block)) return LARGE_SHAPE;

        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (this.equals(FlowersModule.wildflower) && world.getBlockState(pos.down()).getBlock() instanceof SandBlock) {
            return true;
        }
        return super.canPlaceAt(state, world, pos);
    }
}
