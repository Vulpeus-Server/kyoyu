package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

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
        String json = this.kyoyuPlacement.toJson();
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(CompatibleUtils.KyoyuPlayer player) {
        KyoyuPlacement preKyoyuPlacement = Kyoyu.findPlacement(this.kyoyuPlacement.getUuid());
        if (preKyoyuPlacement == null) {
            Kyoyu.LOGGER.info("New placement by {}", player.getName());
            Kyoyu.savePlacement(this.kyoyuPlacement);
            FileRequestPacket fileRequestPacket = new FileRequestPacket(this.kyoyuPlacement.getUuid());
            KyoyuPacketManager.sendS2C(fileRequestPacket, player);
        } else {
            String playerName = player.getName();
            if (!Kyoyu.CONFIG.isAllowedModify(playerName)) {
                Kyoyu.LOGGER.warn("disallowed player attempted to modify.");
                return;
            }
            kyoyuPlacement.updateBy(playerName);
            Kyoyu.savePlacement(kyoyuPlacement);
            for (UUID uuid: Kyoyu.PLAYERS.getAll()) {
                CompatibleUtils.KyoyuPlayer otherPlayer = Kyoyu.PLAYERS.getServerPlayer(uuid);
                if (player.equals(otherPlayer)) continue;
                PlacementMetaPacket placementMetaPacket = new PlacementMetaPacket(this.kyoyuPlacement);
                KyoyuPacketManager.sendS2C(placementMetaPacket, otherPlayer);
            }
        }
    }

    //? if client {
    @Override
    public void onClient() {
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (kyoyuClient == null) return;
        ISchematicPlacement schematicPlacement = (ISchematicPlacement) kyoyuClient.findSchematicPlacement(this.kyoyuPlacement.getUuid());
        if (schematicPlacement == null) return;
        schematicPlacement.kyoyu$updateFromKyoyuPlacement(this.kyoyuPlacement);
    }
    //?}
}
