package net.extendeddrawersaddon.init;

import net.extendeddrawersaddon.screen.DrawerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenInit {

    public static ScreenHandlerType<DrawerScreenHandler> DRAWER = new ExtendedScreenHandlerType<>(DrawerScreenHandler::new);

    public static void init() {
        Registry.register(Registries.SCREEN_HANDLER, "drawer", DRAWER);
    }

}
