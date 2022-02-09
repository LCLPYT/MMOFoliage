package work.lclpnet.mmofoliage.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import work.lclpnet.mmofoliage.util.FTF;

public class MTallWatersidePlantBlock extends MTallWaterPlantBlock implements Waterloggable {

    public MTallWatersidePlantBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.getBlock() != this) {
            return super.canPlaceAt(state, world, pos);
        } else {
            if (state.get(HALF) != DoubleBlockHalf.UPPER) {
                if (world.getFluidState(pos).getFluid() == Fluids.WATER) return true;

                return FTF.canSustainBeach(world.getBlockState(pos.down()), world, pos.down());
            } else {
                BlockState soil = world.getBlockState(pos.down());
                return soil.getBlock() == this && soil.get(HALF) == DoubleBlockHalf.LOWER;
            }
        }
    }
}
