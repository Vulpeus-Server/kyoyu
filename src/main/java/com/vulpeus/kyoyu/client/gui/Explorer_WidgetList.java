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
import java.util.List;

public class Explorer_WidgetList extends WidgetListBase<KyoyuPlacement, Explorer_WidgetListEntry> {

    private final List<KyoyuPlacement> kyoyuPlacement;

    public Explorer_WidgetList(int x, int y, int width, int height, ISelectionListener<KyoyuPlacement> selectionListener, List<KyoyuPlacement> kyoyuPlacement) {
        super(x, y, width, height, selectionListener);
        this.browserEntryHeight = 22;
        this.kyoyuPlacement = kyoyuPlacement;
        Kyoyu.LOGGER.info("{}", getAllEntries());
        setSize(width, height);
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
        return new Explorer_WidgetListEntry(x, y, browserEntryWidth, getBrowserEntryHeightFor(kyoyuPlacement), kyoyuPlacement, listIndex, isOdd);
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
