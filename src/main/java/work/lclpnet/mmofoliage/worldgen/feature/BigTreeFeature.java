package work.lclpnet.mmofoliage.worldgen.feature;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofoliage.worldgen.BlockPosBiPredicate;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class BigTreeFeature extends TreeFeatureBase {

    private final double trunkHeightScale = 0.618D;
    private int trunkWidth;
    private final int foliageHeight;
    private final double foliageDensity;

    protected BigTreeFeature(BlockPosBiPredicate placeOn, BlockPosBiPredicate replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight, int trunkWidth, int foliageHeight, double foliageDensity) {
        super(placeOn, replace, log, leaves, altLeaves, vine, hanging, trunkFruit, minHeight, maxHeight);
        this.foliageHeight = foliageHeight;
        this.foliageDensity = foliageDensity;
        this.trunkWidth = trunkWidth;
    }

    private void crossSection(WorldAccess world, BlockPos pos, float radius, BlockBox boundingBox, Set<BlockPos> changedBlocks) {
        int r = (int) ((double) radius + this.trunkHeightScale);

        for (int dx = -r; dx <= r; ++dx) {
            for (int dz = -r; dz <= r; ++dz) {
                if (Math.pow((double) Math.abs(dx) + 0.5D, 2.0D) + Math.pow((double) Math.abs(dz) + 0.5D, 2.0D) <= (double) (radius * radius)) {
                    BlockPos blockpos = pos.add(dx, 0, dz);
                    if (this.replace.matches(world, blockpos)) {
                        if (this.altLeaves != Blocks.AIR.getDefaultState()) {
                            int rand = (new Random()).nextInt(4);
                            if (rand == 0) {
                                this.placeBlock(world, blockpos, this.altLeaves, changedBlocks, boundingBox);
                            } else {
                                this.placeBlock(world, blockpos, this.leaves, changedBlocks, boundingBox);
                            }
                        } else {
                            this.placeBlock(world, blockpos, this.leaves, changedBlocks, boundingBox);
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

    private float foliageShape(int y) {
        if (y >= 0 && y < this.foliageHeight) {
            return y != 0 && y != this.foliageHeight - 1 ? 3.0F : 2.0F;
        } else {
            return -1.0F;
        }
    }

    private void foliageCluster(WorldAccess world, BlockPos pos, BlockBox boundingBox, Set<BlockPos> changedBlocks) {
        for (int y = 0; y < this.foliageHeight; ++y) {
            this.crossSection(world, pos.up(y), this.foliageShape(y), boundingBox, changedBlocks);
        }

    }

    private int checkLineAndOptionallySet(Set<BlockPos> changedBlocks, WorldAccess world, BlockPos startPos, BlockPos endPos, boolean set, BlockBox boundingBox) {
        if (set || !Objects.equals(startPos, endPos)) {
            BlockPos delta = endPos.add(-startPos.getX(), -startPos.getY(), -startPos.getZ());
            int steps = this.getGreatestDistance(delta);
            float dx = (float) delta.getX() / (float) steps;
            float dy = (float) delta.getY() / (float) steps;
            float dz = (float) delta.getZ() / (float) steps;

            for (int j = 0; j <= steps; ++j) {
                BlockPos deltaPos = startPos.add(0.5F + (float) j * dx, 0.5F + (float) j * dy, 0.5F + (float) j * dz);
                if (set) {
                    this.placeLog(world, deltaPos, this.getLogAxis(startPos, deltaPos), changedBlocks, boundingBox);
                } else if (!canTreeReplace(world, deltaPos)) {
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

    private void makeFoliage(WorldAccess worldIn, int height, BlockPos pos, List<BigTreeFeature.FoliageCoordinates> coordinates, BlockBox boundingBox, Set<BlockPos> changedBlocks) {
        for (BigTreeFeature.FoliageCoordinates coordinate : coordinates) {
            if (this.trimBranches(height, coordinate.getBranchBase() - pos.getY())) {
                this.foliageCluster(worldIn, coordinate, boundingBox, changedBlocks);
            }
        }
    }

    private boolean trimBranches(int height, int localY) {
        return (double) localY >= (double) height * 0.2D;
    }

    private void makeTrunk(Set<BlockPos> changedBlocks, WorldAccess world, BlockPos pos, int height, BlockBox boundingBox) {
        this.checkLineAndOptionallySet(changedBlocks, world, pos, pos.up(height), true, boundingBox);
        if (this.trunkWidth == 2) {
            this.checkLineAndOptionallySet(changedBlocks, world, pos.east(), pos.up(height).east(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.east().south(), pos.up(height).east().south(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.south(), pos.up(height).south(), true, boundingBox);
        }

        if (this.trunkWidth == 4) {
            this.checkLineAndOptionallySet(changedBlocks, world, pos.east(), pos.up(height).east(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.east().south(), pos.up(height).east().south(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.south(), pos.up(height).south(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.north(), pos.up(height).north(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.north().east(), pos.up(height).north().east(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.east().east(), pos.up(height).east().east(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.south().east().east(), pos.up(height).south().east().east(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.south().south().east(), pos.up(height).south().south().east(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.south().south(), pos.up(height).south().south(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.west().south(), pos.up(height).west().south(), true, boundingBox);
            this.checkLineAndOptionallySet(changedBlocks, world, pos.west(), pos.up(height).west(), true, boundingBox);
        }

    }

    private void makeBranches(Set<BlockPos> changedBlocks, WorldAccess world, int height, BlockPos origin, List<BigTreeFeature.FoliageCoordinates> coordinates, BlockBox boundingBox) {
        for (BigTreeFeature.FoliageCoordinates coordinate : coordinates) {
            int branchBase = coordinate.getBranchBase();
            BlockPos baseCoord = new BlockPos(origin.getX(), branchBase, origin.getZ());
            if (!baseCoord.equals(coordinate) && this.trimBranches(height, branchBase - origin.getY())) {
                this.checkLineAndOptionallySet(changedBlocks, world, baseCoord, coordinate, true, boundingBox);
            }
        }
    }

    @Override
    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, WorldAccess world, Random rand, BlockPos pos, BlockBox boundingBox) {
        Random random = new Random(rand.nextLong());
        int height = this.checkLocation(changedLogs, world, pos, this.minHeight + random.nextInt(this.maxHeight), boundingBox);
        if (height == -1) {
            return false;
        } else {
            this.setBlockState(world, pos.down(), Blocks.DIRT.getDefaultState());
            int trunkHeight = (int) ((double) height * this.trunkHeightScale);
            if (trunkHeight >= height) {
                trunkHeight = height - 1;
            }

            int clustersPerY = (int) (1.382D + Math.pow(this.foliageDensity * (double) height / 13.0D, 2.0D));
            if (clustersPerY < 1) {
                clustersPerY = 1;
            }

            int trunkTop = pos.getY() + trunkHeight;
            int relativeY = height - this.foliageHeight;
            List<BigTreeFeature.FoliageCoordinates> foliageCoords = Lists.newArrayList();
            foliageCoords.add(new BigTreeFeature.FoliageCoordinates(pos.up(relativeY), trunkTop));

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
                        if (this.checkLineAndOptionallySet(changedLogs, world, checkStart, checkEnd, false, boundingBox) == -1) {
                            int dx = pos.getX() - checkStart.getX();
                            int dz = pos.getZ() - checkStart.getZ();
                            double branchSlope = 0.381D;
                            double branchHeight = (double) checkStart.getY() - Math.sqrt(dx * dx + dz * dz) * branchSlope;
                            int branchTop = branchHeight > (double) trunkTop ? trunkTop : (int) branchHeight;
                            BlockPos checkBranchBase = new BlockPos(pos.getX(), branchTop, pos.getZ());
                            if (this.checkLineAndOptionallySet(changedLogs, world, checkBranchBase, checkStart, false, boundingBox) == -1) {
                                foliageCoords.add(new BigTreeFeature.FoliageCoordinates(checkStart, checkBranchBase.getY()));
                            }
                        }
                    }
                }
            }

            this.makeFoliage(world, height, pos, foliageCoords, boundingBox, changedLeaves);
            this.makeTrunk(changedLogs, world, pos, trunkHeight, boundingBox);
            this.makeBranches(changedLogs, world, height, pos, foliageCoords, boundingBox);
            return true;
        }
    }

    private int checkLocation(Set<BlockPos> changedBlocks, WorldAccess world, BlockPos pos, int height, BlockBox boundingBox) {
        if (!this.placeOn.matches(world, pos.down())) {
            return -1;
        } else {
            int step = this.checkLineAndOptionallySet(changedBlocks, world, pos, pos.up(height - 1), false, boundingBox);
            if (step == -1) {
                return height;
            } else {
                return step < 6 ? -1 : step;
            }
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

    public static class Builder extends BuilderBase<BigTreeFeature.Builder, BigTreeFeature> {
        private int trunkWidth;
        private int foliageHeight;
        private double foliageDensity;

        public BigTreeFeature.Builder trunkWidth(int a) {
            this.trunkWidth = a;
            return this;
        }

        public BigTreeFeature.Builder foliageHeight(int a) {
            this.foliageHeight = a;
            return this;
        }

        public BigTreeFeature.Builder foliageDensity(int a) {
            this.foliageDensity = a;
            return this;
        }

        public Builder() {
            this.minHeight = 5;
            this.maxHeight = 12;
            this.trunkWidth = 1;
            this.foliageHeight = 5;
            this.foliageDensity = 1.0D;
        }

        public BigTreeFeature create() {
            return new BigTreeFeature(this.placeOn, this.replace, this.log, this.leaves, this.altLeaves, this.vine, this.hanging, this.trunkFruit, this.minHeight, this.maxHeight, this.trunkWidth, this.foliageHeight, this.foliageDensity);
        }
    }
}
