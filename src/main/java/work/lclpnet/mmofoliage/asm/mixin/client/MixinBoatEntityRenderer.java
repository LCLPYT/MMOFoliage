package work.lclpnet.mmofoliage.asm.mixin.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.mmofoliage.entity.MBoatEntity;
import work.lclpnet.mmofoliage.entity.MBoatType;

import java.util.Map;

@Mixin(BoatEntityRenderer.class)
public abstract class MixinBoatEntityRenderer extends EntityRenderer<BoatEntity> {

    @Mutable
    @Shadow @Final private Map<BoatEntity.Type, Pair<Identifier, BoatEntityModel>> texturesAndModels;

    protected MixinBoatEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Shadow public abstract Identifier getTexture(BoatEntity boatEntity);

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

    @Inject(
            method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/entity/BoatEntityRenderer;texturesAndModels:Ljava/util/Map;",
                    opcode = Opcodes.GETFIELD
            ),
            cancellable = true
    )
    public void renderMBoatEntity(BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!(boatEntity instanceof MBoatEntity)) return;

        ci.cancel();

        Identifier identifier = getTexture(boatEntity);
        Pair<Identifier, BoatEntityModel> parentPair = this.texturesAndModels.get(BoatEntity.Type.OAK);
        if (parentPair == null) throw new IllegalStateException("No parent model found for MBoatEntity");

        BoatEntityModel boatEntityModel = parentPair.getSecond();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
        boatEntityModel.setAngles(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatEntityModel.getLayer(identifier));
        boatEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            boatEntityModel.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();

        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
