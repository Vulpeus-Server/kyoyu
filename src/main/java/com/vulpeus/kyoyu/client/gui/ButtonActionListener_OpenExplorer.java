package com.vulpeus.kyoyu.client.gui;

//? if client {
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import net.minecraft.client.gui.screen.Screen;

public class ButtonActionListener_OpenExplorer implements IButtonActionListener {

    private final Screen parent;

    public ButtonActionListener_OpenExplorer(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase buttonBase, int i) {

        GuiBase loading_gui = new Gui_LoadingExplorer();
        loading_gui.setParent(parent);
        GuiBase.openGui(loading_gui);

        // TODO
        //  Packet Process on OpenExplorer
    }
}
//?}
