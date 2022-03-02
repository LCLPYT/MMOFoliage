package work.lclpnet.mmofoliage.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import work.lclpnet.mmofoliage.module.SoilModule;
import work.lclpnet.mmofoliage.worldgen.BlockPosBiPredicate;

import java.util.Random;

public class HugeToadstoolFeature extends Feature<DefaultFeatureConfig> {

    protected BlockPosBiPredicate placeOn = (world, pos)
            -> world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK || world.getBlockState(pos).getBlock() == Blocks.MYCELIUM;
    protected BlockPosBiPredicate replace = (world, pos) -> {
        final BlockState state = world.getBlockState(pos);
        return world.isAir(pos) || state.isIn(BlockTags.LEAVES) || world.getBlockState(pos).getBlock() instanceof PlantBlock;
    };

    public HugeToadstoolFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        while (pos.getY() > world.getDimension().getMinimumY() && this.replace.matches(world, pos)) {
            pos = pos.down();
        }

        if (!this.placeOn.matches(world, pos.add(0, 0, 0))) {
            return false;
        } else if (!this.checkSpace(world, pos.up())) {
            return false;
        } else {
            BlockPos above = pos.up();
            int height = 2 + random.nextInt(3);
            int radius = 2;

            int x;
            for(x = 0; x < height; ++x) {
                this.setBlock(world, above.up(x), Blocks.MUSHROOM_STEM.getDefaultState());
            }

            for(x = -(radius - 1); x <= radius - 1; ++x) {
                for(int z = -(radius - 1); z <= radius - 1; ++z) {
                    this.setBlock(world, above.add(x, height, z), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
                    this.setBlock(world, above.add(x, height + 1, z), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
                    this.setBlock(world, above.add(x, height + 2, z), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
                }
            }

            this.setBlock(world, above.add(0, height + 3, 0), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
            this.setBlock(world, above.add(1, height + 3, 0), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
            this.setBlock(world, above.add(-1, height + 3, 0), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
            this.setBlock(world, above.add(0, height + 3, 1), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
            this.setBlock(world, above.add(0, height + 3, -1), SoilModule.toadstool_block.getDefaultState().with(MushroomBlock.DOWN, false));
            return true;
        }
    }

    public void setBlock(WorldAccess world, BlockPos pos, BlockState state) {
        if (this.replace.matches(world, pos)) {
            super.setBlockState(world, pos, state);
        }
    }

    public boolean checkSpace(WorldAccess world, BlockPos pos) {
        for(int y = 0; y <= 8; ++y) {
            for(int x = -2; x <= 2; ++x) {
                for(int z = -2; z <= 2; ++z) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (pos1.getY() >= 255 || !this.replace.matches(world, pos1)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
