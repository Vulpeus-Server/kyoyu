package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class Explorer_GuiList extends GuiListBase<KyoyuPlacement, Explorer_WidgetListEntry, Explorer_WidgetList> {

    public final List<KyoyuPlacement> kyoyuPlacementList;

    public Explorer_GuiList(Screen parentGui, List<KyoyuPlacement> kyoyuPlacementList) {
        super(10, 30 + 2 + 15 + 2);
        this.setParent(parentGui);
        this.setTitle(StringUtils.translate("kyoyu.gui.title", Kyoyu.MOD_VERSION));
        this.kyoyuPlacementList = kyoyuPlacementList;
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 10;
        int y = 30 + 2;

        x += 20;
        this.addButton(
                new ButtonGeneric(x, y, 198, 15, "Name"),
                (button, mouseButton) -> this.getListWidget().sortList(Explorer_WidgetList.SortKey.NAME)
        );
        x += 200;
        this.addButton(
                new ButtonGeneric(x, y, 78, 15, "Created by"),
                (button, mouseButton) -> this.getListWidget().sortList(Explorer_WidgetList.SortKey.OWNER)
        );
        x += 80;
        this.addButton(
                new ButtonGeneric(x, y, 78, 15, "Updated by"),
                (button, mouseButton) -> this.getListWidget().sortList(Explorer_WidgetList.SortKey.UPDATER)
        );
        x += 80;
        this.addButton(
                new ButtonGeneric(x, y, 178, 15, "Timestamp"),
                (button, mouseButton) -> this.getListWidget().sortList(Explorer_WidgetList.SortKey.TIMESTAMP)
        );
        x += 180;
    }

    @Override
    protected Explorer_WidgetList createListWidget(int x, int y) {
        return new Explorer_WidgetList(x, y, this.width - 20, this.height - 30 - 30 - 20, null, kyoyuPlacementList);
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
