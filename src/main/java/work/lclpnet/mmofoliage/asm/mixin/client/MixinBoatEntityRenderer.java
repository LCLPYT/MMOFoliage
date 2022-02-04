package work.lclpnet.mmofoliage.asm.mixin.client;

import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.mmofoliage.entity.MBoatEntity;
import work.lclpnet.mmofoliage.entity.MBoatType;

@Mixin(BoatEntityRenderer.class)
public class MixinBoatEntityRenderer {

    @Inject(
            method = "getTexture(Lnet/minecraft/entity/vehicle/BoatEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getCustomTexture(BoatEntity boatEntity, CallbackInfoReturnable<Identifier> cir) {
        if (!(boatEntity instanceof MBoatEntity)) return;

        MBoatType type = ((MBoatEntity) boatEntity).getMBoatType();
        if (type == null) return;

        String path = String.format("textures/entity/boat/%s.png", type.identifier.getPath());
        cir.setReturnValue(new Identifier(type.identifier.getNamespace(), path));
    }
}
