package com.vulpeus.kyoyu.net;

import net.minecraft.world.entity.player.Player;

public abstract class IKyoyuPacket {

    public IKyoyuPacket(byte[] bytes) {}

    public abstract byte[] encode();

    public abstract void onServer(Player player);

    public abstract void onClient();

}