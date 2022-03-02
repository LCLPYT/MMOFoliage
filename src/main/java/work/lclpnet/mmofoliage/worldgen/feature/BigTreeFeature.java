package work.lclpnet.mmofoliage.worldgen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.worldgen.config.BigTreeConfig;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

public class BigTreeFeature extends AbstractTreeFeature<BigTreeConfig> {

    private final double trunkHeightScale = 0.618D;

    public BigTreeFeature(Codec<BigTreeConfig> codec) {
        super(codec);
    }

    private void crossSection(WorldAccess world, BlockPos pos, float radius, Random random, BiConsumer<BlockPos, BlockState> leaves, BigTreeConfig config) {
        int r = (int) ((double) radius + this.trunkHeightScale);

        for (int dx = -r; dx <= r; ++dx) {
            for (int dz = -r; dz <= r; ++dz) {
                if (Math.pow((double) Math.abs(dx) + 0.5D, 2.0D) + Math.pow((double) Math.abs(dz) + 0.5D, 2.0D) <= (double) (radius * radius)) {
                    BlockPos blockpos = pos.add(dx, 0, dz);
                    if (this.isReplaceable(world, blockpos)) {
                        if (config.altFoliageProvider.getBlockState(random, pos) != Blocks.AIR.getDefaultState()) {
                            int rand = (new Random()).nextInt(4);
                            if (rand == 0) {
                                placeAltLeaves(world, blockpos, leaves, config);
                            } else {
                                placeLeaves(world, blockpos, leaves, config);
                            }
                        } else {
                            placeLeaves(world, blockpos, leaves, config);
                        }
                    }
                }
            }
        }
    }

    private float treeShape(int height, int y) {
        if ((float) y < (float) height * 0.3F) {
            return -1.0F;
        } else {
            float radius = (float) height / 2.0F;
            float adjacent = radius - (float) y;
            float distance = MathHelper.sqrt(radius * radius - adjacent * adjacent);
            if (adjacent == 0.0F) {
                distance = radius;
            } else if (Math.abs(adjacent) >= radius) {
                return 0.0F;
            }

            return distance * 0.5F;
        }
    }

    private float foliageShape(int y, BigTreeConfig config) {
        if (y >= 0 && y < config.foliageHeight) {
            return y != 0 && y != config.foliageHeight - 1 ? 3.0F : 2.0F;
        } else {
            return -1.0F;
        }
    }

    private void foliageCluster(WorldAccess world, BlockPos pos, Random random, BiConsumer<BlockPos, BlockState> leaves, BigTreeConfig config) {
        for (int y = 0; y < config.foliageHeight; ++y) {
            this.crossSection(world, pos.up(y), this.foliageShape(y, config), random, leaves, config);
        }
    }

    private int checkLineAndOptionallySet(WorldAccess world, BlockPos startPos, BlockPos endPos, boolean set, BiConsumer<BlockPos, BlockState> logs, BigTreeConfig config) {
        if (set || !Objects.equals(startPos, endPos)) {
            BlockPos delta = endPos.add(-startPos.getX(), -startPos.getY(), -startPos.getZ());
            int steps = this.getGreatestDistance(delta);
            float dx = (float) delta.getX() / (float) steps;
            float dy = (float) delta.getY() / (float) steps;
            float dz = (float) delta.getZ() / (float) steps;

            for (int j = 0; j <= steps; ++j) {
                BlockPos deltaPos = startPos.add(0.5F + (float) j * dx, 0.5F + (float) j * dy, 0.5F + (float) j * dz);
                if (set) {
                    this.placeLog(world, deltaPos, this.getLogAxis(startPos, deltaPos), logs, config);
                } else if (!isReplaceable(world, deltaPos)) {
                    return j;
                }
            }

        }
        return -1;
    }

    private int getGreatestDistance(BlockPos posIn) {
        int i = MathHelper.abs(posIn.getX());
        int j = MathHelper.abs(posIn.getY());
        int k = MathHelper.abs(posIn.getZ());
        return k > i && k > j ? k : Math.max(j, i);
    }

    private Direction.Axis getLogAxis(BlockPos startPos, BlockPos endPos) {
        Direction.Axis axis = Direction.Axis.Y;
        int xDiff = Math.abs(endPos.getX() - startPos.getX());
        int zDiff = Math.abs(endPos.getZ() - startPos.getZ());
        int maxDiff = Math.max(xDiff, zDiff);
        if (maxDiff > 0) {
            if (xDiff == maxDiff) {
                axis = Direction.Axis.X;
            } else {
                axis = Direction.Axis.Z;
            }
        }

        return axis;
    }

    private void makeFoliage(WorldAccess worldIn, int height, BlockPos pos, List<FoliageCoordinates> coordinates, Random random, BiConsumer<BlockPos, BlockState> leaves, BigTreeConfig config) {
        for (FoliageCoordinates coordinate : coordinates) {
            if (this.trimBranches(height, coordinate.getBranchBase() - pos.getY())) {
                this.foliageCluster(worldIn, coordinate, random, leaves, config);
            }
        }
    }

    private boolean trimBranches(int height, int localY) {
        return (double) localY >= (double) height * 0.2D;
    }

    private void makeTrunk(WorldAccess world, BlockPos pos, int height, BiConsumer<BlockPos, BlockState> logs, BigTreeConfig config) {
        this.checkLineAndOptionallySet(world, pos, pos.up(height), true, logs, config);
    }

    private void makeBranches(WorldAccess world, int height, BlockPos origin, List<FoliageCoordinates> coordinates, BiConsumer<BlockPos, BlockState> logs, BigTreeConfig config) {
        for (FoliageCoordinates coordinate : coordinates) {
            int branchBase = coordinate.getBranchBase();
            BlockPos baseCoord = new BlockPos(origin.getX(), branchBase, origin.getZ());
            if (!baseCoord.equals(coordinate) && this.trimBranches(height, branchBase - origin.getY())) {
                this.checkLineAndOptionallySet(world, baseCoord, coordinate, true, logs, config);
            }
        }
    }

    @Override
    public boolean generateCustomTree(StructureWorldAccess world, Random random, BlockPos pos, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TreeFeatureConfig configBase) {
        final BigTreeConfig config = (BigTreeConfig) configBase;

        int height = this.checkLocation(world, pos, config.minHeight + random.nextInt(config.maxHeight), logs, config);
        if (height == -1) return false;

        this.setBlockState(world, pos.down(), Blocks.DIRT.getDefaultState());
        int trunkHeight = (int) ((double) height * this.trunkHeightScale);
        if (trunkHeight >= height) {
            trunkHeight = height - 1;
        }

        int clustersPerY = (int) (1.382D + Math.pow(config.foliageDensity * (double) height / 13.0D, 2.0D));
        if (clustersPerY < 1) {
            clustersPerY = 1;
        }

        int trunkTop = pos.getY() + trunkHeight;
        int relativeY = height - config.foliageHeight;
        List<FoliageCoordinates> foliageCoords = Lists.newArrayList();
        foliageCoords.add(new FoliageCoordinates(pos.up(relativeY), trunkTop));

        for (; relativeY >= 0; --relativeY) {
            float treeShape = this.treeShape(height, relativeY);
            if (!(treeShape < 0.0F)) {
                for (int i = 0; i < clustersPerY; ++i) {
                    double radius = 1.0D * (double) treeShape * ((double) random.nextFloat() + 0.328D);
                    double angle = (double) (random.nextFloat() * 2.0F) * 3.141592653589793D;
                    double x = radius * Math.sin(angle) + 0.5D;
                    double z = radius * Math.cos(angle) + 0.5D;
                    BlockPos checkStart = pos.add(x, relativeY - 1, z);
                    BlockPos checkEnd = checkStart.up(5);
                    if (this.checkLineAndOptionallySet(world, checkStart, checkEnd, false, logs, config) == -1) {
                        int dx = pos.getX() - checkStart.getX();
                        int dz = pos.getZ() - checkStart.getZ();
                        double branchSlope = 0.381D;
                        double branchHeight = (double) checkStart.getY() - Math.sqrt(dx * dx + dz * dz) * branchSlope;
                        int branchTop = branchHeight > (double) trunkTop ? trunkTop : (int) branchHeight;
                        BlockPos checkBranchBase = new BlockPos(pos.getX(), branchTop, pos.getZ());
                        if (this.checkLineAndOptionallySet(world, checkBranchBase, checkStart, false, logs, config) == -1) {
                            foliageCoords.add(new FoliageCoordinates(checkStart, checkBranchBase.getY()));
                        }
                    }
                }
            }
        }

        this.makeFoliage(world, height, pos, foliageCoords, random, leaves, config);
        this.makeTrunk(world, pos, trunkHeight, logs, config);
        this.makeBranches(world, height, pos, foliageCoords, logs, config);
        return true;
    }

    private int checkLocation(WorldAccess world, BlockPos pos, int height, BiConsumer<BlockPos, BlockState> logs, BigTreeConfig config) {
        int step = this.checkLineAndOptionallySet(world, pos, pos.up(height - 1), false, logs, config);
        if (step == -1) {
            return height;
        } else {
            return step < 6 ? -1 : step;
        }
    }

    static class FoliageCoordinates extends BlockPos {
        private final int branchBase;

        public FoliageCoordinates(BlockPos pos, int branchBase) {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.branchBase = branchBase;
        }

        public int getBranchBase() {
            return this.branchBase;
        }
    }
}
