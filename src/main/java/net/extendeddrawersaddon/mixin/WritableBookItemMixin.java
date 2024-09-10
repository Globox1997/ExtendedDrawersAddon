package net.extendeddrawersaddon.mixin;

import io.github.mattidragon.extendeddrawers.block.DrawerBlock;
import io.github.mattidragon.extendeddrawers.block.entity.DrawerBlockEntity;
import net.extendeddrawersaddon.access.DrawerStorageAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.WritableBookItem;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WritableBookItem.class)
public abstract class WritableBookItemMixin extends Item {

    public WritableBookItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof DrawerBlock && context.getWorld().getBlockEntity(context.getBlockPos()) != null) {
            if (!context.getWorld().isClient()) {
                if (context.getWorld().getBlockEntity(context.getBlockPos()) instanceof DrawerBlockEntity drawerBlockEntity && drawerBlockEntity.storages.length > 0 && !((DrawerStorageAccess) (Object) drawerBlockEntity.storages[0]).getShowDrawerSlotCount()) {
                    for (int i = 0; i < drawerBlockEntity.storages.length; i++) {
                        ((DrawerStorageAccess) (Object) drawerBlockEntity.storages[i]).setShowDrawerSlotCount(true);
                    }
                    if (!context.getPlayer().isCreative()) {
                        context.getPlayer().getStackInHand(context.getHand()).decrement(1);
                    }
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
