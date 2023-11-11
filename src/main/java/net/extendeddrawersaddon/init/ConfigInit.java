package net.extendeddrawersaddon.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.extendeddrawersaddon.config.ExtendedDrawersAddonConfig;

public class ConfigInit {

    public static ExtendedDrawersAddonConfig CONFIG = new ExtendedDrawersAddonConfig();

    public static void init() {
        AutoConfig.register(ExtendedDrawersAddonConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ExtendedDrawersAddonConfig.class).getConfig();
    }
}
