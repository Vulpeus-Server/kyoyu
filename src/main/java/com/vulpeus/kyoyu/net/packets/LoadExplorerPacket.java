package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class LoadExplorerPacket extends IKyoyuPacket {

    public LoadExplorerPacket() {}
    public LoadExplorerPacket(byte[] data) {}

    @Override
    public void onServer(ServerPlayer player) {

        // TODO: Get Kyoyu Placements
        List<KyoyuPlacement> list = new ArrayList<>();

        PlacementMetaPacket placementMetaPacket = new PlacementMetaPacket(list);
        KyoyuPacketManager.sendS2C(placementMetaPacket, player);
    }
}
