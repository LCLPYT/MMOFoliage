package work.lclpnet.mmofoliage.asm.type;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;
import java.util.function.BiConsumer;

public interface IFoliageTreeFeature {

    boolean generateCustomTree(StructureWorldAccess world, Random random, BlockPos startPos, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TreeFeatureConfig configBase);
}
