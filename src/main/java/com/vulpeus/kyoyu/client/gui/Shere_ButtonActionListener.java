package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.net.packets.PlacementMetaPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import com.vulpeus.kyoyu.placement.KyoyuRegion;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

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

        // TODO
        //  `schematicPlacement` add to client registry

        File placementFile = schematicPlacement.getSchematicFile();
        if (placementFile == null) {
            // TODO: in memory placement
            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.file_is_null");
            Kyoyu.LOGGER.error("File is null\n\t> note: may be added in the future");
            return;
        }

//        FileType fileType = FileType.fromFile(placementFile);
//        Kyoyu.LOGGER.info("getFileType {}", fileType.name());
//        if (fileType == FileType.VANILLA_STRUCTURE || fileType == FileType.SCHEMATICA_SCHEMATIC) {
//            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.share_incompatible_schematic");
//        } else if (fileType != FileType.LITEMATICA_SCHEMATIC) {
//            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.invalid_file");
//        }

        KyoyuPlacement kyoyuPlacement = new KyoyuPlacement(
                new KyoyuRegion(
                        schematicPlacement.getOrigin(),
                        schematicPlacement.getMirror(),
                        schematicPlacement.getRotation(),
                        schematicPlacement.getName()
                ),
                schematicPlacement.getAllSubRegionsPlacements().stream().map(x ->
                        new KyoyuRegion(x.getPos(), x.getMirror(), x.getRotation(), x.getName())
                ).collect(Collectors.toList()),
                Minecraft.getInstance().name(),
                Minecraft.getInstance().name(),
                placementFile
        );

        PlacementMetaPacket handshakePacket = new PlacementMetaPacket(Collections.singletonList(kyoyuPlacement));
        KyoyuPacketManager.sendC2S(handshakePacket);
    }
}
//?}
