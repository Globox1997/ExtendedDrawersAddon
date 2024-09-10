package net.extendeddrawersaddon.mixin;

import net.extendeddrawersaddon.access.DrawerInteractionHandlerAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 999)
public class ItemStackMixin {

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"))
    private void extended_drawers_addon$applyModifiers(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        var world = context.getWorld();
        var state = world.getBlockState(context.getBlockPos());

        if (state.getBlock() instanceof DrawerInteractionHandlerAccess drawerInteractionHandlerAccess) {
            drawerInteractionHandlerAccess.setDrawerInteractionPlayerEntity(context.getPlayer());
        }
    }
}
