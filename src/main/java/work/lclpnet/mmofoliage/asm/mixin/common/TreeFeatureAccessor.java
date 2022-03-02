package work.lclpnet.mmofoliage.asm.mixin.common;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeFeature.class)
public interface TreeFeatureAccessor {

    @Invoker("setBlockStateWithoutUpdatingNeighbors")
    static void invokeSetBlockStateWithoutUpdatingNeighbors(ModifiableWorld world, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }
}
