package com.vulpeus.kyoyu.client;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.util.List;

//? if client {
import com.vulpeus.kyoyu.client.gui.Explorer_GuiList;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.GuiUtils;
//?}

public class KyoyuClient {

    private static KyoyuClient instance = null;

    private final String serverVersion;

    public KyoyuClient(String serverVersion) {
        this.serverVersion = serverVersion;
    }
    private String serverVersion() {
        return serverVersion;
    }

    public static void init(String serverVersion) {
        if (instance != null) {
            Kyoyu.LOGGER.error("Duplicate client init");
            return;
        }
        Kyoyu.LOGGER.info("Kyoyu client init");
        instance = new KyoyuClient(serverVersion);
    }

    public static void deinitClient() {
        Kyoyu.LOGGER.info("Kyoyu client deinit");
        instance = null;
    }

    public static KyoyuClient getInstance() {
        return instance;
    }

//? if client {
    public void openExplorer(List<KyoyuPlacement> kyoyuPlacementList){
        if (Kyoyu.isClient() && KyoyuClient.getInstance() != null && GuiUtils.getCurrentScreen() instanceof GuiBase) {
            GuiBase currentGui = (GuiBase) GuiUtils.getCurrentScreen();
            GuiBase.openGui(new Explorer_GuiList(currentGui.getParent(), kyoyuPlacementList));
        }
    }
//?}
}
