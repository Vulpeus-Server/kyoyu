package com.vulpeus.kyoyu;

import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

//? if PAPER {
/* import org.bukkit.entity.Player; */
//?} else {
import net.minecraft.server.level.ServerPlayer;
//?}

public class CompatibleUtils {
    public static ResourceLocation identifier(String namespace, String path) {
        //? if >1.20.6 {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?} elif =1.20.6 {
        /* return ResourceLocation.tryBuild(namespace, path); */
        //?} else {
        /* return new ResourceLocation(namespace, path); */
        //?}
    }


    public static class KyoyuPlayer {
        private boolean isCompatible = false;

        //? if PAPER {
            /* private final Player player; */
        //?} else {
            private final ServerPlayer player;
        //?}

        public KyoyuPlayer(
                //? if PAPER {
                    /* Player player */
                //?} else {
                    ServerPlayer player
                //?}
        ) {
            this.player = player;
        }

        public UUID getUUID() {
            //? if PAPER {
                /* return player.getUniqueId(); */
            //?} else {
                return player.getUUID();
            //?}
        }
        public String getName() {
            //? if PAPER {
                /* return player.getName(); */
            //?} else {
                return player.getName().getString();
            //?}
        }
        public void setCompatible(boolean isCompatible) {
            this.isCompatible = isCompatible;
        }
        public boolean isCompatible() {
            return this.isCompatible;
        }

        //? if PAPER {
            /* public Player player() { */
        //?} else {
            public ServerPlayer player() {
        //?}
            return this.player;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj instanceof KyoyuPlayer) return ((KyoyuPlayer) obj).player.equals(this.player);
            return this.player.equals(obj);
        }
    }
}
