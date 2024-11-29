package com.vulpeus.kyoyu.client.gui;

//? if client {
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import net.minecraft.client.gui.screens.Screen;

public class LoadExplorer_ButtonActionListener implements IButtonActionListener {

    private final Screen parent;

    public LoadExplorer_ButtonActionListener(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase buttonBase, int i) {

        GuiBase loading_gui = new LoadingExplorer_Gui();
        loading_gui.setParent(parent);
        GuiBase.openGui(loading_gui);

        // TODO
        //  Packet Process on OpenExplorer
        //  REQUEST LIST PACKET
    }
}
//?}
