package net.extendeddrawersaddon.init;

import io.github.mattidragon.extendeddrawers.registry.ModBlocks;
import net.extendeddrawersaddon.screen.DrawerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SINGLE_DRAWER, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DOUBLE_DRAWER, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.QUAD_DRAWER, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADOW_DRAWER, RenderLayer.getCutoutMipped());

        HandledScreens.register(ScreenInit.DRAWER, DrawerScreen::new);
    }

}
