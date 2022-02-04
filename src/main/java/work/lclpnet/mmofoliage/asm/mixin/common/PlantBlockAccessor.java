package work.lclpnet.mmofoliage.asm.mixin.common;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlantBlock.class)
public interface PlantBlockAccessor {

    @Invoker
    boolean invokeCanPlantOnTop(BlockState floor, BlockView world, BlockPos pos);
}
