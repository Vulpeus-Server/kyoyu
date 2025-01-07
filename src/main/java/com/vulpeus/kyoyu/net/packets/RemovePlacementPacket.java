package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

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
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    @Override
    public void onServer(ServerPlayer serverPlayer) {
        KyoyuPlacement kyoyuPlacement = Kyoyu.findPlacement(uuid);
        Kyoyu.LOGGER.info("Remove kyoyu placement '{}' by {}", uuid.toString(), serverPlayer.getName().getString());
        if (kyoyuPlacement == null) {
            Kyoyu.LOGGER.error("Not found kyoyu placement '{}'!", uuid.toString());
        } else {
            for (ServerPlayer player: Kyoyu.PLAYERS) {
                RemovePlacementPacket removePlacementPacket = new RemovePlacementPacket(uuid);
                KyoyuPacketManager.sendS2C(removePlacementPacket, player);
            }
            Kyoyu.unlinkPlacement(kyoyuPlacement);
        }
    }

    //? if client {
    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Remove placement '{}'", uuid.toString());
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (kyoyuClient == null) return;
        ISchematicPlacement schematicPlacement = ((ISchematicPlacement) kyoyuClient.findSchematicPlacement(uuid));
        if (schematicPlacement == null) return;
        schematicPlacement.kyoyu$setKyoyuId(null);
    }
    //?}
}
