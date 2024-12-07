package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import net.minecraft.server.level.ServerPlayer;

public class FileResponsePacket extends IKyoyuPacket {

    private final byte[] data;

    public FileResponsePacket(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] encode() {
        return data;
    }

    @Override
    public void onServer(ServerPlayer player) {

        Kyoyu.LOGGER.info("file response from {}", player.getName().getString());

        // TODO
        //  save to file

    }

    @Override
    public void onClient() {

        Kyoyu.LOGGER.info("file response from server");

        // TODO
        //  save to file

    }
}
