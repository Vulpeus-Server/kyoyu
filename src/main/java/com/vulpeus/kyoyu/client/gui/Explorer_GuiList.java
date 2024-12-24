package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class Explorer_GuiList extends GuiListBase<KyoyuPlacement, Explorer_WidgetListEntry, Explorer_WidgetList> {

    public final List<KyoyuPlacement> kyoyuPlacementList;

    public Explorer_GuiList(Screen parentGui, List<KyoyuPlacement> kyoyuPlacementList) {
        super(12, 30);
        this.setParent(parentGui);
        this.setTitle(StringUtils.translate("kyoyu.gui.title", Kyoyu.MOD_VERSION));
        this.kyoyuPlacementList = kyoyuPlacementList;
    }

    @Override
    protected Explorer_WidgetList createListWidget(int x, int y) {
        return new Explorer_WidgetList(x, y, this.width - 20, this.height - 20, null, kyoyuPlacementList);
    }

    @Override
    protected int getBrowserWidth() {
        return this.width;
    }

    @Override
    protected int getBrowserHeight() {
        return this.height;
    }
}
//?}
