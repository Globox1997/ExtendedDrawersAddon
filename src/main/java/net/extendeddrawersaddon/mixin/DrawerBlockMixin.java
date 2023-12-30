package net.extendeddrawersaddon.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import io.github.mattidragon.extendeddrawers.block.DrawerBlock;
import io.github.mattidragon.extendeddrawers.block.base.StorageDrawerBlock;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(DrawerBlock.class)
public abstract class DrawerBlockMixin extends StorageDrawerBlock<DrawerBlockEntity> {

    public DrawerBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isSneaking() && player.getStackInHand(hand).isEmpty()) {
            if (!world.isClient()) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
            ActionResult.success(world.isClient());
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public ActionResult toggleLock(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        return ActionResult.PASS;
    }

    @Override
    public ActionResult toggleVoid(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        return ActionResult.PASS;
    }

    @Override
    public ActionResult toggleDuping(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        return ActionResult.PASS;
    }

    @Override
    public ActionResult toggleHide(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        ActionResult result = super.toggleHide(state, world, pos, hitPos, side);
        if (!world.isClient() && result == ActionResult.SUCCESS) {
            if (ConfigInit.CONFIG.affectAllSlots) {
                for (int i = 0; i < getBlockEntity(world, pos).storages.length; i++) {
                    var storage = getBlockEntity(world, pos).storages[i];
                    storage.setHidden(!storage.isHidden());
                }
            }
        }
        return result;
    }

    @Override
    public ActionResult changeLimiter(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, PlayerEntity player, ItemStack stack) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        var access = tryGetModifierAccess(state, world, pos, hitPos, side);
        if (access == null) {
            return ActionResult.PASS;
        }
        if (access.hasLimiter()) {
            access.changeLimiter(ItemVariant.blank(), world, pos, side, player);
            return ActionResult.SUCCESS;
        } else {
            return super.changeLimiter(state, world, pos, hitPos, side, player, stack);
        }
    }

    @Override
    public ActionResult changeUpgrade(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, PlayerEntity player, ItemStack stack) {
        return ActionResult.PASS;
    }

    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) ((Object) blockEntity) : null;
    }

}
