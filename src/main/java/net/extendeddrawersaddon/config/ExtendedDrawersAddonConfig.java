package net.extendeddrawersaddon.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "extendeddrawersaddon")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class ExtendedDrawersAddonConfig implements ConfigData {

    // public float test = 1.0f;
    // public float test2 = 0.0f;
    // public float test3 = 0.0f;
    // public float test4 = 0.0f;

    // public float test5 = 0.0f;
    // public float test6 = 0.0f;
    // public float test7 = 0.0f;

    // public float test8 = 0.0f;
    // public float test9 = 0.0f;
    // public float test0 = 0.0f;

    @Comment("Drawer items affect all slots")
    public boolean affectAllSlots = true;
    public boolean showDrawerIcons = false;
    public boolean flatTextures = false;
}