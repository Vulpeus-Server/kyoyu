package com.vulpeus.kyoyu;

//? if FORGE
/* import net.minecraftforge.fml.common.Mod; */

//? if NEOFORGE
/* import net.neoforged.fml.common.Mod; */


//? if FORGE || NEOFORGE
/* @Mod("kyoyu") */
public class KyoyuMod
        //? if FABRIC
        implements net.fabricmc.api.ModInitializer

        //? if PAPER
        /* extends org.bukkit.plugin.java.JavaPlugin */
{
    //? if FABRIC
    @Override public void onInitialize()

    //? if FORGE || NEOFORGE
    /* public KyoyuMod() */
    //? if PAPER
    /* @Override public void onEnable() */
    {
        Kyoyu.LOGGER.info("Hello, World!");
        Kyoyu.LOGGER.info("KyoyuMod Version : {}", Kyoyu.MOD_VERSION);
    }
}
