package com.vulpeus.kyoyu;

import com.vulpeus.kyoyu.net.KyoyuPacketPayload;

//? if FORGE
/* import net.minecraftforge.fml.common.Mod; */

//? if NEOFORGE
/* import net.neoforged.fml.common.Mod; */


//? if FORGE || NEOFORGE
/* @Mod(Kyoyu.MOD_ID) */
public class KyoyuMod
        //? if FABRIC
        implements net.fabricmc.api.ModInitializer

        //? if PAPER
        /* extends org.bukkit.plugin.java.JavaPlugin */
{
    //? if FABRIC
    @Override public void onInitialize()
    //? if NEOFORGE
    /* public KyoyuMod(net.neoforged.bus.api.IEventBus modBus) */
    //? if FORGE
    /* public KyoyuMod() */
    //? if PAPER
    /* @Override public void onEnable() */
    {
        Kyoyu.LOGGER.info("Hello, World!");
        Kyoyu.LOGGER.info("KyoyuMod Version : {}", Kyoyu.MOD_VERSION);

        Kyoyu.setEnv(
                //? if FABRIC
                net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT
                //? if NEOFORGE
                /* net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT */
                //? if FORGE
                /* net.minecraftforge.fml.loading.FMLEnvironment.dist == net.minecraftforge.api.distmarker.Dist.CLIENT */
                //? if PAPER
                /* false */
        );

        Kyoyu.loadConfig();

        //? if FABRIC
        KyoyuPacketPayload.register();
        //? if NEOFORGE
         /* modBus.addListener(KyoyuPacketPayload::register); */
        // TODO: Forge Packet Register
    }
}
