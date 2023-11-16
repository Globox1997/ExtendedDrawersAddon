package net.extendeddrawersaddon.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "extendeddrawersaddon")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class ExtendedDrawersAddonConfig implements ConfigData {

    @Comment("Drawer items affect all slots")
    public boolean affectAllSlots = true;
    public boolean showDrawerIcons = false;
    public boolean flatTextures = false;
    @Comment("Will decrease performance when changed, 1.8 is recommended")
    public float blockRenderScale = 1.0f;
}