package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.net.packets.PlacementMetaPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.InfoUtils;

import java.util.UUID;

public class Shere_ButtonActionListener implements IButtonActionListener {

    private final SchematicPlacement schematicPlacement;

    public Shere_ButtonActionListener(SchematicPlacement placement) {
        schematicPlacement = placement;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase buttonBase, int i) {
        if (!GuiBase.isShiftDown()) {
            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.share_without_shift");
            return;
        }

        buttonBase.setEnabled(false);

        if (schematicPlacement.getSchematicFile() == null) {
            // TODO: in memory placement
            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.file_is_null");
            Kyoyu.LOGGER.error("File is null\n\t> note: may be added in the future");
            return;
        }

        ((ISchematicPlacement) schematicPlacement).kyoyu$setKyoyuId(UUID.randomUUID());
        KyoyuPlacement kyoyuPlacement = ((ISchematicPlacement) schematicPlacement).kyoyu$toKyoyuPlacement();
        if (kyoyuPlacement != null) {
            PlacementMetaPacket placementMetaPacket = new PlacementMetaPacket(kyoyuPlacement);
            KyoyuPacketManager.sendC2S(placementMetaPacket);
        }
    }
}
//?}
