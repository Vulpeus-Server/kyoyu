package com.vulpeus.kyoyu.client.gui;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.net.packets.FileRequestPacket;
import com.vulpeus.kyoyu.net.packets.RemovePlacementPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.gui.GuiMaterialList;
import fi.dy.masa.litematica.materials.MaterialListSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.Minecraft;

//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} elif >=1.16 {
/* import com.mojang.blaze3d.vertex.PoseStack; */
//?}

import java.util.Map;
import java.util.Set;

public class Explorer_WidgetListEntry extends WidgetListEntryBase<KyoyuPlacement> {

    private final KyoyuPlacement kyoyuPlacement;
    private final Map<Explorer_WidgetList.SortKey, Integer> columnWidth;

    public Explorer_WidgetListEntry(int x, int y, int width, int height, KyoyuPlacement entry, int listIndex, Map<Explorer_WidgetList.SortKey, Integer> columnWidth) {
        super(x, y, width, height, entry, listIndex);
        this.kyoyuPlacement = entry;
        this.columnWidth = columnWidth;
        
        KyoyuClient kyoyuClient = KyoyuClient.getInstance();

        int buttonHeight = 20;
        int buttonY = (height - buttonHeight)/2 + y;
        int endX = width - 2;
        String text;
        int textWidth;
        ButtonGeneric button;
        ButtonListener listener;

        // REMOVE
        text = StringUtils.translate("kyoyu.gui.button.remove");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        button.setEnabled(kyoyuClient.serverConfig().isAllowedRemove(Minecraft.getInstance().player.getName().getString()));
        listener = new ButtonListener(ButtonListener.Type.REMOVE, this);
        addButton(button, listener);


        // DOWNLOAD
        text = StringUtils.translate("kyoyu.gui.button.download");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        button.setEnabled(!kyoyuPlacement.existFile());
        listener = new ButtonListener(ButtonListener.Type.DOWNLOAD, this);
        addButton(button, listener);


        // LOAD
        text = StringUtils.translate("kyoyu.gui.button.load");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        button.setEnabled(kyoyuPlacement.existFile() && kyoyuClient.findSchematicPlacement(kyoyuPlacement.getUuid()) == null);
        listener = new ButtonListener(ButtonListener.Type.LOAD, this);
        addButton(button, listener);


        // MATERIAL_LIST
        text = StringUtils.translate("kyoyu.gui.button.material_list");
        textWidth = getStringWidth(text) + 10;
        endX -= textWidth + 2;
        button = new ButtonGeneric(endX, buttonY, textWidth, buttonHeight, text);
        button.setEnabled(kyoyuPlacement.existFile());
        listener = new ButtonListener(ButtonListener.Type.MATERIAL_LIST, this);
        addButton(button, listener);
    }
    @FunctionalInterface
    interface CompatibleDrawString {
        void apply(int x, int y, int color, String text);
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
//        } else if (this.isOdd) {
//            RenderUtils.drawRect(x, y, width, height, 0x20FFFFFF);
        }
        // Draw a slightly lighter background for even entries
        else {
            RenderUtils.drawRect(x, y, width, height, 0x50FFFFFF);
        }

        int x = this.x + 20;
        int y = this.y + 7;

        CompatibleDrawString drawStringCompatible = (x_, y_, color_, text_) ->
                //? if >=1.16 {
                    drawString(x_, y_, color_, text_, drawContext);
                //?} else {
                    /* drawString(x_, y_, color_, text_); */
                //?}

        for (Explorer_WidgetList.SortKey key: Explorer_WidgetList.SortKey.ALL) {
            drawStringCompatible.apply(x, y, 0xFFFFFFFF, key.comparator().apply(kyoyuPlacement));
            x += this.columnWidth.get(key) + 5;
        }

        //? if >=1.16 {
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
            button.setEnabled(!this.type.onAction(entry));
            Kyoyu.LOGGER.info("{} on click {}", entry.kyoyuPlacement.getName(), type.name());
        }

        public enum Type {
            MATERIAL_LIST() {
                @Override
                boolean onAction(Explorer_WidgetListEntry entry) {
                    LitematicaSchematic schematic = SchematicHolder.getInstance().getOrLoad(entry.kyoyuPlacement.getFile());
                    Set<String> regionNames= schematic.getAreas().keySet();
                    MaterialListSchematic materialList = new MaterialListSchematic(schematic, regionNames, true);
                    DataManager.setMaterialList(materialList);
                    GuiBase gui = new GuiMaterialList(materialList);

                    gui.setTitle(StringUtils.translate("litematica.gui.title.material_list.select_schematic_regions", schematic.getMetadata().getName()));
                    gui.setParent(GuiUtils.getCurrentScreen());
                    GuiBase.openGui(gui);
                    return true;
                }
            },
            LOAD() {
                @Override
                boolean onAction(Explorer_WidgetListEntry entry) {

                    SchematicPlacement placement = SchematicPlacement.createFor(
                            SchematicHolder.getInstance().getOrLoad(entry.kyoyuPlacement.getFile()),
                            entry.kyoyuPlacement.getRegion().getPos(),
                            entry.kyoyuPlacement.getName(),
                            true,
                            true
                    );
                    ((ISchematicPlacement) placement).kyoyu$updateFromKyoyuPlacement(entry.kyoyuPlacement);
                    ((ISchematicPlacement) placement).kyoyu$setKyoyuId(entry.kyoyuPlacement.getUuid());
                    DataManager.getSchematicPlacementManager().addSchematicPlacement(placement, true);
                    return true;
                }
            },
            DOWNLOAD() {
                @Override
                boolean onAction(Explorer_WidgetListEntry entry) {
                    FileRequestPacket fileRequestPacket = new FileRequestPacket(entry.kyoyuPlacement.getUuid());
                    KyoyuPacketManager.sendC2S(fileRequestPacket);
                    return true;
                }
            },
            REMOVE() {
                @Override
                boolean onAction(Explorer_WidgetListEntry entry) {
                    if (!GuiBase.isShiftDown()) {
                        InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.remove_without_shift");
                        return false;
                    }
                    RemovePlacementPacket removePlacementPacket = new RemovePlacementPacket(entry.kyoyuPlacement.getUuid());
                    KyoyuPacketManager.sendC2S(removePlacementPacket);
                    return true;
                }
            };

            abstract boolean onAction(Explorer_WidgetListEntry entry);
        }

    }
}
//?}
