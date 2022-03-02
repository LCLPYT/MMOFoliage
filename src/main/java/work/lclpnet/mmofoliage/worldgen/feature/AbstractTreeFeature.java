package work.lclpnet.mmofoliage.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.asm.type.IFoliageTreeFeature;
import work.lclpnet.mmofoliage.module.PlantsModule;
import work.lclpnet.mmofoliage.worldgen.config.CustomTreeConfig;

import java.util.Random;
import java.util.function.BiConsumer;

public abstract class AbstractTreeFeature<C extends CustomTreeConfig> extends TreeFeature implements IFoliageTreeFeature {

    @SuppressWarnings("unchecked")
    protected AbstractTreeFeature(Codec<C> codec) {
        super((Codec<TreeFeatureConfig>) (Object) codec);  // compiler seems to be a little to strict, since CustomTreeConfig IS extending TreeConfiguration
    }

    public boolean placeLeaves(WorldAccess world, BlockPos pos, BiConsumer<BlockPos, BlockState> leaves, C config) {
        if (this.isReplaceable(world, pos)) {
            leaves.accept(pos, config.foliageProvider.getBlockState(world.getRandom(), pos));
            return true;
        } else {
            return false;
        }
    }

    public boolean placeAltLeaves(WorldAccess level, BlockPos pos, BiConsumer<BlockPos, BlockState> leaves, C config) {
        if (this.isReplaceable(level, pos)) {
            leaves.accept(pos, config.altFoliageProvider.getBlockState(level.getRandom(), pos));
            return true;
        } else {
            return false;
        }
    }

    public boolean placeLog(WorldAccess world, BlockPos pos, BiConsumer<BlockPos, BlockState> logs, C config) {
        return this.placeLog(world, pos, null, logs, config);
    }

    public boolean placeLog(WorldAccess world, BlockPos pos, Direction.Axis axis, BiConsumer<BlockPos, BlockState> logs, C config) {
        Property<Direction.Axis> logAxisProperty = this.getLogAxisProperty(world, pos, config);
        BlockState log = config.trunkProvider.getBlockState(world.getRandom(), pos);
        BlockState directedLog = axis != null && logAxisProperty != null ? log.with(logAxisProperty, axis) : log;
        if (this.isReplaceable(world, pos)) {
            logs.accept(pos, directedLog);
            return true;
        } else {
            return false;
        }
    }

    public boolean setVine(WorldAccess world, Random random, BlockPos pos, Direction side, int length, C config) {
        BlockState vine = config.vineProvider.getBlockState(random, pos);
        BlockState directedVine = vine.getBlock() instanceof VineBlock ? vine
                .with(VineBlock.NORTH, side == Direction.NORTH)
                .with(VineBlock.EAST, side == Direction.EAST)
                .with(VineBlock.SOUTH, side == Direction.SOUTH)
                .with(VineBlock.WEST, side == Direction.WEST) : vine;

        boolean setOne;
        for (setOne = false; world.getBlockState(pos).isAir() && length > 0 && random.nextInt(12) > 0; pos = pos.down()) {
            this.setBlockState(world, pos, directedVine);
            setOne = true;
            --length;
        }

        return setOne;
    }

    public boolean setHanging(WorldAccess world, BlockPos pos, C config) {
        BlockState hanging = config.hangingProvider.getBlockState(world.getRandom(), pos);
        if (isReplaceable(world, pos)) {
            this.setBlockState(world, pos, hanging);
        }

        return false;
    }

    public boolean setTrunkFruit(WorldAccess world, BlockPos pos, C config) {
        BlockState trunkFruit = config.trunkFruitProvider.getBlockState(world.getRandom(), pos);
        if (trunkFruit == null) {
            return false;
        } else {
            if (isReplaceable(world, pos)) {
                this.setBlockState(world, pos, trunkFruit);
            }

            return false;
        }
    }

    protected boolean isReplaceable(WorldAccess level, BlockPos pos) {
        return TreeFeature.isAirOrLeaves(level, pos) || level.testBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            Block block = state.getBlock();
            return block instanceof PlantBlock || material == Material.REPLACEABLE_PLANT || state.isIn(BlockTags.SAPLINGS) || block == Blocks.VINE || block == PlantsModule.willowVine || block == PlantsModule.deadBranch || block == Blocks.MOSS_CARPET;
        });
    }

    @SuppressWarnings("unchecked")
    protected Property<Direction.Axis> getLogAxisProperty(WorldAccess level, BlockPos pos, C config) {
        BlockState log = config.trunkFruitProvider.getBlockState(level.getRandom(), pos);
        return (Property<Direction.Axis>) log.getProperties().stream()
                .filter(Properties.AXIS::equals)
                .findFirst().orElse(null);
    }
}
