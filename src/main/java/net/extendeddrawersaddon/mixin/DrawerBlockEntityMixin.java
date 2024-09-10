package net.extendeddrawersaddon.mixin;

import io.github.mattidragon.extendeddrawers.block.DrawerBlock;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
import io.github.mattidragon.extendeddrawers.block.entity.StorageDrawerBlockEntity;
import io.github.mattidragon.extendeddrawers.storage.DrawerSlot;
import net.extendeddrawersaddon.access.DrawerStorageAccess;
import net.extendeddrawersaddon.init.ComponentInit;
import net.extendeddrawersaddon.network.packet.DrawerData;
import net.extendeddrawersaddon.screen.DrawerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(DrawerBlockEntity.class)
public abstract class DrawerBlockEntityMixin extends StorageDrawerBlockEntity implements ExtendedScreenHandlerFactory<DrawerData> {

    @Shadow(remap = false)
    @Mutable
    @Final
    public DrawerSlot[] storages;

    public DrawerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "readComponents", at = @At("TAIL"))
    protected void readComponentsMixin(ComponentsAccess components, CallbackInfo info) {
        if (components.get(ComponentInit.DRAWER_SHOW_COUNT) != null && this.storages.length > 0) {
            ((DrawerStorageAccess) (Object) this.storages[0]).setShowDrawerSlotCount(components.get(ComponentInit.DRAWER_SHOW_COUNT).booleanValue());
        }
    }

    // Won't work since components are only used when set in loot table
    @Inject(method = "addComponents", at = @At("TAIL"))
    protected void addComponentsMixin(ComponentMap.Builder componentMapBuilder, CallbackInfo info) {
        if (this.storages.length > 0) {
            componentMapBuilder.add(ComponentInit.DRAWER_SHOW_COUNT, ((DrawerStorageAccess) (Object) this.storages[0]).getShowDrawerSlotCount());
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.drawer");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        List<DrawerSlot> list = new ArrayList<DrawerSlot>();
        Collections.addAll(list, this.storages);
        return new DrawerScreenHandler(syncId, playerInventory, list, 4 + list.size() * 2, this.getPos(), this.getCachedState().get(DrawerBlock.FACING));
    }

    @Override
    public DrawerData getScreenOpeningData(ServerPlayerEntity player) {
        return new DrawerData(this.storages.length);
    }

}
