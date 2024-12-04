package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LoadExplorerPacket extends IKyoyuPacket {

    private final List<KyoyuPlacement> kyoyuPlacementList;

    public LoadExplorerPacket() {
        this.kyoyuPlacementList = null;
    }
    public LoadExplorerPacket(List<KyoyuPlacement> kyoyuPlacementList) {
        this.kyoyuPlacementList = kyoyuPlacementList;
    }
    public LoadExplorerPacket(byte[] data) {
        if (data.length == 0) {
            this.kyoyuPlacementList = null;
        } else {
            String json = new String(data, StandardCharsets.UTF_8);
            this.kyoyuPlacementList = KyoyuPlacement.fromJsonList(json);
        }
    }

    @Override
    public byte[] encode() {
        if (kyoyuPlacementList != null) {
            String json = KyoyuPlacement.toJsonList(kyoyuPlacementList);
            return json.getBytes(StandardCharsets.UTF_8);
        } else {
            return new byte[0];
        }
    }

    @Override
    public void onServer(ServerPlayer player) {

        // TODO: Get Kyoyu Placements
        List<KyoyuPlacement> list = new ArrayList<>();

        LoadExplorerPacket loadExplorerPacket = new LoadExplorerPacket(list);
        KyoyuPacketManager.sendS2C(loadExplorerPacket, player);
    }

    //? if client {
    @Override
    public void onClient() {
        if (kyoyuPlacementList != null) {
            Kyoyu.LOGGER.info("KyoyuPlacement list from server");
            com.vulpeus.kyoyu.client.NetworkHelper.openExplorer(kyoyuPlacementList);
        } else {
            Kyoyu.LOGGER.info("KyoyuPlacement list is empty!");
        }
    }
    //?}
}
