package net.extendeddrawersaddon.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.github.mattidragon.extendeddrawers.block.DrawerBlock;
import io.github.mattidragon.extendeddrawers.block.base.NetworkBlockWithEntity;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
import io.github.mattidragon.extendeddrawers.storage.DrawerSlot;
import net.extendeddrawersaddon.init.ConfigInit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(DrawerBlock.class)
public abstract class DrawerBlockMixin extends NetworkBlockWithEntity<DrawerBlockEntity> {

    public DrawerBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUse", at = @At(value = "FIELD", target = "Lio/github/mattidragon/extendeddrawers/block/entity/DrawerBlockEntity;storages:[Lio/github/mattidragon/extendeddrawers/storage/DrawerSlot;", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onUseMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info, Vec2f internalPos, int slot,
            DrawerBlockEntity drawer) {
        if (player.isSneaking() && player.getStackInHand(hand).isEmpty()) {
            if (!world.isClient()) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
            info.setReturnValue(ActionResult.success(world.isClient()));
        }
    }

    @Inject(method = "changeLimiter", at = @At(value = "INVOKE", target = "Lio/github/mattidragon/extendeddrawers/storage/DrawerSlot;changeLimiter(Lnet/fabricmc/fabric/api/transfer/v1/item/ItemVariant;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/entity/player/PlayerEntity;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void changeLimiterMixin(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ActionResult> info,
            Vec2f facePos, DrawerSlot storage) {
        if (storage.hasLimiter()) {
            if (storage.changeLimiter(ItemVariant.blank(), world, pos, side, player)) {
                info.setReturnValue(ActionResult.success(world.isClient()));
            }
        }
    }

    @Inject(method = "toggleLock", at = @At("HEAD"), cancellable = true)
    private void toggleLockMixin(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, CallbackInfoReturnable<ActionResult> info) {
        if (ConfigInit.CONFIG.affectAllSlots) {
            for (int i = 0; i < getBlockEntity(world, pos).storages.length; i++) {
                var storage = getBlockEntity(world, pos).storages[i];
                storage.setLocked(!storage.isLocked());
            }
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "toggleVoid", at = @At("HEAD"), cancellable = true)
    private void toggleVoidMixin(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, CallbackInfoReturnable<ActionResult> info) {
        if (ConfigInit.CONFIG.affectAllSlots) {
            for (int i = 0; i < getBlockEntity(world, pos).storages.length; i++) {
                var storage = getBlockEntity(world, pos).storages[i];
                storage.setVoiding(!storage.isVoiding());
            }
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "toggleHide", at = @At("HEAD"), cancellable = true)
    private void toggleHideMixin(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, CallbackInfoReturnable<ActionResult> info) {
        if (ConfigInit.CONFIG.affectAllSlots) {
            for (int i = 0; i < getBlockEntity(world, pos).storages.length; i++) {
                var storage = getBlockEntity(world, pos).storages[i];
                storage.setHidden(!storage.isHidden());
            }
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "toggleDuping", at = @At("HEAD"), cancellable = true)
    private void toggleDupingMixin(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, CallbackInfoReturnable<ActionResult> info) {
        if (ConfigInit.CONFIG.affectAllSlots) {
            for (int i = 0; i < getBlockEntity(world, pos).storages.length; i++) {
                var storage = getBlockEntity(world, pos).storages[i];
                storage.setDuping(!storage.isDuping());
            }
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) ((Object) blockEntity) : null;
    }

}
