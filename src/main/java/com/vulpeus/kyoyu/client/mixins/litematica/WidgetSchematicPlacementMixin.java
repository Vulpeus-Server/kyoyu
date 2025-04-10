package com.vulpeus.kyoyu.client.mixins.litematica;

//? if client {
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.gui.Shere_ButtonActionListener;
import fi.dy.masa.litematica.gui.widgets.WidgetListSchematicPlacements;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.util.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WidgetSchematicPlacement.class)
public abstract class WidgetSchematicPlacementMixin extends WidgetListEntryBase<SchematicPlacement> {

    @Shadow(remap = false)
    public int buttonsStartX;

    @Final
    @Shadow(remap = false)
    public SchematicPlacement placement;

    protected WidgetSchematicPlacementMixin(int x, int y, int width, int height, @Nullable SchematicPlacement entry, int listIndex) {
        super(x, y, width, height, entry, listIndex);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void init(int x, int y, int width, int height, boolean isOdd, SchematicPlacement placement, int listIndex, WidgetListSchematicPlacements parent, CallbackInfo ci) {

        String text = StringUtils.translate("kyoyu.gui.button.kyoyu");
        ButtonGeneric shareButton = new ButtonGeneric(buttonsStartX, y + 1, getStringWidth(text) + 10, true, text);
        shareButton.setEnabled(((ISchematicPlacement) placement).kyoyu$getKyoyuId() == null);
        addButton(shareButton, new Shere_ButtonActionListener(placement));
        buttonsStartX = shareButton.getX() - 1;
    }
}
//?}
