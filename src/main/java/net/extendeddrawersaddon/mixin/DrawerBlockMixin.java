package net.extendeddrawersaddon.mixin;

import io.github.mattidragon.extendeddrawers.block.DrawerBlock;
import io.github.mattidragon.extendeddrawers.block.base.StorageDrawerBlock;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
import io.github.mattidragon.extendeddrawers.item.DrawerItem;
import io.github.mattidragon.extendeddrawers.item.UpgradeItem;
import io.github.mattidragon.extendeddrawers.registry.ModTags;
import io.github.mattidragon.extendeddrawers.storage.ModifierAccess;
import net.extendeddrawersaddon.access.DrawerInteractionHandlerAccess;
import net.extendeddrawersaddon.access.DrawerStorageAccess;
import net.extendeddrawersaddon.init.ComponentInit;
import net.extendeddrawersaddon.init.ConfigInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(DrawerBlock.class)
public abstract class DrawerBlockMixin extends StorageDrawerBlock<DrawerBlockEntity> implements DrawerInteractionHandlerAccess {

    @Shadow
    @Final
    public int slots;
    @Unique
    private boolean showCount = false;

    @Nullable
    @Unique
    private PlayerEntity playerEntity = null;

    public DrawerBlockMixin(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public PlayerEntity getDrawerInteractionPlayerEntity() {
        return this.playerEntity;
    }

    @Override
    public void setDrawerInteractionPlayerEntity(@Nullable PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> list = super.getDroppedStacks(state, builder);
        for (ItemStack stack : list) {
            if (stack.getItem() instanceof DrawerItem) {
                stack.set(ComponentInit.DRAWER_SHOW_COUNT, this.showCount);
            }
        }
        return list;
    }


    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && world.getBlockEntity(pos) instanceof DrawerBlockEntity drawerBlockEntity && drawerBlockEntity.storages.length > 0) {
            this.showCount = ((DrawerStorageAccess) (Object) drawerBlockEntity.storages[0]).getShowDrawerSlotCount();
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isSneaking() && player.getMainHandStack().isEmpty()) {
            if (!world.isClient()) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
            return ActionResult.success(world.isClient());
        } else {
            return super.onUse(state, world, pos, player, hit);
        }
    }

    @Override
    public ActionResult toggleLock(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        ActionResult actionResult = super.toggleLock(state, world, pos, hitPos, side);

        if (!world.isClient() && actionResult.isAccepted() && this.playerEntity != null) {
            ModifierAccess access = this.tryGetModifierAccess(state, world, pos, hitPos, side);
            if (access.isLocked()) {
                if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_LOCK)) {
                    this.playerEntity.getMainHandStack().decrement(1);
                } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_LOCK)) {
                    this.playerEntity.getOffHandStack().decrement(1);
                }
            } else {
                if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_LOCK)) {
                    this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getMainHandStack().copy());
                } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_LOCK)) {
                    this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getOffHandStack().copy());
                }
            }
        }
        return actionResult;
    }

    @Override
    public ActionResult toggleVoid(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        ActionResult actionResult = super.toggleVoid(state, world, pos, hitPos, side);

        if (!world.isClient() && actionResult.isAccepted() && this.playerEntity != null) {
            ModifierAccess access = this.tryGetModifierAccess(state, world, pos, hitPos, side);
            if (access.isVoiding()) {
                if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_VOIDING)) {
                    this.playerEntity.getMainHandStack().decrement(1);
                } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_VOIDING)) {
                    this.playerEntity.getOffHandStack().decrement(1);
                }
            } else {
                if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_VOIDING)) {
                    this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getMainHandStack().copy());
                } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_VOIDING)) {
                    this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getOffHandStack().copy());
                }
            }
        }
        return actionResult;
    }

    @Override
    public ActionResult toggleDuping(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        ActionResult actionResult = super.toggleDuping(state, world, pos, hitPos, side);

        if (!world.isClient() && actionResult.isAccepted() && this.playerEntity != null) {
            ModifierAccess access = this.tryGetModifierAccess(state, world, pos, hitPos, side);
            if (access.isDuping()) {
                if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_DUPING)) {
                    this.playerEntity.getMainHandStack().decrement(1);
                } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_DUPING)) {
                    this.playerEntity.getOffHandStack().decrement(1);
                }
            } else {
                if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_DUPING)) {
                    this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getMainHandStack().copy());
                } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_DUPING)) {
                    this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getOffHandStack().copy());
                }
            }
        }
        return actionResult;
    }

    @Override
    public ActionResult toggleHide(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side) {
        ActionResult actionResult = super.toggleHide(state, world, pos, hitPos, side);
        if (!world.isClient() && actionResult.isAccepted() && this.playerEntity != null) {
            if (ConfigInit.CONFIG.affectAllSlots) {
                ModifierAccess access = this.tryGetModifierAccess(state, world, pos, hitPos, side);
                for (int i = 0; i < getBlockEntity(world, pos).storages.length; i++) {
                    var storage = getBlockEntity(world, pos).storages[i];
                    storage.setHidden(access.isHidden());
                }
                if (access.isHidden()) {
                    if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_HIDDEN)) {
                        this.playerEntity.getMainHandStack().decrement(1);
                    } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_HIDDEN)) {
                        this.playerEntity.getOffHandStack().decrement(1);
                    }
                } else {
                    if (this.playerEntity.getMainHandStack().isIn(ModTags.ItemTags.TOGGLE_HIDDEN)) {
                        this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getMainHandStack().copy());
                    } else if (this.playerEntity.getOffHandStack().isIn(ModTags.ItemTags.TOGGLE_HIDDEN)) {
                        this.playerEntity.getInventory().offerOrDrop(this.playerEntity.getOffHandStack().copy());
                    }
                }

            }
        }
        return actionResult;
    }

//    @Override
//    public ActionResult changeLimiter(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, PlayerEntity player, ItemStack stack) {
//        ActionResult actionResult = super.changeLimiter(state, world, pos, hitPos, side, player, stack);
//        return actionResult;
//    }

    @Override
    public ActionResult changeUpgrade(BlockState state, World world, BlockPos pos, Vec3d hitPos, Direction side, PlayerEntity player, ItemStack stack) {
        ModifierAccess access = this.tryGetModifierAccess(state, world, pos, hitPos, side);
        if (access != null && access.getUpgrade() != null && access.getUpgrade().equals(stack.getItem())) {
            return ActionResult.FAIL;
        } else {
            return super.changeUpgrade(state, world, pos, hitPos, side, player, stack);
        }
    }

    @Override
    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) ((Object) blockEntity) : null;
    }

}
