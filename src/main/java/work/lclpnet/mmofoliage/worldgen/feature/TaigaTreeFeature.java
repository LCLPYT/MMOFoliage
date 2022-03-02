package work.lclpnet.mmofoliage.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.util.Randoms;
import work.lclpnet.mmofoliage.worldgen.config.TaigaTreeConfig;

import java.util.Random;
import java.util.function.BiConsumer;

public class TaigaTreeFeature extends AbstractTreeFeature<TaigaTreeConfig> {

    public TaigaTreeFeature(Codec<TaigaTreeConfig> codec) {
        super(codec);
    }

    public boolean checkSpace(WorldAccess world, BlockPos pos, int baseHeight, int height, TaigaTreeConfig config) {
        for (int y = 0; y <= height; ++y) {
            int trunkWidth = config.trunkWidth * (height - y) / height + 1;
            int trunkStart = MathHelper.ceil(0.25D - (double) trunkWidth / 2.0D);
            int trunkEnd = MathHelper.floor(0.25D + (double) trunkWidth / 2.0D);
            int start = y <= baseHeight ? trunkStart : trunkStart - 1;
            int end = y <= baseHeight ? trunkEnd : trunkEnd + 1;

            for (int x = start; x <= end; ++x) {
                for (int z = start; z <= end; ++z) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (pos1.getY() >= 255 || !this.isReplaceable(world, pos1)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void generateLeafLayer(WorldAccess world, Random rand, BlockPos pos, int leavesRadius, int trunkStart, int trunkEnd, BiConsumer<BlockPos, BlockState> leaves, TaigaTreeConfig config) {
        int start = trunkStart - leavesRadius;
        int end = trunkEnd + leavesRadius;

        for (int x = start; x <= end; ++x) {
            for (int z = start; z <= end; ++z) {
                if (leavesRadius <= 0 || x != start && x != end || z != start && z != end) {
                    int distFromTrunk = (x < 0 ? trunkStart - x : x - trunkEnd) + (z < 0 ? trunkStart - z : z - trunkEnd);
                    if (distFromTrunk < 4 || distFromTrunk == 4 && rand.nextInt(2) == 0) {
                        this.placeLeaves(world, pos.add(x, 0, z), leaves, config);
                    }
                }
            }
        }
    }

    public void generateBranch(WorldAccess world, Random rand, BlockPos pos, Direction direction, int length, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TaigaTreeConfig config) {
        Direction.Axis axis = direction.getAxis();
        Direction sideways = direction.rotateYClockwise();

        for (int i = 1; i <= length; ++i) {
            BlockPos pos1 = pos.offset(direction, i);
            int r = i != 1 && i != length ? 2 : 1;

            for (int j = -r; j <= r; ++j) {
                if (i < length || rand.nextInt(2) == 0) {
                    this.placeLeaves(world, pos1.offset(sideways, j), leaves, config);
                }
            }

            if (length - i > 2) {
                this.placeLeaves(world, pos1.up(), leaves, config);
                this.placeLeaves(world, pos1.up().offset(sideways, -1), leaves, config);
                this.placeLeaves(world, pos1.up().offset(sideways, 1), leaves, config);
                this.placeLog(world, pos1, axis, logs, config);
            }
        }
    }

    @Override
    public boolean generateCustomTree(StructureWorldAccess world, Random random, BlockPos startPos, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TreeFeatureConfig configBase) {
        TaigaTreeConfig config = (TaigaTreeConfig) configBase;

        while (startPos.getY() > world.getDimension().getMinimumY() && world.isAir(startPos) || world.getBlockState(startPos).getMaterial() == Material.LEAVES) {
            startPos = startPos.down();
        }

        int height = Randoms.getRandomInt(config.minHeight, config.maxHeight, random);
        int baseHeight = Randoms.getRandomInt(height / 5, height / 3, random);
        int leavesHeight = height - baseHeight;
        if (leavesHeight < 3) {
            return false;
        } else if (!this.checkSpace(world, startPos.up(), baseHeight, height, config)) {
            return false;
        } else {
            BlockPos pos = startPos.up(height);
            this.placeLeaves(world, pos, leaves, config);
            pos.down();

            int y;
            int trunkWidth;
            int trunkStart;
            int trunkEnd;
            int x;
            for (y = 0; y < leavesHeight; ++y) {
                trunkWidth = config.trunkWidth * y / height + 1;
                trunkStart = MathHelper.ceil(0.25D - (double) trunkWidth / 2.0D);
                trunkEnd = MathHelper.floor(0.25D + (double) trunkWidth / 2.0D);
                x = Math.min(Math.min((y + 2) / 3, 3 + (leavesHeight - y)), 6);
                if (x == 0) {
                    this.placeLeaves(world, pos, leaves, config);
                } else if (x < 4) {
                    if (y % 2 == 0) {
                        this.generateLeafLayer(world, random, pos, x, trunkStart, trunkEnd, leaves, config);
                    } else {
                        this.generateLeafLayer(world, random, pos, x / 2, trunkStart, trunkEnd, leaves, config);
                    }
                } else if (y % 2 == 0) {
                    this.generateBranch(world, random, pos.add(trunkStart, 0, trunkStart), Direction.NORTH, x, logs, leaves, config);
                    this.generateBranch(world, random, pos.add(trunkEnd, 0, trunkStart), Direction.EAST, x, logs, leaves, config);
                    this.generateBranch(world, random, pos.add(trunkEnd, 0, trunkEnd), Direction.SOUTH, x, logs, leaves, config);
                    this.generateBranch(world, random, pos.add(trunkStart, 0, trunkEnd), Direction.WEST, x, logs, leaves, config);
                }

                pos = pos.down();
            }

            for (y = 0; y < height - 1; ++y) {
                trunkWidth = config.trunkWidth * (height - y) / height + 1;
                trunkStart = MathHelper.ceil(0.25D - (double) trunkWidth / 2.0D);
                trunkEnd = MathHelper.floor(0.25D + (double) trunkWidth / 2.0D);
                if (trunkWidth < 1 || trunkWidth > config.trunkWidth) {
                    trunkStart = 0;
                    trunkEnd = 0;
                }

                for (x = trunkStart; x <= trunkEnd; ++x) {
                    for (int z = trunkStart; z <= trunkEnd; ++z) {
                        this.placeLog(world, startPos.add(x, y, z), logs, config);
                    }
                }
            }

            return true;
        }
    }
}
