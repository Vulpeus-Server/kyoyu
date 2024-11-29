package com.vulpeus.kyoyu.client.gui;

//? if client {
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;

public class Shere_ButtonActionListener implements IButtonActionListener {

    private final SchematicPlacement schematicPlacement;
    private final GuiBase messageDisplay;

    public Shere_ButtonActionListener(SchematicPlacement placement, GuiBase messageDisplay) {
        schematicPlacement = placement;
        this.messageDisplay = messageDisplay;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase buttonBase, int i) {
        if (!GuiBase.isShiftDown()) {
            messageDisplay.addMessage(Message.MessageType.ERROR, "kyoyu.error.share_without_shift");
            return;
        }

        buttonBase.setEnabled(false);

        // TODO
        //  `schematicPlacement` add to client registry

        // TODO
        //  Packet Process on Share scheme
    }
}
//?}
