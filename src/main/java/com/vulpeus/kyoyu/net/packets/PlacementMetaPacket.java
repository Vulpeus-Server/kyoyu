package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;

public class PlacementMetaPacket extends IKyoyuPacket {

    private final KyoyuPlacement kyoyuPlacement;

    public PlacementMetaPacket(KyoyuPlacement kyoyuPlacement) {
        this.kyoyuPlacement = kyoyuPlacement;
    }

    public PlacementMetaPacket(byte[] bytes) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        this.kyoyuPlacement = KyoyuPlacement.fromJson(json);
    }

    @Override
    public byte[] encode() {
        String json = kyoyuPlacement.toJson();
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(ServerPlayer player) {
        Kyoyu.LOGGER.info("New placement by {}", player.getName());
        KyoyuPlacement preKyoyuPlacement = Kyoyu.findPlacement(kyoyuPlacement.getUuid());
        if (preKyoyuPlacement == null) {
            Kyoyu.savePlacement(kyoyuPlacement);
            FileRequestPacket fileRequestPacket = new FileRequestPacket(kyoyuPlacement.getUuid());
            KyoyuPacketManager.sendS2C(fileRequestPacket, player);
        } else {
            // TODO: Modify
            //  check diff and update `updater` field
        }
    }

    //? if client {
    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Modify placement by {}", kyoyuPlacement.getUuid());
        // TODO: modify
    }
    //?}
}
