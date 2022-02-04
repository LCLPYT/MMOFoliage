package work.lclpnet.mmofoliage.asm.type;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;
import java.util.Set;

public interface IFoliageTreeFeature {

    boolean generateCustomTree(ModifiableTestableWorld world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config);
}
