package work.lclpnet.mmofoliage.worldgen.feature;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofoliage.util.FTF;
import work.lclpnet.mmofoliage.util.Randoms;
import work.lclpnet.mmofoliage.worldgen.IBlockPosQuery;

import java.util.Random;
import java.util.Set;

import static work.lclpnet.mmofoliage.module.AdditionalWoodModule.palm;

public class PalmTreeFeature extends TreeFeatureBase {
    
    protected PalmTreeFeature(IBlockPosQuery placeOn, IBlockPosQuery replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight) {
        super(placeOn, replace, log, leaves, altLeaves, vine, hanging, trunkFruit, minHeight, maxHeight);
    }

    @Override
    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, WorldAccess world, Random random, BlockPos startPos, BlockBox boundingBox) {
        while(startPos.getY() > 1 && world.isAir(startPos) || world.getBlockState(startPos).getMaterial() == Material.LEAVES) {
            startPos = startPos.down();
        }

        if (!this.placeOn.matches(world, startPos)) {
            return false;
        } else {
            int height = Randoms.getRandomInt(this.minHeight, this.maxHeight, random);
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
                            this.placeLog(world, offsetPos, changedLogs, boundingBox);
                            this.generateLeavesTop(world, offsetPos, changedLeaves, boundingBox);
                            break;
                        }

                        this.placeLog(world, offsetPos, changedLogs, boundingBox);
                    }

                    return true;
                }
            }
        }
    }

    public boolean checkSpace(WorldAccess world, BlockPos pos, int height, int radius) {
        for(int y = 0; y <= height; ++y) {
            for(int x = -radius; x <= radius; ++x) {
                for(int z = -radius; z <= radius; ++z) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (pos1.getY() >= 255 || !this.replace.matches(world, pos1)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void generateLeavesTop(WorldAccess world, BlockPos pos, Set<BlockPos> changedLeaves, BlockBox boundingBox) {
        this.placeLeaves(world, pos.add(2, -1, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-2, -1, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, -1, 2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, -1, -2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(1, 0, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-1, 0, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, 0, 1), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, 0, -1), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(2, 0, 2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-2, 0, -2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(2, 0, -2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-2, 0, 2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(1, 1, -1), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-1, 1, 1), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(1, 1, 1), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-1, 1, -1), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, 1, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(2, 2, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(-2, 2, 0), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, 2, 2), changedLeaves, boundingBox);
        this.placeLeaves(world, pos.add(0, 2, -2), changedLeaves, boundingBox);
    }

    public static class Builder extends BuilderBase<PalmTreeFeature.Builder, PalmTreeFeature> {
        public Builder() {
            this.placeOn = (world, pos) -> {
                Block ground = world.getBlockState(pos).getBlock();
                return FTF.canSustainSapling(Blocks.OAK_SAPLING, world, pos) || ground instanceof SandBlock;
            };
            this.minHeight = 10;
            this.maxHeight = 14;
            this.log = palm.log.getDefaultState();
            this.leaves = palm.leaves.getDefaultState().with(LeavesBlock.PERSISTENT, true);
        }

        public PalmTreeFeature create() {
            return new PalmTreeFeature(this.placeOn, this.replace, this.log, this.leaves, this.altLeaves, this.vine, this.hanging, this.trunkFruit, this.minHeight, this.maxHeight);
        }
    }
}
