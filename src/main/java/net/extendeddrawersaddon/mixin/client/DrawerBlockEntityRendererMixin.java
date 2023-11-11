package net.extendeddrawersaddon.mixin.client;

import java.util.ArrayList;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.mattidragon.extendeddrawers.ExtendedDrawers;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
import io.github.mattidragon.extendeddrawers.client.renderer.AbstractDrawerBlockEntityRenderer;
import io.github.mattidragon.extendeddrawers.client.renderer.DrawerBlockEntityRenderer;
import io.github.mattidragon.extendeddrawers.config.category.ClientCategory;
import io.github.mattidragon.extendeddrawers.registry.ModItems;
import io.github.mattidragon.extendeddrawers.storage.DrawerSlot;
import net.extendeddrawersaddon.access.DrawerStorageAccess;
import net.extendeddrawersaddon.init.ConfigInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(DrawerBlockEntityRenderer.class)
public abstract class DrawerBlockEntityRendererMixin extends AbstractDrawerBlockEntityRenderer<DrawerBlockEntity> {

    private boolean showDrawerSlotCount = false;

    public DrawerBlockEntityRendererMixin(ItemRenderer itemRenderer, TextRenderer textRenderer) {
        super(itemRenderer, textRenderer);
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lio/github/mattidragon/extendeddrawers/client/renderer/DrawerBlockEntityRenderer;renderSlot(Lnet/fabricmc/fabric/api/transfer/v1/item/ItemVariant;Ljava/lang/String;ZZLjava/util/List;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIILnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void renderSlotMixin(DrawerSlot storage, boolean small, int light, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int seed, int overlay, BlockPos pos, World world,
            CallbackInfo info, ArrayList<Sprite> icons, ClientCategory.IconGroup config, Function<Identifier, Sprite> blockAtlas) {
        if (!ConfigInit.CONFIG.showDrawerIcons) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                icons.clear();
                if (storage.hasLimiter() && client.player.getMainHandStack().isOf(ModItems.LIMITER)) {
                    icons.add(blockAtlas.apply(ExtendedDrawers.id("item/limiter")));
                }
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lio/github/mattidragon/extendeddrawers/block/entity/DrawerBlockEntity;getPos()Lnet/minecraft/util/math/BlockPos;"))
    private void renderMixin(DrawerBlockEntity drawer, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        this.showDrawerSlotCount = ((DrawerStorageAccess) (Object) drawer.storages[0]).getShowDrawerSlotCount();
    }

    @Override
    public void renderText(String amount, boolean small, int light, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (this.showDrawerSlotCount) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            if (small) {
                matrices.translate(0, 0.25, -0.01);
            } else {
                matrices.translate(0, 0.5, -0.01);
            }
            matrices.scale(small ? 0.4f : 1.0f, small ? 0.4f : 1.0f, 1.0f);
            matrices.translate(0, 0.8f / -4.0f, -0.01f);

            if (small) {
                matrices.translate(0.0D, -0.027D, 0.09D);
                matrices.scale(0.5f, 0.5f, 0.5f);
            } else {
                matrices.translate(0.0D, -0.02D, 0.09D);
                matrices.scale(0.7f, 0.7f, 0.7f);
            }
            matrices.scale(0.02f, 0.02f, 0.02f);

            ((AbstractDrawerBlockEntityRendererAccessor) this).getTextRenderer().draw(amount, -((AbstractDrawerBlockEntityRendererAccessor) this).getTextRenderer().getWidth(amount) / 2f, 0, 0xffffff,
                    false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x000000, light);
            matrices.pop();
        }
    }
}
