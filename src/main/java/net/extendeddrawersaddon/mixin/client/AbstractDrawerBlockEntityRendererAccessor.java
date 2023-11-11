package net.extendeddrawersaddon.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import io.github.mattidragon.extendeddrawers.client.renderer.AbstractDrawerBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;

@Environment(EnvType.CLIENT)
@Mixin(AbstractDrawerBlockEntityRenderer.class)
public interface AbstractDrawerBlockEntityRendererAccessor {

    @Accessor("textRenderer")
    TextRenderer getTextRenderer();

}
