package com.vulpeus.kyoyu.client;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.gui.Explorer_GuiList;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.GuiUtils;

import java.util.List;

public class NetworkHelper {
    public static void openExplorer(List<KyoyuPlacement> kyoyuPlacementList){
        if (Kyoyu.isClient() && Kyoyu.getClient().isPresent() && GuiUtils.getCurrentScreen() instanceof GuiBase) {
            GuiBase currentGui = (GuiBase) GuiUtils.getCurrentScreen();
            GuiBase.openGui(new Explorer_GuiList(currentGui.getParent(), kyoyuPlacementList));
        }
    }
}
//?}
