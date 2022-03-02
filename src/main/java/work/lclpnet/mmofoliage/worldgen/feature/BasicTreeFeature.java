package work.lclpnet.mmofoliage.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.util.Randoms;
import work.lclpnet.mmofoliage.worldgen.config.BasicTreeConfig;

import java.util.Random;
import java.util.function.BiConsumer;

public class BasicTreeFeature extends AbstractTreeFeature<BasicTreeConfig> {

    public BasicTreeFeature(Codec<BasicTreeConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generateCustomTree(StructureWorldAccess world, Random random, BlockPos pos, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TreeFeatureConfig configBase) {
        final BasicTreeConfig config = (BasicTreeConfig) configBase;

        int height = Randoms.getRandomInt(config.minHeight, config.maxHeight, random);
        boolean hasSpace = true;
        final int dimensionMinY = world.getDimension().getMinimumY(), dimensionHeight = world.getDimension().getHeight();

        if (pos.getY() >= 1 && pos.getY() + height + 1 <= dimensionHeight) {
            for (int y = pos.getY(); y <= pos.getY() + 1 + height; y++) {
                int radius = 1;
                if (y == pos.getY()) radius = 0;
                if (y >= pos.getY() + 1 + height - 2) radius = 2;
                for (int x = pos.getX() - radius; x <= pos.getX() + radius && hasSpace; x++) {
                    for (int z = pos.getZ() - radius; z <= pos.getZ() + radius && hasSpace; z++) {
                        if (y >= dimensionMinY && y < dimensionHeight) {
                            if (!this.isReplaceable(world, new BlockPos(x, y, z))) {
                                hasSpace = false;
                            }
                        } else {
                            hasSpace = false;
                        }
                    }
                }
            }
            if (!hasSpace) return false;

            BlockPos soilPos = pos.down();
            world.setBlockState(soilPos, Blocks.DIRT.getDefaultState(), 3);

            int leafLayers = config.leafLayers - 1;
            int i;
            for (i = pos.getY() + height - leafLayers; i <= pos.getY() + height; i++) {
                int currentLayer = i - (pos.getY() + height);
                int leavesRadius = config.maxLeavesRadius - currentLayer / config.leavesLayerHeight;
                for (int x = pos.getX() - leavesRadius; x <= pos.getX() + leavesRadius; x++) {
                    int xDiff = x - pos.getX();
                    for (int z = pos.getZ() - leavesRadius; z <= pos.getZ() + leavesRadius; z++) {
                        int zDiff = z - pos.getZ();
                        if (Math.abs(xDiff) != leavesRadius || Math.abs(zDiff) != leavesRadius || (random.nextInt(2) != 0 && currentLayer != 0)) {
                            BlockPos leavesPos = new BlockPos(x, i, z);
                            if (this.isReplaceable(world, leavesPos)) {
                                if (config.altFoliageProvider.getBlockState(random, pos) != Blocks.AIR.getDefaultState()) {
                                    if (random.nextInt(4) == 0) {
                                        placeAltLeaves(world, leavesPos, leaves, config);
                                    } else {
                                        placeLeaves(world, leavesPos, leaves, config);
                                    }
                                } else {
                                    placeLeaves(world, leavesPos, leaves, config);
                                }
                            }
                        }
                    }
                }
            }
            generateTrunk(world, pos, height, logs, config);
            if (config.vineProvider.getBlockState(random, pos) != Blocks.AIR.getDefaultState()) {
                for (i = pos.getY() - leafLayers + height; i <= pos.getY() + height; i++) {
                    int currentLayer = i - (pos.getY() + height);
                    int leavesRadius = config.maxLeavesRadius + config.leavesOffset - currentLayer / config.leavesLayerHeight;
                    for (int x = pos.getX() - leavesRadius; x <= pos.getX() + leavesRadius; x++) {
                        for (int z = pos.getZ() - leavesRadius; z <= pos.getZ() + leavesRadius; z++) {
                            BlockPos blockpos3 = new BlockPos(x, i, z);
                            if (world.getBlockState(blockpos3).getMaterial() == Material.LEAVES) {
                                BlockPos westPos = blockpos3.west();
                                if (random.nextInt(4) == 0 && this.canPlaceVinesOn(world, westPos))
                                    extendVines(world, random, westPos, Direction.EAST, config);

                                BlockPos eastPos = blockpos3.east();
                                if (random.nextInt(4) == 0 && this.canPlaceVinesOn(world, eastPos))
                                    extendVines(world, random, eastPos, Direction.WEST, config);

                                BlockPos northPos = blockpos3.north();
                                if (random.nextInt(4) == 0 && this.canPlaceVinesOn(world, northPos))
                                    extendVines(world, random, northPos, Direction.SOUTH, config);

                                BlockPos southPos = blockpos3.south();
                                if (random.nextInt(4) == 0 && this.canPlaceVinesOn(world, southPos))
                                    extendVines(world, random, southPos, Direction.NORTH, config);
                            }
                        }
                    }
                }
            }

            if (config.hangingProvider.getBlockState(random, pos) != Blocks.AIR.getDefaultState()) {
                generateHanging(world, pos, random, height, config);
            }

            if (config.trunkFruitProvider.getBlockState(random, pos) != Blocks.AIR.getDefaultState()) {
                if (random.nextInt(5) == 0 && height > 5) {
                    for (int l3 = 0; l3 < 2; l3++) {
                        for (Direction dir : Direction.Type.HORIZONTAL) {
                            if (random.nextInt(4 - l3) == 0) {
                                Direction d = dir.getOpposite();
                                generateTrunkFruit(world, random, random.nextInt(3), pos.add(d.getOffsetX(), height - 5 + l3, d.getOffsetZ()), dir, config);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected void generateTrunk(WorldAccess world, BlockPos start, int height, BiConsumer<BlockPos, BlockState> logs, BasicTreeConfig config) {
        for (int layer = 0; layer < height; ++layer) {
            if (this.isReplaceable(world, start.up(layer))) {
                this.placeLog(world, start.up(layer), logs, config);
            }
        }
    }

    protected void generateHanging(WorldAccess world, BlockPos start, Random rand, int height, BasicTreeConfig config) {
        int y = start.getY() + (height - config.leafLayers);

        for (int x = start.getX() - (config.maxLeavesRadius + 1); x <= start.getX() + config.maxLeavesRadius + 1; ++x) {
            for (int z = start.getZ() - (config.maxLeavesRadius + 1); z <= start.getZ() + config.maxLeavesRadius + 1; ++z) {
                BlockPos hangingPos = new BlockPos(x, y, z);
                if (!world.isAir(hangingPos.up()) && world.isAir(hangingPos) && rand.nextFloat() <= config.hangingChance) {
                    this.setHanging(world, hangingPos, config);
                }
            }
        }
    }

    private void generateTrunkFruit(WorldAccess world, Random random, int age, BlockPos pos, Direction direction, BasicTreeConfig config) {
        if (config.trunkFruitProvider.getBlockState(random, pos) == Blocks.COCOA.getDefaultState()) {
            this.setBlockState(world, pos, config.trunkFruitProvider.getBlockState(random, pos)
                    .with(CocoaBlock.AGE, age)
                    .with(CocoaBlock.FACING, direction));
        } else {
            this.setBlockState(world, pos, config.trunkFruitProvider.getBlockState(random, pos)
                    .with(CocoaBlock.FACING, direction));
        }
    }

    private BlockState getVineStateForSide(Random random, BlockPos pos, Direction side, BasicTreeConfig config) {
        return config.vineProvider.getBlockState(random, pos).getBlock() instanceof VineBlock
                ? config.vineProvider.getBlockState(random, pos).with(VineBlock.getFacingProperty(side), true)
                : config.vineProvider.getBlockState(random, pos);
    }

    private void extendVines(WorldAccess world, Random random, BlockPos pos, Direction side, BasicTreeConfig config) {
        BlockState vineState = this.getVineStateForSide(random, pos, side, config);
        this.setBlockState(world, pos, vineState);
        int length = 4;

        for (pos = pos.down(); this.canPlaceVinesOn(world, pos) && length > 0; --length) {
            this.setBlockState(world, pos, vineState);
            pos = pos.down();
        }
    }

    protected boolean canPlaceVinesOn(WorldAccess world, BlockPos pos) {
        Material mat = world.getBlockState(pos).getMaterial();
        return mat == Material.AIR;
    }
}
