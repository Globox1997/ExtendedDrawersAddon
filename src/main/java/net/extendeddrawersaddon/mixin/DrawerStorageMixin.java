package net.extendeddrawersaddon.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
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

    @Shadow(remap = false)
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
    default void readNbtMixin(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo info) {
        ((DrawerStorageAccess) settings()).setShowDrawerSlotCount(nbt.getBoolean("ShowDrawerSlotCount"));
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    default void writeNbtMixin(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo info) {
        nbt.putBoolean("ShowDrawerSlotCount", ((DrawerStorageAccess) settings()).getShowDrawerSlotCount());
    }

    @WrapOperation(method = "changeUpgrade", at = @At(value = "INVOKE", target = "Lio/github/mattidragon/extendeddrawers/misc/ItemUtils;offerOrDrop(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V"))
    private void changeUpgradeMixin(World world, BlockPos blockPos, Direction direction, PlayerEntity player, ItemStack stack, Operation<Void> original) {
        if (player != null && !player.isSneaking()) {
        } else {
            original.call(world, blockPos, direction, player, stack);
        }
    }

    @Shadow(remap = false)
    default void update() {
    }

}
