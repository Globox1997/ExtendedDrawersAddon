package net.extendeddrawersaddon.init;

import net.extendeddrawersaddon.ExtendedDrawersAddonMain;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EventInit {

    public static void init() {
        FabricLoader.getInstance().getModContainer("extendeddrawersaddon")
                .map(container -> ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("extendeddrawersaddon", "addon"), container, Text.literal("Extended Drawers Addon Pack"),
                        ResourcePackActivationType.ALWAYS_ENABLED))
                .filter(success -> !success).ifPresent(success -> ExtendedDrawersAddonMain.LOGGER.warn("Could not register built-in resource pack by extendeddrawersaddon."));
    }

}
