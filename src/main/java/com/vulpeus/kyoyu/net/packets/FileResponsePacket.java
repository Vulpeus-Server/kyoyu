package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

import java.nio.ByteBuffer;
import java.util.UUID;

public class FileResponsePacket extends IKyoyuPacket {

    private final UUID uuid;
    private final byte[] data;

    public FileResponsePacket(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        this.uuid = new UUID(bb.getLong(), bb.getLong());
        this.data = new byte[bb.getInt()];
        bb.get(this.data);
    }

    public FileResponsePacket(UUID uuid, byte[] data) {
        this.uuid = uuid;
        this.data = data;
    }

    @Override
    public byte[] encode() {
        int totalSize = Long.BYTES * 2 + Integer.BYTES + data.length;
        ByteBuffer bb = ByteBuffer.allocate(totalSize);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        bb.putInt(data.length);
        bb.put(data);
        return bb.array();
    }

    @Override
    public void onServer(ServerPlayer player) {

        Kyoyu.LOGGER.info("file response from {}", player.getName().getString());
        KyoyuPlacement kyoyuPlacement = Kyoyu.findPlacement(uuid);
        kyoyuPlacement.writeToFile(data);

    }

    //? if client {
    @Override
    public void onClient() {

        Kyoyu.LOGGER.info("file response from server");
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (kyoyuClient != null) {
            KyoyuPlacement kyoyuPlacement = ((ISchematicPlacement) kyoyuClient.findSchematicPlacement(uuid)).kyoyu$toKyoyuPlacement();
            kyoyuPlacement.writeToFile(data);
        }
    }
    //?}
}
