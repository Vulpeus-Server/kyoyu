package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;

//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} elif >=1.16 {
/* import com.mojang.blaze3d.vertex.PoseStack; */
//?}

public class LoadingExplorer_Gui extends GuiBase {

    @Override
    public void init() {
        this.setTitle(StringUtils.translate("kyoyu.gui.title", Kyoyu.MOD_VERSION));
    }

    @Override
    //? if >=1.20 {
    public void drawContents(GuiGraphics drawContext, int mouseX, int mouseY, float partialTicks) {
    //?} elif >=1.16 {
    /* public void drawContents(PoseStack drawContext, int mouseX, int mouseY, float partialTicks) { */
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
