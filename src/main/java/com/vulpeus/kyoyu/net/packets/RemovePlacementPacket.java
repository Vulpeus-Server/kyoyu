package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.nio.ByteBuffer;
import java.util.UUID;

public class RemovePlacementPacket extends IKyoyuPacket {

    private final UUID uuid;

    public RemovePlacementPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public RemovePlacementPacket(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        this.uuid = new UUID(bb.getLong(), bb.getLong());
    }

    @Override
    public byte[] encode() {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(this.uuid.getMostSignificantBits());
        bb.putLong(this.uuid.getLeastSignificantBits());
        return bb.array();
    }

    @Override
    public void onServer(CompatibleUtils.KyoyuPlayer serverPlayer) {
        KyoyuPlacement kyoyuPlacement = Kyoyu.findPlacement(this.uuid);
        Kyoyu.LOGGER.info("Remove kyoyu placement '{}' by {}", this.uuid.toString(), serverPlayer.getName());
        if (kyoyuPlacement == null) {
            Kyoyu.LOGGER.error("Not found kyoyu placement '{}'!", this.uuid.toString());
        } else {
            for (UUID uuid: Kyoyu.PLAYERS.getAll()) {
                RemovePlacementPacket removePlacementPacket = new RemovePlacementPacket(this.uuid);
                KyoyuPacketManager.sendS2C(removePlacementPacket, Kyoyu.PLAYERS.getServerPlayer(uuid));
            }
            Kyoyu.unlinkPlacement(kyoyuPlacement);
        }
    }

    //? if client {
    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Remove placement '{}'", this.uuid.toString());
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (kyoyuClient == null) return;
        ISchematicPlacement schematicPlacement = ((ISchematicPlacement) kyoyuClient.findSchematicPlacement(this.uuid));
        if (schematicPlacement == null) return;
        schematicPlacement.kyoyu$setKyoyuId(null);
    }
    //?}
}
