package cn.nulladev.railguncraft.core;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigLoader {
    private static Configuration config;

    private static Logger logger;

    public static boolean canRailgunDestroyBlocks;

    public ConfigLoader(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
        load();
    }

    public static void load() {
        logger.info("Started loading config. ");
        String comment;

        comment = "Can Railgun destroy blocks.";
        canRailgunDestroyBlocks = config.get(Configuration.CATEGORY_GENERAL, "canRailgunDestroyBlocks", true, comment).getBoolean();

        config.save();
        logger.info("Finished loading config. ");
    }

    public static Logger logger() {
        return logger;
    }
}
