package work.lclpnet.mmofoliage.asm.mixin.client;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.mmofoliage.block.MSignType;

@Mixin(TexturedRenderLayers.class)
public class MixinTexturedRenderLayers {

    @Shadow @Final public static Identifier SIGNS_ATLAS_TEXTURE;

    @Inject(
            method = "getSignTextureId",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getCustomSignTextureId(SignType type, CallbackInfoReturnable<SpriteIdentifier> cir) {
        if (type instanceof MSignType) {
            String namespace = ((MSignType) type).getId().getNamespace();
            Identifier texture = new Identifier(namespace, String.format("entity/signs/%s", type.getName()));
            cir.setReturnValue(new SpriteIdentifier(SIGNS_ATLAS_TEXTURE, texture));
        }
    }
}
