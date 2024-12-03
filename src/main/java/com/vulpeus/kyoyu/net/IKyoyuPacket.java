package com.vulpeus.kyoyu.net;

import net.minecraft.server.level.ServerPlayer;

public abstract class IKyoyuPacket {

    public byte[] encode() {
        return new byte[0];
    }

    public void onServer(ServerPlayer player) {
        // nothing to do
    }

    public void onClient() {
        // nothing to do
    }
}
