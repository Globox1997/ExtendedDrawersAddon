package net.extendeddrawersaddon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.extendeddrawersaddon.init.ConfigInit;
import net.extendeddrawersaddon.init.EventInit;
import net.extendeddrawersaddon.init.ScreenInit;
import net.fabricmc.api.ModInitializer;

public class ExtendedDrawersAddonMain implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExtendedDrawersAddonMain.class);

    @Override
    public void onInitialize() {
        ConfigInit.init();
        EventInit.init();
        ScreenInit.init();
    }

}
