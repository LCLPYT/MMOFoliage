package work.lclpnet.mmofoliage.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

@FunctionalInterface
public interface IBlockPosQuery {

    boolean matches(WorldAccess world, BlockPos pos);
}
