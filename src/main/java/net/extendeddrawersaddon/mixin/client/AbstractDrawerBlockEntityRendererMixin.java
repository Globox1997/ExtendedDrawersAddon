package net.extendeddrawersaddon.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.mattidragon.extendeddrawers.client.renderer.AbstractDrawerBlockEntityRenderer;
import net.extendeddrawersaddon.init.ConfigInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(AbstractDrawerBlockEntityRenderer.class)
public class AbstractDrawerBlockEntityRendererMixin {

    @Shadow
    @Mutable
    @Final
    private ItemRenderer itemRenderer;

    @Inject(method = "renderText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1))
    private void renderTextMixin(String amount, boolean small, int light, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        if (small) {
            matrices.translate(0.0D, -0.027D, 0.09D);
            matrices.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrices.translate(0.0D, -0.02D, 0.09D);
            matrices.scale(0.7f, 0.7f, 0.7f);
        }
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void renderItemMixin(ItemVariant itemVariant, boolean small, int light, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int seed, CallbackInfo info) {
        if (itemVariant.isBlank()) {
            return;
        }
        if (!ConfigInit.CONFIG.flatTextures) {
            matrices.push();
            if (small) {
                matrices.scale(0.25f, 0.25f, 0.25f);
                matrices.translate(0.0D, 0.0D, -0.21D);
            } else {
                matrices.scale(0.5f, 0.5f, 0.5f);
                matrices.translate(0.0D, 0.0D, -0.11D);
            }
            if ((ConfigInit.CONFIG.blockRenderScale < 0.999f || ConfigInit.CONFIG.blockRenderScale > 1.001) && itemVariant.getItem() instanceof BlockItem) {
                matrices.scale(ConfigInit.CONFIG.blockRenderScale, ConfigInit.CONFIG.blockRenderScale, ConfigInit.CONFIG.blockRenderScale);
            }

            var stack = itemVariant.toStack();
            var model = itemRenderer.getModel(stack, world, null, seed);

            itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);

            matrices.pop();

            info.cancel();
        }
    }

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiplyPositionMatrix(Lorg/joml/Matrix4f;)V"))
    private void renderFlatItemMixin(ItemVariant itemVariant, boolean small, int light, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int seed, CallbackInfo info) {
        if (small) {
            matrices.translate(0.0D, 0.0D, -0.07D);
        } else {
            matrices.translate(0.0D, 0.0D, -0.07D);
        }
    }
}
