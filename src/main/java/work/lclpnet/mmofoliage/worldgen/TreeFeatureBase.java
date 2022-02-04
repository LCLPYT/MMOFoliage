package work.lclpnet.mmofoliage.worldgen;

import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.asm.mixin.common.AbstractBlockAccessor;
import work.lclpnet.mmofoliage.asm.mixin.common.PlantBlockAccessor;
import work.lclpnet.mmofoliage.asm.type.IFoliageTreeFeature;

import java.util.Random;
import java.util.Set;

public class TreeFeatureBase extends TreeFeature implements IFoliageTreeFeature {

    protected final IBlockPosQuery placeOn, replace;
    protected final BlockState log;
    protected final BlockState leaves;
    protected final BlockState altLeaves;
    protected final BlockState vine;
    protected final BlockState hanging;
    protected final BlockState trunkFruit;
    protected final int minHeight;
    protected final int maxHeight;
    protected Property<Direction.Axis> logAxisProperty;

    @SuppressWarnings("unchecked")
    protected TreeFeatureBase(IBlockPosQuery placeOn, IBlockPosQuery replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight) {
        super(TreeFeatureConfig.CODEC.stable());
        this.placeOn = placeOn;
        this.replace = replace;
        this.log = log;
        this.leaves = leaves;
        this.logAxisProperty = (Property<Direction.Axis>) log.getProperties().stream()
                .filter(Properties.AXIS::equals)
                .findFirst().orElse(null);
        this.altLeaves = altLeaves;
        this.vine = vine;
        this.hanging = hanging;
        this.trunkFruit = trunkFruit;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public boolean placeLeaves(WorldAccess world, BlockPos pos, Set<BlockPos> changedBlocks, BlockBox boundingBox) {
        if (this.replace.matches(world, pos)) {
            this.placeBlock(world, pos, this.leaves, changedBlocks, boundingBox);
            return true;
        } else {
            return false;
        }
    }

    public boolean placeLog(WorldAccess world, BlockPos pos, Set<BlockPos> changedBlocks, BlockBox boundingBox) {
        return this.placeLog(world, pos, null, changedBlocks, boundingBox);
    }

    public boolean placeLog(WorldAccess world, BlockPos pos, Direction.Axis axis, Set<BlockPos> changedBlocks, BlockBox boundingBox) {
        BlockState directedLog = axis != null && this.logAxisProperty != null ? this.log.with(this.logAxisProperty, axis) : this.log;
        if (this.replace.matches(world, pos)) {
            this.placeBlock(world, pos, directedLog, changedBlocks, boundingBox);
            return true;
        } else {
            return false;
        }
    }

    public boolean setVine(WorldAccess world, Random rand, BlockPos pos, Direction side, int length) {
        BlockState vineState = this.vine.getBlock() instanceof VineBlock ?
                this.vine.with(VineBlock.NORTH, side == Direction.NORTH)
                        .with(VineBlock.EAST, side == Direction.EAST)
                        .with(VineBlock.SOUTH, side == Direction.SOUTH)
                        .with(VineBlock.WEST, side == Direction.WEST) : this.vine;

        boolean setOne;
        for(setOne = false; ((AbstractBlockAccessor) world.getBlockState(pos).getBlock()).getMaterial() == Material.AIR
                && length > 0 && rand.nextInt(12) > 0; pos = pos.down()) {
            this.setBlockState(world, pos, vineState);
            setOne = true;
            --length;
        }

        return setOne;
    }

    public boolean setHanging(WorldAccess world, BlockPos pos) {
        if (this.replace.matches(world, pos)) {
            this.setBlockState(world, pos, this.hanging);
        }

        return false;
    }

    public boolean setTrunkFruit(WorldAccess world, BlockPos pos) {
        if (this.trunkFruit == null) {
            return false;
        } else {
            if (this.replace.matches(world, pos)) {
                this.setBlockState(world, pos, this.trunkFruit);
            }

            return false;
        }
    }

    public boolean setAltLeaves(WorldAccess world, BlockPos pos, Set<BlockPos> changedBlocks, BlockBox boundingBox) {
        if (this.replace.matches(world, pos)) {
            this.placeBlock(world, pos, this.altLeaves, changedBlocks, boundingBox);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean generateCustomTree(ModifiableTestableWorld world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config) {
        return this.place(logPositions, leavesPositions, (WorldAccess) world, random, pos, box);
    }

    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, WorldAccess world, Random rand, BlockPos position, BlockBox boundingBox) {
        return false;
    }

    protected boolean placeBlock(WorldAccess world, BlockPos pos, BlockState state, Set<BlockPos> changedBlocks, BlockBox boundingBox) {
        if (!canTreeReplace(world, pos)) {
            return false;
        } else {
            setBlock(world, pos, state, boundingBox);
            changedBlocks.add(pos.toImmutable());
            return true;
        }
    }

    protected static void setBlock(ModifiableWorld world, BlockPos pos, BlockState state, BlockBox boundingBox) {
        setBlockStateWithoutUpdatingNeighbors(world, pos, state);
        boundingBox.encompass(new BlockBox(pos, pos));
    }

    @SuppressWarnings("unchecked")
    protected abstract static class BuilderBase<T extends BuilderBase<T, F>, F extends TreeFeatureBase> {

        protected IBlockPosQuery placeOn = (world, pos) -> ((PlantBlockAccessor) Blocks.OAK_SAPLING)
                .invokeCanPlantOnTop(world.getBlockState(pos), world, pos);

        protected IBlockPosQuery replace = (world, pos) -> {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            boolean replaceableByLeaves = ((AbstractBlockAccessor) block).getMaterial() == Material.AIR || state.isIn(BlockTags.LEAVES);
            return replaceableByLeaves || block instanceof PlantBlock || block.equals(Blocks.VINE);// || block == willow_vine || block == dead_branch;
        };

        protected BlockState log;
        protected BlockState leaves;
        protected BlockState vine;
        protected BlockState hanging;
        protected BlockState trunkFruit;
        protected BlockState altLeaves;
        protected int minHeight;
        protected int maxHeight;

        public BuilderBase() {
            this.log = Blocks.OAK_LOG.getDefaultState();
            this.leaves = Blocks.OAK_LEAVES.getDefaultState();
            this.vine = Blocks.AIR.getDefaultState();
            this.hanging = Blocks.AIR.getDefaultState();
            this.trunkFruit = Blocks.AIR.getDefaultState();
            this.altLeaves = Blocks.AIR.getDefaultState();
        }

        public T placeOn(IBlockPosQuery a) {
            this.placeOn = a;
            return (T) this;
        }

        public T replace(IBlockPosQuery a) {
            this.replace = a;
            return (T) this;
        }

        public T log(BlockState a) {
            this.log = a;
            return (T) this;
        }

        public T leaves(BlockState a) {
            this.leaves = a;
            return (T) this;
        }

        public T vine(BlockState a) {
            this.vine = a;
            return (T) this;
        }

        public T hanging(BlockState a) {
            this.hanging = a;
            return (T) this;
        }

        public T trunkFruit(BlockState a) {
            this.trunkFruit = a;
            return (T) this;
        }

        public T altLeaves(BlockState a) {
            this.altLeaves = a;
            return (T) this;
        }

        public T minHeight(int a) {
            this.minHeight = a;
            return (T) this;
        }

        public T maxHeight(int a) {
            this.maxHeight = a;
            return (T) this;
        }

        abstract F create();
    }
}
