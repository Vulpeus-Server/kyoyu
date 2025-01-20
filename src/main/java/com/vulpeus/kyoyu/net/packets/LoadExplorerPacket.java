package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.nio.charset.StandardCharsets;
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
        if (this.kyoyuPlacementList != null) {
            String json = KyoyuPlacement.toJsonList(this.kyoyuPlacementList);
            return json.getBytes(StandardCharsets.UTF_8);
        } else {
            return new byte[0];
        }
    }

    @Override
    public void onServer(CompatibleUtils.KyoyuPlayer player) {

        List<KyoyuPlacement> list = Kyoyu.getAllPlacement();

        LoadExplorerPacket loadExplorerPacket = new LoadExplorerPacket(list);
        KyoyuPacketManager.sendS2C(loadExplorerPacket, player);
    }

    //? if client {
    @Override
    public void onClient() {
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();
        if (this.kyoyuPlacementList != null && kyoyuClient != null) {
            Kyoyu.LOGGER.info("KyoyuPlacement list from server");
            kyoyuClient.openExplorer(this.kyoyuPlacementList);
        } else {
            Kyoyu.LOGGER.info("KyoyuPlacement list is empty!");
        }
    }
    //?}
}
