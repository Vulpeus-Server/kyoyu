package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import com.vulpeus.kyoyu.placement.KyoyuRegion;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.gui.GuiMaterialList;
import fi.dy.masa.litematica.materials.MaterialListSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} elif >=1.16 {
/* import com.mojang.blaze3d.vertex.PoseStack; */
//?}

import java.util.Set;

public class Explorer_WidgetListEntry extends WidgetListEntryBase<KyoyuPlacement> {

    private final KyoyuPlacement kyoyuPlacement;
    private final boolean isOdd;

    public Explorer_WidgetListEntry(int x, int y, int width, int height, KyoyuPlacement entry, int listIndex, boolean isOdd) {
        super(x, y, width, height, entry, listIndex);
        this.kyoyuPlacement = entry;
        this.isOdd = isOdd;

        int buttonHeight = 20;
        int buttonY = (height - buttonHeight)/2 + y;
        int endX = width - 2;
        String text;
        int textWidth;
        ButtonGeneric button;
        ButtonListener listener;

        // REMOVE
        text = StringUtils.translate("kyoyu.gui.button.remove_litematic");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        button.setEnabled(true);
        listener = new ButtonListener(ButtonListener.Type.REMOVE, this);
        addButton(button, listener);


        // DOWNLOAD
        text = StringUtils.translate("kyoyu.gui.button.download_litematic");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        // TODO
        //  button.setEnabled(!kyoyuPlacement.existFile());
        listener = new ButtonListener(ButtonListener.Type.DOWNLOAD, this);
        addButton(button, listener);


        // LOAD
        text = StringUtils.translate("kyoyu.gui.button.load_litematic");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        // TODO
        //  button.setEnabled(kyoyuPlacement.existFile() && !getClient().isLoadedKyoyuPlacement(kyoyuPlacement));
        listener = new ButtonListener(ButtonListener.Type.LOAD, this);
        addButton(button, listener);


        // MATERIAL_LIST
        text = StringUtils.translate("kyoyu.gui.button.material_list");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        // TODO
        //  button.setEnabled(kyoyuPlacement.existFile());
        listener = new ButtonListener(ButtonListener.Type.MATERIAL_LIST, this);
        addButton(button, listener);
    }

    @Override
    //? if >=1.20 {
    public void render(int mouseX, int mouseY, boolean selected, GuiGraphics drawContext) {
    //?} elif >=1.16 {
    /* public void render(int mouseX, int mouseY, boolean selected, PoseStack drawContext) { */
    //?} else {
    /* public void render(int mouseX, int mouseY, boolean selected) { */
    //?}

        // Source: WidgetSchematicEntry
        RenderUtils.color(1f, 1f, 1f, 1f);

        // Draw a lighter background for the hovered and the selected entry
        if (selected || isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect(x, y, width, height, 0x70FFFFFF);
        } else if (isOdd) {
            RenderUtils.drawRect(x, y, width, height, 0x20FFFFFF);
        }
        // Draw a slightly lighter background for even entries
        else {
            RenderUtils.drawRect(x, y, width, height, 0x50FFFFFF);
        }

        String schematicName = kyoyuPlacement.getName();

        //? if >=1.16 {
        drawString(x + 20, y + 7, 0xFFFFFFFF, schematicName, drawContext);
        drawSubWidgets(mouseX, mouseY, drawContext);
        //?} else {
        /* drawSubWidgets(mouseX, mouseY); */
        //?}
    }

    private static class ButtonListener implements IButtonActionListener {

        private final Type type;
        private final Explorer_WidgetListEntry entry;

        public ButtonListener(Type type, Explorer_WidgetListEntry entry) {
            this.type = type;
            this.entry = entry;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int arg1) {
            if (type == null) {
                return;
            }
            button.setEnabled(false);
            Kyoyu.LOGGER.info("{} on click {}", entry.kyoyuPlacement.getName(), type.name());
            this.type.onAction(entry);
        }

        public enum Type {
            MATERIAL_LIST() {
                @Override
                void onAction(Explorer_WidgetListEntry entry) {
                    LitematicaSchematic schematic = SchematicHolder.getInstance().getOrLoad(entry.kyoyuPlacement.getFile());
                    Set<String> regionNames= schematic.getAreas().keySet();
                    MaterialListSchematic materialList = new MaterialListSchematic(schematic, regionNames, true);
                    DataManager.setMaterialList(materialList);
                    GuiBase gui = new GuiMaterialList(materialList);

                    gui.setTitle(StringUtils.translate("litematica.gui.title.material_list.select_schematic_regions", schematic.getMetadata().getName()));
                    gui.setParent(GuiUtils.getCurrentScreen());
                    GuiBase.openGui(gui);
                }
            },
            LOAD() {
                @Override
                void onAction(Explorer_WidgetListEntry entry) {
                    String name = entry.kyoyuPlacement.getName();
                    BlockPos pos = entry.kyoyuPlacement.getRegion().getPos();
                    Mirror mirror = entry.kyoyuPlacement.getRegion().getMirror();
                    Rotation rotation = entry.kyoyuPlacement.getRegion().getRotation();

                    LitematicaSchematic schematic = SchematicHolder.getInstance().getOrLoad(entry.kyoyuPlacement.getFile());
                    SchematicPlacement placement = SchematicPlacement.createFor(schematic, pos, name, true, true);

                    if (placement.isLocked()) {
                        placement.toggleLocked();
                    }

                    placement.setMirror(mirror, null);
                    placement.setRotation(rotation, null);
                    for (KyoyuRegion subRegion: entry.kyoyuPlacement.getSubRegions()) {
                        String subRegionName = subRegion.getName();

                        placement.moveSubRegionTo(subRegionName, subRegion.getPos(), null);
                        placement.setSubRegionMirror(subRegionName, subRegion.getMirror(), null);
                        placement.setSubRegionRotation(subRegionName, subRegion.getRotation(), null);
                    }
                    placement.toggleLocked();

                    DataManager.getSchematicPlacementManager().addSchematicPlacement(placement, true);
                    // TODO
                    //  getClient().add(entry.kyoyuPlacement, placement);
                }
            },
            DOWNLOAD() {
                @Override
                void onAction(Explorer_WidgetListEntry entry) {
                    // TODO
                    //  Packet Process on Download
                    //  REQUEST Download File
                }
            },
            REMOVE() {
                @Override
                void onAction(Explorer_WidgetListEntry entry) {
                    // TODO
                    //  Packet Process on Remove
                    //  REQUEST Remove KyoyuPlacement
                }
            };

            abstract void onAction(Explorer_WidgetListEntry entry);
        }

    }
}
//?}
