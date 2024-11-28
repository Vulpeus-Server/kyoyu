package com.vulpeus.kyoyu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//? if FORGE
/* import net.minecraftforge.fml.common.Mod; */

//? if NEOFORGE
/* import net.neoforged.fml.common.Mod; */


//? if FORGE || NEOFORGE
/* @Mod("kyoyu") */
public class Kyoyu
        //? if FABRIC
        implements net.fabricmc.api.ModInitializer

        //? if PAPER
        /* extends org.bukkit.plugin.java.JavaPlugin */
{
    public static final String MOD_ID = "kyoyu";
    public static final String MOD_VERSION = /*$ mod_version*/ "unknown";

    public static final Logger LOGGER = LogManager.getLogger();

    //? if FABRIC
    @Override public void onInitialize()

    //? if FORGE || NEOFORGE
    /* public Kyoyu() */
    //? if PAPER
    /* @Override public void onEnable() */
    {
        LOGGER.info("Hello, World!");
        LOGGER.info("Kyoyu Version : {}", MOD_VERSION);
    }
}
