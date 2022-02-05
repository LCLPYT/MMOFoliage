package work.lclpnet.mmofoliage.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofoliage.asm.mixin.common.PlantBlockAccessor;

public class FTF {

    public static boolean canSustainSapling(Block plantBlock, WorldAccess world, BlockPos pos) {
        return ((PlantBlockAccessor) plantBlock).invokeCanPlantOnTop(world.getBlockState(pos), world, pos);
    }
}
