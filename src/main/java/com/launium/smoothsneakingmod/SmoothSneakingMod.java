package com.launium.smoothsneakingmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "smoothsneakingmod", clientSideOnly = true, useMetadata = true)
public class SmoothSneakingMod {

    private static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("SmoothSneakingMod is initialized");
    }

}
