package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Explorer_GuiList extends GuiListBase<KyoyuPlacement, Explorer_WidgetListEntry, Explorer_WidgetList> {

    public final List<KyoyuPlacement> kyoyuPlacementList;
    private final Map<Explorer_WidgetList.SortKey, Integer> columnWidth;

    public Explorer_GuiList(Screen parentGui, List<KyoyuPlacement> kyoyuPlacementList) {
        super(10, 30 + 2 + 15 + 2);
        this.setParent(parentGui);
        this.setTitle(StringUtils.translate("kyoyu.gui.title", Kyoyu.MOD_VERSION));
        this.kyoyuPlacementList = kyoyuPlacementList;
        this.columnWidth = new HashMap<>();

        for (Explorer_WidgetList.SortKey key: Explorer_WidgetList.SortKey.ALL) {
            int w = getStringWidth(key.columnName());
            for (KyoyuPlacement placement: kyoyuPlacementList) {
                w = Math.max(w, getStringWidth(key.comparator().apply(placement)));
            }
            this.columnWidth.put(key, w);
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 30;
        int y = 30 + 2;

        for (Explorer_WidgetList.SortKey key: Explorer_WidgetList.SortKey.ALL) {
            int w = this.columnWidth.get(key) + 5;
            this.addButton(
                    new ButtonGeneric(x, y, w, 15, key.columnName()),
                    (button, mouseButton) -> this.getListWidget().sortList(key)
            );
            x += w;
        }
    }

    @Override
    protected Explorer_WidgetList createListWidget(int x, int y) {
        return new Explorer_WidgetList(x, y, this.width - 20, this.height - 30 - 30 - 20, null, kyoyuPlacementList, columnWidth);
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
