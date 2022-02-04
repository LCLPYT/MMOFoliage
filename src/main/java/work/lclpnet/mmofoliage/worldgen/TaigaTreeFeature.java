package work.lclpnet.mmofoliage.worldgen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofoliage.util.Randoms;

import java.util.Random;
import java.util.Set;

public class TaigaTreeFeature extends TreeFeatureBase {
    private int trunkWidth;

    protected TaigaTreeFeature(IBlockPosQuery placeOn, IBlockPosQuery replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight, int trunkWidth) {
        super(placeOn, replace, log, leaves, altLeaves, vine, hanging, trunkFruit, minHeight, maxHeight);
        this.trunkWidth = trunkWidth;
    }

    public boolean checkSpace(WorldAccess world, BlockPos pos, int baseHeight, int height) {
        for (int y = 0; y <= height; ++y) {
            int trunkWidth = this.trunkWidth * (height - y) / height + 1;
            int trunkStart = MathHelper.ceil(0.25D - (double) trunkWidth / 2.0D);
            int trunkEnd = MathHelper.floor(0.25D + (double) trunkWidth / 2.0D);
            int start = y <= baseHeight ? trunkStart : trunkStart - 1;
            int end = y <= baseHeight ? trunkEnd : trunkEnd + 1;

            for (int x = start; x <= end; ++x) {
                for (int z = start; z <= end; ++z) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (pos1.getY() >= 255 || !this.replace.matches(world, pos1)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void generateLeafLayer(WorldAccess world, Random rand, BlockPos pos, int leavesRadius, int trunkStart, int trunkEnd, Set<BlockPos> changedLeaves, BlockBox blockBox) {
        int start = trunkStart - leavesRadius;
        int end = trunkEnd + leavesRadius;

        for (int x = start; x <= end; ++x) {
            for (int z = start; z <= end; ++z) {
                if (leavesRadius <= 0 || x != start && x != end || z != start && z != end) {
                    int distFromTrunk = (x < 0 ? trunkStart - x : x - trunkEnd) + (z < 0 ? trunkStart - z : z - trunkEnd);
                    if (distFromTrunk < 4 || distFromTrunk == 4 && rand.nextInt(2) == 0) {
                        this.placeLeaves(world, pos.add(x, 0, z), changedLeaves, blockBox);
                    }
                }
            }
        }
    }

    public void generateBranch(WorldAccess world, Random rand, BlockPos pos, Direction direction, int length, Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, BlockBox blockBox) {
        Direction.Axis axis = direction.getAxis();
        Direction sideways = direction.rotateYClockwise();

        for (int i = 1; i <= length; ++i) {
            BlockPos pos1 = pos.offset(direction, i);
            int r = i != 1 && i != length ? 2 : 1;

            for (int j = -r; j <= r; ++j) {
                if (i < length || rand.nextInt(2) == 0) {
                    this.placeLeaves(world, pos1.offset(sideways, j), changedLeaves, blockBox);
                }
            }

            if (length - i > 2) {
                this.placeLeaves(world, pos1.up(), changedLeaves, blockBox);
                this.placeLeaves(world, pos1.up().offset(sideways, -1), changedLeaves, blockBox);
                this.placeLeaves(world, pos1.up().offset(sideways, 1), changedLeaves, blockBox);
                this.placeLog(world, pos1, axis, changedLogs, blockBox);
            }
        }
    }

    @Override
    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, WorldAccess world, Random rand, BlockPos position, BlockBox boundingBox) {
        while (position.getY() > 1 && world.isAir(position) || world.getBlockState(position).getMaterial() == Material.LEAVES) {
            position = position.down();
        }

        int height;
        int baseHeight;
        for (height = 0; height <= this.trunkWidth - 1; ++height)
            for (baseHeight = 0; baseHeight <= this.trunkWidth - 1; ++baseHeight)
                if (!this.placeOn.matches(world, position.add(height, 0, baseHeight)))
                    return false;

        height = Randoms.getRandomInt(this.minHeight, this.maxHeight, rand);
        baseHeight = Randoms.getRandomInt(height / 5, height / 3, rand);
        int leavesHeight = height - baseHeight;
        if (leavesHeight < 3) {
            return false;
        } else if (!this.checkSpace(world, position.up(), baseHeight, height)) {
            return false;
        } else {
            BlockPos pos = position.up(height);
            this.placeLeaves(world, pos, changedLeaves, boundingBox);
            pos.down();

            int y;
            int trunkWidth;
            int trunkStart;
            int trunkEnd;
            int x;
            for (y = 0; y < leavesHeight; ++y) {
                trunkWidth = this.trunkWidth * y / height + 1;
                trunkStart = MathHelper.ceil(0.25D - (double) trunkWidth / 2.0D);
                trunkEnd = MathHelper.floor(0.25D + (double) trunkWidth / 2.0D);
                x = Math.min(Math.min((y + 2) / 3, 3 + (leavesHeight - y)), 6);
                if (x == 0) {
                    this.placeLeaves(world, pos, changedLeaves, boundingBox);
                } else if (x < 4) {
                    if (y % 2 == 0) {
                        this.generateLeafLayer(world, rand, pos, x, trunkStart, trunkEnd, changedLeaves, boundingBox);
                    } else {
                        this.generateLeafLayer(world, rand, pos, x / 2, trunkStart, trunkEnd, changedLeaves, boundingBox);
                    }
                } else if (y % 2 == 0) {
                    this.generateBranch(world, rand, pos.add(trunkStart, 0, trunkStart), Direction.NORTH, x, changedLogs, changedLeaves, boundingBox);
                    this.generateBranch(world, rand, pos.add(trunkEnd, 0, trunkStart), Direction.EAST, x, changedLogs, changedLeaves, boundingBox);
                    this.generateBranch(world, rand, pos.add(trunkEnd, 0, trunkEnd), Direction.SOUTH, x, changedLogs, changedLeaves, boundingBox);
                    this.generateBranch(world, rand, pos.add(trunkStart, 0, trunkEnd), Direction.WEST, x, changedLogs, changedLeaves, boundingBox);
                }

                pos = pos.down();
            }

            for (y = 0; y < height - 1; ++y) {
                trunkWidth = this.trunkWidth * (height - y) / height + 1;
                trunkStart = MathHelper.ceil(0.25D - (double) trunkWidth / 2.0D);
                trunkEnd = MathHelper.floor(0.25D + (double) trunkWidth / 2.0D);
                if (trunkWidth < 1 || trunkWidth > this.trunkWidth) {
                    trunkStart = 0;
                    trunkEnd = 0;
                }

                for (x = trunkStart; x <= trunkEnd; ++x) {
                    for (int z = trunkStart; z <= trunkEnd; ++z) {
                        this.placeLog(world, position.add(x, y, z), changedLogs, boundingBox);
                    }
                }
            }

            return true;
        }
    }

    public static class Builder extends BuilderBase<Builder, TaigaTreeFeature> {
        protected int trunkWidth;

        public Builder trunkWidth(int a) {
            this.trunkWidth = a;
            return this;
        }

        public Builder() {
            this.minHeight = 6;
            this.maxHeight = 12;
            this.log = Blocks.SPRUCE_LOG.getDefaultState();
            this.leaves = Blocks.SPRUCE_LEAVES.getDefaultState();
            this.vine = Blocks.VINE.getDefaultState();
            this.trunkWidth = 1;
        }

        public TaigaTreeFeature create() {
            return new TaigaTreeFeature(this.placeOn, this.replace, this.log, this.leaves, this.altLeaves, this.vine, this.hanging, this.trunkFruit, this.minHeight, this.maxHeight, this.trunkWidth);
        }
    }
}
