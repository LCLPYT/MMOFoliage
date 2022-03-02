package work.lclpnet.mmofoliage.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import work.lclpnet.mmofoliage.util.Randoms;
import work.lclpnet.mmofoliage.worldgen.config.PalmTreeConfig;

import java.util.Random;
import java.util.function.BiConsumer;

public class PalmTreeFeature extends AbstractTreeFeature<PalmTreeConfig> {

    public PalmTreeFeature(Codec<PalmTreeConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generateCustomTree(StructureWorldAccess world, Random random, BlockPos startPos, BiConsumer<BlockPos, BlockState> logs, BiConsumer<BlockPos, BlockState> leaves, TreeFeatureConfig configBase) {
        final PalmTreeConfig config = (PalmTreeConfig) configBase;

        while (startPos.getY() > world.getDimension().getMinimumY() && world.isAir(startPos) || world.getBlockState(startPos).getMaterial() == Material.LEAVES) {
            startPos = startPos.down();
        }

        int height = Randoms.getRandomInt(config.minHeight, config.maxHeight, random);
        int leavesRadius = 2;
        int heightMinusTop = height - leavesRadius - 1;

        if (height < 8) {
            return false;
        } else {
            BlockPos pos = startPos.up();
            if (!this.checkSpace(world, pos, height, 1)) {
                return false;
            } else {
                for(int step = 0; step <= heightMinusTop; ++step) {
                    BlockPos offsetPos = pos.up(step);

                    if (step == heightMinusTop) {
                        this.placeLog(world, offsetPos, logs, config);
                        this.generateLeavesTop(world, offsetPos, leaves, config);
                        break;
                    }

                    this.placeLog(world, offsetPos, logs, config);
                }

                return true;
            }
        }
    }

    public boolean checkSpace(WorldAccess world, BlockPos pos, int height, int radius) {
        for(int y = 0; y <= height; ++y) {
            for(int x = -radius; x <= radius; ++x) {
                for(int z = -radius; z <= radius; ++z) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (pos1.getY() >= 255 || !this.isReplaceable(world, pos1)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void generateLeavesTop(WorldAccess world, BlockPos pos, BiConsumer<BlockPos, BlockState> leaves, PalmTreeConfig config) {
        this.placeLeaves(world, pos.add(2, -1, 0), leaves, config);
        this.placeLeaves(world, pos.add(-2, -1, 0), leaves, config);
        this.placeLeaves(world, pos.add(0, -1, 2), leaves, config);
        this.placeLeaves(world, pos.add(0, -1, -2), leaves, config);
        this.placeLeaves(world, pos.add(1, 0, 0), leaves, config);
        this.placeLeaves(world, pos.add(-1, 0, 0), leaves, config);
        this.placeLeaves(world, pos.add(0, 0, 1), leaves, config);
        this.placeLeaves(world, pos.add(0, 0, -1), leaves, config);
        this.placeLeaves(world, pos.add(2, 0, 2), leaves, config);
        this.placeLeaves(world, pos.add(-2, 0, -2), leaves, config);
        this.placeLeaves(world, pos.add(2, 0, -2), leaves, config);
        this.placeLeaves(world, pos.add(-2, 0, 2), leaves, config);
        this.placeLeaves(world, pos.add(1, 1, -1), leaves, config);
        this.placeLeaves(world, pos.add(-1, 1, 1), leaves, config);
        this.placeLeaves(world, pos.add(1, 1, 1), leaves, config);
        this.placeLeaves(world, pos.add(-1, 1, -1), leaves, config);
        this.placeLeaves(world, pos.add(0, 1, 0), leaves, config);
        this.placeLeaves(world, pos.add(2, 2, 0), leaves, config);
        this.placeLeaves(world, pos.add(-2, 2, 0), leaves, config);
        this.placeLeaves(world, pos.add(0, 2, 2), leaves, config);
        this.placeLeaves(world, pos.add(0, 2, -2), leaves, config);
    }
}
