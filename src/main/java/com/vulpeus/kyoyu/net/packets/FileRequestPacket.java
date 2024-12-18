package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class FileRequestPacket extends IKyoyuPacket {

    private final UUID uuid;

    public FileRequestPacket(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        this.uuid = new UUID(bb.getLong(), bb.getLong());
    }
    public FileRequestPacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public byte[] encode() {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    @Override
    public void onServer(ServerPlayer player) {

        Kyoyu.LOGGER.info("file request from {}", player.getName().getString());

        try {
            FileResponsePacket fileResponsePacket = new FileResponsePacket(uuid, Kyoyu.findPlacement(uuid).readFromFile());
            KyoyuPacketManager.sendS2C(fileResponsePacket, player);
        } catch (IOException e) {
            Kyoyu.LOGGER.error(e);
        }
    }

    //? if client {
    @Override
    public void onClient() {

        Kyoyu.LOGGER.info("file request from server");
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (kyoyuClient != null) {
            try {
                KyoyuPlacement kyoyuPlacement = ((ISchematicPlacement) kyoyuClient.findSchematicPlacement(uuid)).kyoyu$toKyoyuPlacement();
                FileResponsePacket fileResponsePacket = new FileResponsePacket(uuid, kyoyuPlacement.readFromFile());
                KyoyuPacketManager.sendC2S(fileResponsePacket);
            } catch (IOException e) {
                Kyoyu.LOGGER.error(e);
            }
        }
    }
    //?}
}
