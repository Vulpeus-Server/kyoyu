package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;

public class FileRequestPacket extends IKyoyuPacket {

    private final String filename;

    public FileRequestPacket(byte[] bytes) {
        this.filename = new String(bytes, StandardCharsets.UTF_8);
    }
    public FileRequestPacket(String filename) {
        this.filename = filename;
    }

    @Override
    public byte[] encode() {
        return filename.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(ServerPlayer player) {

        Kyoyu.LOGGER.info("file request from {}", player.getName().getString());

        // TODO FileResponcePacket
        /*
        FileResponcePacket fileResponcePacket = new FileResponcePacket();
        KyoyuPacketManager.sendS2C(fileResponcePacket, player);
         */
    }

    @Override
    public void onClient() {

        Kyoyu.LOGGER.info("file request from server");

        // TODO FileResponcePacket
        /*
        FileResponcePacket fileResponcePacket = new FileResponcePacket();
        KyoyuPacketManager.sendC2S(fileResponcePacket);
         */
    }
}
