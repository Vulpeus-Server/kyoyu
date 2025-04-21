package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.render.RenderUtils;

//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} elif >=1.16 {
/* import com.mojang.blaze3d.vertex.PoseStack; */
//?}

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Explorer_WidgetList extends WidgetListBase<KyoyuPlacement, Explorer_WidgetListEntry> {

    private final List<KyoyuPlacement> kyoyuPlacement;
    private final Map<SortKey, Integer> columnWidth;

    public Explorer_WidgetList(int x, int y, int width, int height, ISelectionListener<KyoyuPlacement> selectionListener, List<KyoyuPlacement> kyoyuPlacement, Map<SortKey, Integer> columnWidth) {
        super(x, y, width, height, selectionListener);
        this.browserEntryHeight = 22;
        this.kyoyuPlacement = kyoyuPlacement;
        this.columnWidth = columnWidth;
        Kyoyu.LOGGER.info(getAllEntries());
        setSize(width, height);
    }

    public enum SortKey {
        NAME("Name", KyoyuPlacement::getName),
        OWNER("Created by", KyoyuPlacement::getOwnerName),
        UPDATER("Updated by", KyoyuPlacement::getUpdaterName),
        TIMESTAMP("Timestamp", KyoyuPlacement::getTimestampAsString);

        private final String columnName;
        private final Function<KyoyuPlacement, String> comparator;
        public static final SortKey[] ALL = {NAME, OWNER, UPDATER, TIMESTAMP};

        SortKey(String columnName, Function<KyoyuPlacement, String> comparator) {
            this.columnName = columnName;
            this.comparator = comparator;
        }

        public String columnName() {
            return this.columnName;
        }

        public Function<KyoyuPlacement, String> comparator() {
            return this.comparator;
        }
    }

    private SortKey currentSortKey = SortKey.NAME;
    private boolean ascending = true;

    public void sortList(SortKey key) {
        if (this.currentSortKey == key) {
            this.ascending = !this.ascending;
        } else {
            this.currentSortKey = key;
            this.ascending = true;
        }

        Comparator<KyoyuPlacement> comparator = Comparator.comparing(key.comparator());

        if (!ascending) {
            comparator = comparator.reversed();
        }

        kyoyuPlacement.sort(comparator);
        this.refreshEntries();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        this.browserWidth = width - 20;
        this.browserHeight = height - posY - 20;
        this.browserEntryWidth = browserWidth - 5;
    }

    @Override
    protected Explorer_WidgetListEntry createListEntryWidget(int x, int y, int listIndex, boolean isOdd, KyoyuPlacement kyoyuPlacement) {
        return new Explorer_WidgetListEntry(x, y, browserEntryWidth, getBrowserEntryHeightFor(kyoyuPlacement), kyoyuPlacement, listIndex, columnWidth);
    }

    @Override
    protected Collection<KyoyuPlacement> getAllEntries() {
        Kyoyu.LOGGER.info("{}", this.kyoyuPlacement);
        return this.kyoyuPlacement;
    }

    @Override
    //? if >=1.20 {
    public void drawContents(GuiGraphics drawContext, int mouseX, int mouseY, float partialTicks) {
    //?} elif >=1.16 {
    /* public void drawContents(PoseStack drawContext, int mouseX, int mouseY, float partialTicks) { */
    //?} else {
    /* public void drawContents(int mouseX, int mouseY, float partialTicks) { */
    //?}

        // Draw an outline around the entire widget
        RenderUtils.drawOutlinedBox(posX, posY, browserWidth, browserHeight, 0xB0000000, GuiBase.COLOR_HORIZONTAL_BAR);

        //? if >=1.16 {
        super.drawContents(drawContext, mouseX, mouseY, partialTicks);
        //?} else {
        /* super.drawContents(mouseX, mouseY, partialTicks); */
        //?}
    }
}
//?}
