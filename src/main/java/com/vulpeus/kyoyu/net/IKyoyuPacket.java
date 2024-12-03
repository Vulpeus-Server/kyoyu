package com.vulpeus.kyoyu.net;

import net.minecraft.server.level.ServerPlayer;

public abstract class IKyoyuPacket {

    public IKyoyuPacket(byte[] bytes) {}

    public abstract byte[] encode();

    public abstract void onServer(ServerPlayer player);

    public abstract void onClient();

}