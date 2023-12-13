package net.extendeddrawersaddon.screen;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import io.github.mattidragon.extendeddrawers.item.UpgradeItem;
import io.github.mattidragon.extendeddrawers.registry.ModItems;
import io.github.mattidragon.extendeddrawers.storage.DrawerSlot;
import net.extendeddrawersaddon.access.DrawerStorageAccess;
import net.extendeddrawersaddon.init.ScreenInit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DrawerScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final World world;
    @Nullable
    private final BlockPos pos;
    @Nullable
    private final Direction direction;
    private final boolean isCreativeScreen;

    public DrawerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new ArrayList<DrawerSlot>(), 4 + buf.readInt() * 2, null, null);
    }

    public DrawerScreenHandler(int syncId, PlayerInventory playerInventory, List<DrawerSlot> drawerSlots, int slotSize, BlockPos pos, Direction direction) {
        super(ScreenInit.DRAWER, syncId);
        this.inventory = new SimpleInventory(slotSize);
        this.world = playerInventory.player.getWorld();
        this.pos = pos;
        this.direction = direction;
        this.isCreativeScreen = playerInventory.player.isCreative();
        int drawerSlotSize = drawerSlots.size();
        if (drawerSlotSize > 0) {
            if (drawerSlots.get(0).isLocked()) {
                this.inventory.setStack(0, new ItemStack(ModItems.LOCK));
            }
            if (drawerSlots.get(0).isVoiding()) {
                this.inventory.setStack(1, new ItemStack(Items.LAVA_BUCKET));
            }
            if (((DrawerStorageAccess) (Object) drawerSlots.get(0)).getShowDrawerSlotCount()) {
                this.inventory.setStack(2, new ItemStack(Items.WRITABLE_BOOK));
            }
            if (drawerSlots.get(0).isDuping()) {
                this.inventory.setStack(3, new ItemStack(ModItems.DUPE_WAND));
            }
            this.inventory.setStack(4, drawerSlots.get(0).getResource().toStack((int) drawerSlots.get(0).getTrueAmount()));
            if (drawerSlots.get(0).getUpgrade() != null) {
                this.inventory.setStack(5, drawerSlots.get(0).getUpgrade().getDefaultStack());
            }
            if (drawerSlotSize > 1) {
                this.inventory.setStack(6, drawerSlots.get(1).getResource().toStack((int) drawerSlots.get(1).getTrueAmount()));
                if (drawerSlots.get(1).getUpgrade() != null) {
                    this.inventory.setStack(7, drawerSlots.get(1).getUpgrade().getDefaultStack());
                }
                if (drawerSlotSize > 2) {
                    this.inventory.setStack(8, drawerSlots.get(2).getResource().toStack((int) drawerSlots.get(2).getTrueAmount()));
                    if (drawerSlots.get(2).getUpgrade() != null) {
                        this.inventory.setStack(9, drawerSlots.get(2).getUpgrade().getDefaultStack());
                    }
                    this.inventory.setStack(10, drawerSlots.get(3).getResource().toStack((int) drawerSlots.get(3).getTrueAmount()));
                    if (drawerSlots.get(3).getUpgrade() != null) {
                        this.inventory.setStack(11, drawerSlots.get(3).getUpgrade().getDefaultStack());
                    }
                }
            }
        }

        int slotExtraX = isCreativeScreen ? 0 : 9;
        this.addSlot(new Slot(inventory, 0, 53 + slotExtraX, 54) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.LOCK);
            }

            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                if (!world.isClient() && !stack.isEmpty() && stack.isOf(ModItems.LOCK)) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        drawerSlots.get(i).setLocked(true);
                        // maybe have to set blockentity markdirty???
                    }
                }
                super.setStackNoCallbacks(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getWorld().isClient()) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        drawerSlots.get(i).setLocked(false);
                    }

                }
                super.onTakeItem(player, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(inventory, 1, 71 + slotExtraX, 54) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAVA_BUCKET);
            }

            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                if (!world.isClient() && !stack.isEmpty() && stack.isOf(Items.LAVA_BUCKET)) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        drawerSlots.get(i).setVoiding(true);
                    }
                }
                super.setStackNoCallbacks(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getWorld().isClient()) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        drawerSlots.get(i).setVoiding(false);
                    }

                }
                super.onTakeItem(player, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(inventory, 2, 89 + slotExtraX, 54) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.WRITABLE_BOOK);
            }

            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                if (!world.isClient() && !stack.isEmpty() && stack.isOf(Items.WRITABLE_BOOK)) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        ((DrawerStorageAccess) (Object) drawerSlots.get(i)).setShowDrawerSlotCount(true);
                    }
                }
                super.setStackNoCallbacks(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getWorld().isClient()) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        ((DrawerStorageAccess) (Object) drawerSlots.get(i)).setShowDrawerSlotCount(false);
                    }

                }
                super.onTakeItem(player, stack);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(inventory, 3, 107 + slotExtraX, 54) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.DUPE_WAND);
            }

            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                if (!world.isClient() && !stack.isEmpty() && stack.isOf(ModItems.DUPE_WAND)) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        drawerSlots.get(i).setHidden(true);
                    }
                }
                super.setStackNoCallbacks(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getWorld().isClient()) {
                    for (int i = 0; i < drawerSlotSize; i++) {
                        drawerSlots.get(i).setHidden(false);
                    }
                }
                super.onTakeItem(player, stack);
            }

            @Override
            public boolean isEnabled() {
                return isCreativeScreen;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });

        slotExtraX = this.inventory.size() > 6 ? 0 : 20;
        int slotExtraY = this.inventory.size() < 9 ? 11 : 0;

        // Slot top left
        this.addSlot(new Slot(inventory, 4, 69 + slotExtraX, 9 + slotExtraY) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return false;
            }
        });
        this.addSlot(new Slot(inventory, 5, 51 + slotExtraX, 9 + slotExtraY) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof UpgradeItem;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public void setStackNoCallbacks(ItemStack stack) {
                if (!world.isClient() && !stack.isEmpty() && stack.getItem() instanceof UpgradeItem && stack.getItem() != drawerSlots.get(0).getUpgrade()) {
                    drawerSlots.get(0).changeUpgrade(ItemVariant.of(stack), world, pos, direction, null);
                }
                super.setStackNoCallbacks(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getWorld().isClient()) {
                    drawerSlots.get(0).changeUpgrade(ItemVariant.blank(), player.getWorld(), pos, direction, player);
                }
                super.onTakeItem(player, stack);
            }
        });

        if (this.inventory.size() > 6) {
            // Slot top right
            this.addSlot(new Slot(inventory, 6, 91, 9 + slotExtraY) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }

                @Override
                public boolean isEnabled() {
                    return false;
                }

                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    return false;
                }
            });
            this.addSlot(new Slot(inventory, 7, 109, 9 + slotExtraY) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.getItem() instanceof UpgradeItem;
                }

                @Override
                public int getMaxItemCount() {
                    return 1;
                }

                @Override
                public void setStackNoCallbacks(ItemStack stack) {
                    if (!world.isClient() && !stack.isEmpty() && stack.getItem() instanceof UpgradeItem && stack.getItem() != drawerSlots.get(1).getUpgrade()) {
                        drawerSlots.get(1).changeUpgrade(ItemVariant.of(stack), world, pos, direction, null);
                    }
                    super.setStackNoCallbacks(stack);
                }

                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    if (!player.getWorld().isClient()) {
                        drawerSlots.get(1).changeUpgrade(ItemVariant.blank(), player.getWorld(), pos, direction, player);
                    }
                    super.onTakeItem(player, stack);
                }
            });

            // Slot bottom left
            if (this.inventory.size() > 8) {
                this.addSlot(new Slot(inventory, 8, 69, 31) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean isEnabled() {
                        return false;
                    }

                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return false;
                    }
                });
                this.addSlot(new Slot(inventory, 9, 51, 31) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.getItem() instanceof UpgradeItem;
                    }

                    @Override
                    public int getMaxItemCount() {
                        return 1;
                    }

                    @Override
                    public void setStackNoCallbacks(ItemStack stack) {
                        if (!world.isClient() && !stack.isEmpty() && stack.getItem() instanceof UpgradeItem && stack.getItem() != drawerSlots.get(2).getUpgrade()) {
                            drawerSlots.get(2).changeUpgrade(ItemVariant.of(stack), world, pos, direction, null);
                        }
                        super.setStackNoCallbacks(stack);
                    }

                    @Override
                    public void onTakeItem(PlayerEntity player, ItemStack stack) {
                        if (!player.getWorld().isClient()) {
                            drawerSlots.get(2).changeUpgrade(ItemVariant.blank(), player.getWorld(), pos, direction, player);
                        }
                        super.onTakeItem(player, stack);
                    }
                });
                // Slot bottom right
                this.addSlot(new Slot(inventory, 10, 91, 31) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean isEnabled() {
                        return false;
                    }

                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return false;
                    }
                });
                this.addSlot(new Slot(inventory, 11, 109, 31) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.getItem() instanceof UpgradeItem;
                    }

                    @Override
                    public int getMaxItemCount() {
                        return 1;
                    }

                    @Override
                    public void setStackNoCallbacks(ItemStack stack) {
                        if (!world.isClient() && !stack.isEmpty() && stack.getItem() instanceof UpgradeItem && stack.getItem() != drawerSlots.get(3).getUpgrade()) {
                            drawerSlots.get(3).changeUpgrade(ItemVariant.of(stack), world, pos, direction, null);
                        }
                        super.setStackNoCallbacks(stack);
                    }

                    @Override
                    public void onTakeItem(PlayerEntity player, ItemStack stack) {
                        if (!player.getWorld().isClient()) {
                            drawerSlots.get(3).changeUpgrade(ItemVariant.blank(), player.getWorld(), pos, direction, player);
                        }
                        super.onTakeItem(player, stack);
                    }
                });
            }
        }

        int m;
        int l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity playerEntity, int slotId) {
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            if (slot.getIndex() > 11) {
                if (originalStack.isOf(ModItems.LOCK)) {
                    if (this.slots.get(0).getStack().isEmpty()) {
                        this.slots.get(0).setStack(originalStack.copyWithCount(1));
                        originalStack.decrement(1);
                        return originalStack;
                    }
                } else if (originalStack.isOf(Items.LAVA_BUCKET)) {
                    if (this.slots.get(1).getStack().isEmpty()) {
                        this.slots.get(1).setStack(originalStack.copyWithCount(1));
                        originalStack.decrement(1);
                        return originalStack;
                    }
                } else if (originalStack.isOf(Items.WRITABLE_BOOK)) {
                    if (this.slots.get(2).getStack().isEmpty()) {
                        this.slots.get(2).setStack(originalStack.copyWithCount(1));
                        originalStack.decrement(1);
                        return originalStack;
                    }
                } else if (originalStack.isOf(ModItems.DUPE_WAND) && isCreativeScreen) {
                    if (this.slots.get(3).getStack().isEmpty()) {
                        this.slots.get(3).setStack(originalStack.copyWithCount(1));
                        originalStack.decrement(1);
                        return originalStack;
                    }
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isCreativeScreen() {
        return this.isCreativeScreen;
    }

}
