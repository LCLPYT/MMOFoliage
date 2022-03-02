package work.lclpnet.mmofoliage.asm.mixin.common;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.mmofoliage.asm.type.IFoliageTreeFeature;

import java.util.Random;
import java.util.function.BiConsumer;

@Mixin(TreeFeature.class)
public class MixinTreeFeature {

    @Inject(
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Ljava/util/function/BiConsumer;Ljava/util/function/BiConsumer;Lnet/minecraft/world/gen/feature/TreeFeatureConfig;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onGenerate(StructureWorldAccess world, Random random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkReplacer, BiConsumer<BlockPos, BlockState> foliageReplacer, TreeFeatureConfig config, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof IFoliageTreeFeature customFeature) {
            cir.setReturnValue(customFeature.generateCustomTree(world, random, pos, trunkReplacer, foliageReplacer, config));
        }
    }
}
