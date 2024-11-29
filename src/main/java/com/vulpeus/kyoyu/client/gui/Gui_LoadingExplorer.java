package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;

//? if >=1.20
import net.minecraft.client.gui.DrawContext;
//? elif >=1.16
/* import net.minecraft.client.util.math.MatrixStack; */

public class Gui_LoadingExplorer extends GuiBase {

    @Override
    public void init() {
        this.setParent(GuiUtils.getCurrentScreen());
        this.setTitle(StringUtils.translate("kyoyu.gui.title.title", Kyoyu.MOD_VERSION));
    }

    @Override
    //? if >=1.20 {
    public void drawContents(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
    //?} elif >=1.16 {
    /* public void drawContents(MatrixStack drawContext, int mouseX, int mouseY, float partialTicks) { */
    //?} else {
    /* public void drawContents(int mouseX, int mouseY, float partialTicks) { */
    //?}

        String loadingText = StringUtils.translate("kyoyu.message.loading");

        int x = this.width / 2 - (getStringWidth(loadingText) + 12) / 2;
        int y = this.height / 2 - 12;


        //? if >=1.16 {
        drawString(drawContext, loadingText, x, y, 0xC0C0C0C0);
        //?} else {
        /* drawString(loadingText, x, y, 0xC0C0C0C0); */
        //?}
    }
}
//?}
