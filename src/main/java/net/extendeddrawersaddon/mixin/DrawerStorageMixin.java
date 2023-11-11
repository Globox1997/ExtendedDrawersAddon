package net.extendeddrawersaddon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.mattidragon.extendeddrawers.storage.DrawerStorage;
import io.github.mattidragon.extendeddrawers.storage.DrawerStorage.Settings;
import net.extendeddrawersaddon.access.DrawerStorageAccess;
import net.minecraft.nbt.NbtCompound;

@Mixin(DrawerStorage.class)
public interface DrawerStorageMixin extends DrawerStorageAccess {

    @Shadow
    Settings settings();

    @Override
    default boolean getShowDrawerSlotCount() {
        return ((DrawerStorageAccess) settings()).getShowDrawerSlotCount();
    }

    @Override
    default void setShowDrawerSlotCount(boolean showDrawerSlotCount) {
        ((DrawerStorageAccess) settings()).setShowDrawerSlotCount(showDrawerSlotCount);
        update();
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    default void readNbtMixin(NbtCompound nbt, CallbackInfo info) {
        ((DrawerStorageAccess) settings()).setShowDrawerSlotCount(nbt.getBoolean("ShowDrawerSlotCount"));
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    default void writeNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("ShowDrawerSlotCount", ((DrawerStorageAccess) settings()).getShowDrawerSlotCount());
    }

    @Shadow
    default void update() {
    }

}
