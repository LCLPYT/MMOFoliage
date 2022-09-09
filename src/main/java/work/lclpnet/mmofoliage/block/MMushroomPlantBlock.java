package work.lclpnet.mmofoliage.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;
import work.lclpnet.mmofoliage.module.PlantsModule;

import java.util.Random;

public class MMushroomPlantBlock extends MushroomPlantBlock implements IMMOBlock {

    public MMushroomPlantBlock(Settings settings) {
        // supplier is only a fallback in this case
        super(settings, () -> PlantsModule.FEATURE_HUGE_GLOWSHROOM);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public boolean trySpawningBigMushroom(ServerWorld serverWorld, BlockPos pos, BlockState state, Random random) {
        serverWorld.removeBlock(pos, false);
        ConfiguredFeature<?, ?> feature;
        if (this.equals(PlantsModule.glowshroom)) {
            feature = PlantsModule.FEATURE_HUGE_GLOWSHROOM.value();
        } else {
            if (this != PlantsModule.toadstool) {
                serverWorld.setBlockState(pos, state, 3);
                return false;
            }

            feature = PlantsModule.HUGE_TOADSTOOL.value();
        }

        if (feature.generate(serverWorld, serverWorld.getChunkManager().getChunkGenerator(), random, pos)) {
            return true;
        } else {
            serverWorld.setBlockState(pos, state, 3);
            return false;
        }
    }
}
