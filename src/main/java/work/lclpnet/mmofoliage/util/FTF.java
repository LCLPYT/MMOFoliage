package work.lclpnet.mmofoliage.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SandBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import work.lclpnet.mmofoliage.asm.mixin.common.PlantBlockAccessor;
import work.lclpnet.mmofoliage.module.SoilModule;

public class FTF {

    public static boolean canSustainSapling(Block plantBlock, WorldAccess world, BlockPos pos) {
        return ((PlantBlockAccessor) plantBlock).invokeCanPlantOnTop(world.getBlockState(pos), world, pos);
    }

    public static boolean canSustainBeach(BlockState state, WorldView world, BlockPos pos) {
        boolean isBeach = state.isOf(Blocks.GRASS_BLOCK) || state.getBlock().isIn(SoilModule.dirt) || state.getBlock() instanceof SandBlock;
        boolean hasWater = false;
        for (Direction face : Direction.Type.HORIZONTAL) {
            BlockState blockState = world.getBlockState(pos.offset(face));
            FluidState fluidState = world.getFluidState(pos.offset(face));
            hasWater = blockState.isOf(Blocks.FROSTED_ICE) || fluidState.isIn(FluidTags.WATER);
            if (hasWater) break;
        }
        return isBeach && hasWater;
    }
}
