package work.lclpnet.mmofoliage.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.worldgen.config.TwigletTreeConfig;

import java.util.Random;
import java.util.function.BiConsumer;

public class TwigletTreeFeature extends AbstractTreeFeature<TwigletTreeConfig> {

    public TwigletTreeFeature(Codec<TwigletTreeConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generateCustomTree(StructureWorldAccess world, Random random, BlockPos startPos, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TreeFeatureConfig configBase) {
        final TwigletTreeConfig config = (TwigletTreeConfig) configBase;

        while (startPos.getY() > world.getDimension().getMinimumY() && world.isAir(startPos) || world.getBlockState(startPos).getMaterial() == Material.LEAVES) {
            startPos = startPos.down();
        }

        int height = config.minHeight + random.nextInt(1 + config.maxHeight - config.minHeight);
        int baseHeight = height / 3;
        BlockPos pos = startPos.up();

        for (int y = 0; y < height; ++y) {
            if (!this.placeLog(world, pos.up(y), logs, config)) {
                return true;
            }

            float leafChance = (height - y) % 2 == 0 ? config.leafChanceEven : config.leafChanceOdd;
            if (y > baseHeight) {
                if (random.nextFloat() < leafChance) {
                    this.placeLeaves(world, pos.add(1, y, 0), leaves, config);
                }

                if (random.nextFloat() < leafChance) {
                    this.placeLeaves(world, pos.add(-1, y, 0), leaves, config);
                }

                if (random.nextFloat() < leafChance) {
                    this.placeLeaves(world, pos.add(0, y, 1), leaves, config);
                }

                if (random.nextFloat() < leafChance) {
                    this.placeLeaves(world, pos.add(0, y, -1), leaves, config);
                }

                for (Direction dir : Direction.Type.HORIZONTAL) {
                    BlockPos fruitPos = pos.add(dir.getOffsetX(), y, dir.getOffsetZ());
                    BlockState trunkFruit = config.trunkFruitProvider.getBlockState(random, fruitPos);
                    if (trunkFruit.getBlock() != Blocks.AIR && random.nextInt(4) == 0) {
                        if (trunkFruit.getBlock() == Blocks.COCOA) {
                            fruitPos = pos.add(dir.getOpposite().getOffsetX(), 0, dir.getOpposite().getOffsetZ());
                        }

                        this.generateTrunkFruit(world, random.nextInt(3), fruitPos, dir, config);
                    }
                }
            }
        }

        this.placeLeaves(world, pos.add(0, height, 0), leaves, config);
        return true;
    }

    private void generateTrunkFruit(WorldAccess world, int age, BlockPos pos, Direction direction, TwigletTreeConfig config) {
        BlockState trunkFruit = config.trunkFruitProvider.getBlockState(world.getRandom(), pos);
        if (trunkFruit == Blocks.COCOA.getDefaultState()) {
            if (world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() instanceof PlantBlock) {
                this.setBlockState(world, pos, trunkFruit.with(CocoaBlock.AGE, age).with(CocoaBlock.FACING, direction));
            }
        } else if (world.getBlockState(pos).getBlock() == Blocks.AIR || world.getBlockState(pos).getBlock() instanceof PlantBlock) {
            this.setBlockState(world, pos, trunkFruit.with(CocoaBlock.FACING, direction));
        }
    }
}
