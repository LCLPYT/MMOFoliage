package work.lclpnet.mmofoliage.asm.mixin.common;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.mmofoliage.asm.type.IFoliageTreeFeature;

import java.util.Random;
import java.util.Set;

@Mixin(TreeFeature.class)
public class MixinTreeFeature {

    @Inject(
            method = "generate(Lnet/minecraft/world/ModifiableTestableWorld;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Ljava/util/Set;Ljava/util/Set;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/world/gen/feature/TreeFeatureConfig;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onGenerate(ModifiableTestableWorld world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof IFoliageTreeFeature) {
            IFoliageTreeFeature customFeature = (IFoliageTreeFeature) this;
            cir.setReturnValue(customFeature.generateCustomTree(world, random, pos, logPositions, leavesPositions, box, config));
        }
    }
}
