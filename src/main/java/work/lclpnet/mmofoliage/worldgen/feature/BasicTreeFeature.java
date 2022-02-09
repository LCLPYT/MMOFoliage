package work.lclpnet.mmofoliage.worldgen.feature;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofoliage.util.FTF;
import work.lclpnet.mmofoliage.worldgen.BlockPosBiPredicate;

import java.util.Random;
import java.util.Set;

public class BasicTreeFeature extends TreeFeatureBase {

    protected int leafLayers;
    protected int leavesOffset;
    protected int maxLeavesRadius;
    protected int leavesLayerHeight;
    protected BlockPosBiPredicate placeVinesOn;
    protected float hangingChance;

    protected BasicTreeFeature(BlockPosBiPredicate placeOn, BlockPosBiPredicate replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight, int leafLayers, int leavesOffset, int maxLeavesRadius, int leavesLayerHeight, BlockPosBiPredicate placeVinesOn, float hangingChance) {
        super(placeOn, replace, log, leaves, altLeaves, vine, hanging, trunkFruit, minHeight, maxHeight);
        this.leafLayers = leafLayers;
        this.leavesOffset = leavesOffset;
        this.maxLeavesRadius = maxLeavesRadius;
        this.leavesLayerHeight = leavesLayerHeight;
        this.placeVinesOn = placeVinesOn;
        this.hangingChance = hangingChance;
    }

    @Override
    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, WorldAccess world, Random random, BlockPos pos, BlockBox boundingBox) {
        int height = random.nextInt(this.maxHeight - this.minHeight) + this.minHeight;
        boolean hasSpace = true;
        if (pos.getY() >= 1 && pos.getY() + height + 1 <= 256) { // TODO 1.18: change this
            for (int y = pos.getY(); y <= pos.getY() + 1 + height; y++) {
                int radius = 1;
                if (y == pos.getY()) radius = 0;
                if (y >= pos.getY() + 1 + height - 2) radius = 2;
                for (int x = pos.getX() - radius; x <= pos.getX() + radius && hasSpace; x++) {
                    for (int z = pos.getZ() - radius; z <= pos.getZ() + radius && hasSpace; z++) {
                        if (y >= 0 && y < 256) {
                            if (!this.replace.matches(world, new BlockPos(x, y, z))) hasSpace = false;
                        } else {
                            hasSpace = false;
                        }
                    }
                }
            }
            if (!hasSpace) return false;
            BlockPos soilPos = pos.down();
            boolean isSoil = FTF.canSustainSapling(Blocks.OAK_SAPLING, world, soilPos);
            if (this.placeOn.matches(world, soilPos) && isSoil && pos.getY() < 256 - height - 1) {
                int leavesLayers = this.leafLayers - 1;
                int i;
                for (i = pos.getY() + height - leavesLayers; i <= pos.getY() + height; i++) {
                    int currentLayer = i - (pos.getY() + height);
                    int leavesRadius = this.maxLeavesRadius - currentLayer / this.leavesLayerHeight;
                    for (int x = pos.getX() - leavesRadius; x <= pos.getX() + leavesRadius; x++) {
                        int xDiff = x - pos.getX();
                        for (int z = pos.getZ() - leavesRadius; z <= pos.getZ() + leavesRadius; z++) {
                            int zDiff = z - pos.getZ();
                            if (Math.abs(xDiff) != leavesRadius || Math.abs(zDiff) != leavesRadius || (random.nextInt(2) != 0 && currentLayer != 0)) {
                                BlockPos leavesPos = new BlockPos(x, i, z);
                                if (this.replace.matches(world, leavesPos)) {
                                    if (this.altLeaves != Blocks.AIR.getDefaultState()) {
                                        if (random.nextInt(4) == 0) {
                                            setAltLeaves(world, leavesPos, changedLeaves, boundingBox);
                                        } else {
                                            placeLeaves(world, leavesPos, changedLeaves, boundingBox);
                                        }
                                    } else {
                                        placeLeaves(world, leavesPos, changedLeaves, boundingBox);
                                    }
                                }
                            }
                        }
                    }
                }
                generateTrunk(changedLogs, boundingBox, world, pos, height);
                if (this.vine != Blocks.AIR.getDefaultState())
                    for (i = pos.getY() - leavesLayers + height; i <= pos.getY() + height; i++) {
                        int currentLayer = i - (pos.getY() + height);
                        int leavesRadius = this.maxLeavesRadius + this.leavesOffset - currentLayer / this.leavesLayerHeight;
                        for (int x = pos.getX() - leavesRadius; x <= pos.getX() + leavesRadius; x++) {
                            for (int z = pos.getZ() - leavesRadius; z <= pos.getZ() + leavesRadius; z++) {
                                BlockPos blockpos3 = new BlockPos(x, i, z);
                                if (world.getBlockState(blockpos3).getMaterial() == Material.LEAVES) {
                                    BlockPos westPos = blockpos3.west();
                                    BlockPos eastPos = blockpos3.east();
                                    BlockPos northPos = blockpos3.north();
                                    BlockPos southPos = blockpos3.south();
                                    if (random.nextInt(4) == 0 && this.placeVinesOn.matches(world, westPos))
                                        extendVines(world, westPos, Direction.EAST);
                                    if (random.nextInt(4) == 0 && this.placeVinesOn.matches(world, eastPos))
                                        extendVines(world, eastPos, Direction.WEST);
                                    if (random.nextInt(4) == 0 && this.placeVinesOn.matches(world, northPos))
                                        extendVines(world, northPos, Direction.SOUTH);
                                    if (random.nextInt(4) == 0 && this.placeVinesOn.matches(world, southPos))
                                        extendVines(world, southPos, Direction.NORTH);
                                }
                            }
                        }
                    }
                if (this.hanging != Blocks.AIR.getDefaultState()) {
                    generateHanging(world, pos, random, height);
                }
                if (this.trunkFruit != Blocks.AIR.getDefaultState()) {
                    if (random.nextInt(5) == 0 && height > 5) {
                        for (int l3 = 0; l3 < 2; l3++) {
                            for (Direction Direction : Direction.Type.HORIZONTAL) {
                                if (random.nextInt(4 - l3) == 0) {
                                    Direction d = Direction.getOpposite();
                                    generateTrunkFruit(world, random.nextInt(3), pos.add(d.getOffsetX(), height - 5 + l3, d.getOffsetZ()), Direction);
                                }
                            }
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    protected void generateTrunk(Set<BlockPos> changedBlocks, BlockBox boundingBox, WorldAccess world, BlockPos start, int height) {
        for (int layer = 0; layer < height; ++layer) {
            BlockPos blockpos2 = start.up(layer);
            if (this.replace.matches(world, blockpos2)) {
                this.placeLog(world, start.up(layer), changedBlocks, boundingBox);
            }
        }
    }

    protected void generateHanging(WorldAccess world, BlockPos start, Random rand, int height) {
        int y = start.getY() + (height - this.leafLayers);

        for (int x = start.getX() - (this.maxLeavesRadius + 1); x <= start.getX() + this.maxLeavesRadius + 1; ++x) {
            for (int z = start.getZ() - (this.maxLeavesRadius + 1); z <= start.getZ() + this.maxLeavesRadius + 1; ++z) {
                BlockPos hangingPos = new BlockPos(x, y, z);
                if (!world.isAir(hangingPos.up()) && world.isAir(hangingPos) && rand.nextFloat() <= this.hangingChance) {
                    this.setHanging(world, hangingPos);
                }
            }
        }
    }

    private void generateTrunkFruit(WorldAccess world, int age, BlockPos pos, Direction direction) {
        if (this.trunkFruit == Blocks.COCOA.getDefaultState()) {
            this.setBlockState(world, pos, this.trunkFruit.with(CocoaBlock.AGE, age).with(CocoaBlock.FACING, direction));
        } else {
            this.setBlockState(world, pos, this.trunkFruit.with(CocoaBlock.FACING, direction));
        }
    }

    private BlockState getVineStateForSide(Direction side) {
        return this.vine.getBlock() instanceof VineBlock ? this.vine.with(VineBlock.getFacingProperty(side), true) : this.vine;
    }

    private void extendVines(WorldAccess world, BlockPos pos, Direction side) {
        BlockState vineState = this.getVineStateForSide(side);
        this.setBlockState(world, pos, vineState);
        int length = 4;

        for (pos = pos.down(); this.placeVinesOn.matches(world, pos) && length > 0; --length) {
            this.setBlockState(world, pos, vineState);
            pos = pos.down();
        }
    }

    @SuppressWarnings("unchecked")
    protected abstract static class InnerBuilder<T extends InnerBuilder<T, F>, F extends TreeFeatureBase> extends BuilderBase<T, F> {
        protected int leafLayers;
        protected int leavesOffset;
        protected int maxLeavesRadius;
        protected int leavesLayerHeight;
        protected BlockPosBiPredicate placeVinesOn;
        protected float hangingChance;

        public T leafLayers(int a) {
            this.leafLayers = a;
            return (T) this;
        }

        public T leavesOffset(int a) {
            this.leavesOffset = a;
            return (T) this;
        }

        public T leavesLayerHeight(int a) {
            this.leavesLayerHeight = a;
            return (T) this;
        }

        public T maxLeavesRadius(int a) {
            this.maxLeavesRadius = a;
            return (T) this;
        }

        public T placeVinesOn(BlockPosBiPredicate a) {
            this.placeVinesOn = a;
            return (T) this;
        }

        public T hangingChance(float a) {
            this.hangingChance = a;
            return (T) this;
        }

        public InnerBuilder() {
            this.placeOn = (world, pos) -> world.getBlockState(pos).isOpaque();
            this.minHeight = 4;
            this.maxHeight = 7;
            this.leafLayers = 4;
            this.leavesOffset = 1;
            this.maxLeavesRadius = 1;
            this.leavesLayerHeight = 2;
            this.placeVinesOn = (world, pos) -> {
                Material mat = world.getBlockState(pos).getMaterial();
                return mat == Material.AIR;
            };
            this.hangingChance = 0.0F;
        }
    }

    public static class Builder extends BasicTreeFeature.InnerBuilder<BasicTreeFeature.Builder, BasicTreeFeature> {
        @Override
        public BasicTreeFeature create() {
            return new BasicTreeFeature(this.placeOn, this.replace, this.log, this.leaves, this.altLeaves, this.vine, this.hanging, this.trunkFruit, this.minHeight, this.maxHeight, this.leafLayers, this.leavesOffset, this.maxLeavesRadius, this.leavesLayerHeight, this.placeVinesOn, this.hangingChance);
        }
    }
}
