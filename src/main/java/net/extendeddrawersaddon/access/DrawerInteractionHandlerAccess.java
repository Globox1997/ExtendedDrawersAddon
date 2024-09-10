package net.extendeddrawersaddon.access;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface DrawerInteractionHandlerAccess {

    @Nullable
    public PlayerEntity getDrawerInteractionPlayerEntity();

    public void setDrawerInteractionPlayerEntity(@Nullable PlayerEntity playerEntity);
}
