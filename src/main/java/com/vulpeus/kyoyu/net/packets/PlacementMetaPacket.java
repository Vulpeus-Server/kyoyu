package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class PlacementMetaPacket extends IKyoyuPacket {

    private final List<KyoyuPlacement> kyoyuPlacementList;

    public PlacementMetaPacket(List<KyoyuPlacement> kyoyuPlacementList) {
        this.kyoyuPlacementList = kyoyuPlacementList;
    }

    public PlacementMetaPacket(byte[] bytes) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        this.kyoyuPlacementList = KyoyuPlacement.fromJson(json);
    }

    @Override
    public byte[] encode() {
        String json = KyoyuPlacement.toJson(kyoyuPlacementList);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(ServerPlayer player) {
        Kyoyu.LOGGER.info("Share placement from {} :", player.getName().getString());
        for (KyoyuPlacement kyoyuPlacement: kyoyuPlacementList) {
            Kyoyu.LOGGER.info(" > `{}`", kyoyuPlacement.getName());
        }

        // TODO: save to file
    }

    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Share placement from server");
        for (KyoyuPlacement kyoyuPlacement: kyoyuPlacementList) {
            Kyoyu.LOGGER.info(" > `{}`", kyoyuPlacement.getName());
        }

        //? if client {
        com.vulpeus.kyoyu.client.NetworkHelper.openExplorer(kyoyuPlacementList);
        //?}

//        if (Kyoyu.isClient() && Kyoyu.getClient().isPresent()) {
//            fi.dy.masa.malilib.gui.GuiBase.openGui(
//                    new com.vulpeus.kyoyu.client.gui.Explorer_GuiList(
//                            fi.dy.masa.malilib.util.GuiUtils.getCurrentScreen(),
//                            kyoyuPlacementList
//                    )
//            );
//        }
    }
}
