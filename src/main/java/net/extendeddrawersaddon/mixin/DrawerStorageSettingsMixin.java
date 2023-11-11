package net.extendeddrawersaddon.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.mattidragon.extendeddrawers.storage.DrawerStorage;
import net.extendeddrawersaddon.access.DrawerStorageAccess;

@Mixin(DrawerStorage.Settings.class)
public class DrawerStorageSettingsMixin implements DrawerStorageAccess {

    private boolean showDrawerSlotCount = false;

    @Override
    public boolean getShowDrawerSlotCount() {
        return showDrawerSlotCount;
    }

    @Override
    public void setShowDrawerSlotCount(boolean showDrawerSlotCount) {
        this.showDrawerSlotCount = showDrawerSlotCount;
    }

}
