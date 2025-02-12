package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FileResponsePacket extends IKyoyuPacket {

    private final UUID uuid;
    private final String filename;
    private final byte[] data;

    public FileResponsePacket(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);

        this.uuid = new UUID(bb.getLong(), bb.getLong());

        byte[] tmp = new byte[bb.getInt()];
        bb.get(tmp);
        this.filename = new String(tmp, StandardCharsets.UTF_8);

        this.data = new byte[bb.getInt()];
        bb.get(this.data);
    }

    public FileResponsePacket(UUID uuid, String filename, byte[] data) {
        this.uuid = uuid;
        this.filename = filename;
        this.data = data;
    }

    public FileResponsePacket(KyoyuPlacement kyoyuPlacement) throws IOException {
        this(kyoyuPlacement.getUuid(), kyoyuPlacement.getFilename(), kyoyuPlacement.readFromFile());
    }

    @Override
    public byte[] encode() {
        byte[] tmp = this.filename.getBytes(StandardCharsets.UTF_8);
        int totalSize = Long.BYTES * 2 + Integer.BYTES *2 + tmp.length + data.length;
        ByteBuffer bb = ByteBuffer.allocate(totalSize);

        bb.putLong(this.uuid.getMostSignificantBits());
        bb.putLong(this.uuid.getLeastSignificantBits());

        bb.putInt(tmp.length);
        bb.put(tmp);

        bb.putInt(this.data.length);
        bb.put(this.data);
        return bb.array();
    }

    @Override
    public void onServer(CompatibleUtils.KyoyuPlayer player) {

        Kyoyu.LOGGER.info("file response from {}", player.getName());

        KyoyuPlacement.writeToFile(this.filename, this.data);
    }

    //? if client {
    @Override
    public void onClient() {

        Kyoyu.LOGGER.info("file response from server");
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (kyoyuClient != null) {
            KyoyuPlacement.writeToFile(this.filename, this.data);
        }
    }
    //?}
}
