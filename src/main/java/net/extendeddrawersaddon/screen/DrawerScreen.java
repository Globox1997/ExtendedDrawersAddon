package net.extendeddrawersaddon.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DrawerScreen extends HandledScreen<DrawerScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("extendeddrawersaddon", "textures/gui/drawer.png");

    public DrawerScreen(DrawerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);

        int size = this.handler.getInventory().size();
        if (this.handler.isCreativeScreen()) {
            context.drawTexture(TEXTURE, this.x + 52, this.y + 53, 176, 36, 72, 18);
        } else {
            context.drawTexture(TEXTURE, this.x + 61, this.y + 53, 176, 36, 54, 18);
        }

        if (size == 6) {
            context.drawTexture(TEXTURE, this.x + 70, this.y + 19, 176, 0, 36, 18);
            if (!this.handler.getSlot(4).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(4).getStack(), this.x + 89, this.y + 20);

            }
        } else if (size < 9) {
            context.drawTexture(TEXTURE, this.x + 50, this.y + 19, 176, 0, 36, 18);
            context.drawTexture(TEXTURE, this.x + 90, this.y + 19, 176, 18, 36, 18);
            if (!this.handler.getSlot(4).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(4).getStack(), this.x + 69, this.y + 20);
            }
            if (!this.handler.getSlot(6).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(6).getStack(), this.x + 91, this.y + 20);
            }
        } else {
            context.drawTexture(TEXTURE, this.x + 50, this.y + 8, 176, 0, 36, 18);
            context.drawTexture(TEXTURE, this.x + 50, this.y + 30, 176, 0, 36, 18);
            context.drawTexture(TEXTURE, this.x + 90, this.y + 8, 176, 18, 36, 18);
            context.drawTexture(TEXTURE, this.x + 90, this.y + 30, 176, 18, 36, 18);
            if (!this.handler.getSlot(4).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(4).getStack(), this.x + 69, this.y + 9);
            }
            if (!this.handler.getSlot(6).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(6).getStack(), this.x + 91, this.y + 9);
            }
            if (!this.handler.getSlot(8).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(8).getStack(), this.x + 69, this.y + 31);
            }
            if (!this.handler.getSlot(10).getStack().isEmpty()) {
                context.drawItem(this.handler.getSlot(10).getStack(), this.x + 91, this.y + 31);
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

}
