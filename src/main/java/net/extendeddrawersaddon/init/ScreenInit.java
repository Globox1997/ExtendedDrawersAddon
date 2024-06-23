package net.extendeddrawersaddon.init;

import net.extendeddrawersaddon.network.packet.DrawerData;
import net.extendeddrawersaddon.screen.DrawerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenInit {

    // public static final ExtendedScreenHandlerType<OvenScreenHandler> OVEN =
    // new ExtendedScreenHandlerType((syncId, inventory, data) -> ..., OvenData.PACKET_CODEC);

    // Registry.register(Registry.SCREEN_HANDLER, Identifier.of(...), OVEN);

    // public static ScreenHandlerType<DrawerScreenHandler> DRAWER = new ExtendedScreenHandlerType<>(DrawerScreenHandler::new);

    public static ScreenHandlerType<DrawerScreenHandler> DRAWER = new ExtendedScreenHandlerType<>((syncId, inventory, data) -> {
        // int syncId, PlayerInventory playerInventory, PacketByteBuf buf
        return new DrawerScreenHandler(syncId, inventory, data.slotCount());
    }, DrawerData.PACKET_CODEC);

    public static void init() {
        Registry.register(Registries.SCREEN_HANDLER, "drawer", DRAWER);
    }

}
