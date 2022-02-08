package work.lclpnet.mmofoliage.worldgen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofoliage.util.Randoms;
import work.lclpnet.mmofoliage.worldgen.BlockPosBiPredicate;

import java.util.Random;
import java.util.Set;

public class BushTreeFeature extends TreeFeatureBase {

    protected BushTreeFeature(BlockPosBiPredicate placeOn, BlockPosBiPredicate replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight) {
        super(placeOn, replace, log, leaves, altLeaves, vine, hanging, trunkFruit, minHeight, maxHeight);
    }

    @Override
    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, WorldAccess world, Random random, BlockPos startPos, BlockBox boundingBox) {
        while (startPos.getY() > 1 && (world.isAir(startPos) || world.getBlockState(startPos).getMaterial() == Material.AIR)) {
            startPos = startPos.down();
        }

        if (!this.placeOn.matches(world, startPos)) {
            return false;
        } else {
            int height = Randoms.getRandomInt(this.minHeight, this.maxHeight, random);
            BlockPos pos = startPos.up();

            for (int y = 0; y < height; ++y) {
                if (height - y > 1) {
                    this.placeLog(world, pos.add(0, y, 0), changedLogs, boundingBox);
                }

                int leavesRadius = height - y > 1 ? 2 : 1;

                for (int x = -leavesRadius; x <= leavesRadius; ++x) {
                    for (int z = -leavesRadius; z <= leavesRadius; ++z) {
                        if (Math.abs(x) < leavesRadius || Math.abs(z) < leavesRadius || random.nextInt(2) != 0) {
                            if (this.altLeaves != Blocks.AIR.getDefaultState()) {
                                if (random.nextInt(4) == 0) {
                                    this.setAltLeaves(world, pos.add(x, y, z), changedLeaves, boundingBox);
                                } else {
                                    this.placeLeaves(world, pos.add(x, y, z), changedLeaves, boundingBox);
                                }
                            } else {
                                this.placeLeaves(world, pos.add(x, y, z), changedLeaves, boundingBox);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    public static class Builder extends BuilderBase<BushTreeFeature.Builder, BushTreeFeature> {
        public Builder() {
            this.minHeight = 2;
            this.maxHeight = 2;
        }

        public BushTreeFeature create() {
            return new BushTreeFeature(this.placeOn, this.replace, this.log, this.leaves, this.altLeaves, this.vine, this.hanging, this.trunkFruit, this.minHeight, this.maxHeight);
        }
    }
}
