package com.vulpeus.kyoyu.client.mixins.litematica;

//? if client {
import com.vulpeus.kyoyu.client.gui.LoadExplorer_ButtonActionListener;
import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin extends GuiBase {

    @Inject(method = "initGui", at = @At("RETURN"), remap = false)
    public void initGui(CallbackInfo ci) {
        String text = StringUtils.translate("kyoyu.gui.button.open_explorer");
        int width = getStringWidth(text) + 12;

        // 12 : litematica menu x-border size
        // fi.dy.masa.litematica.gui.GuiMainMenu.initGui ~ x = 12
        int x = Math.max(this.width / 2, this.width - 12 - width);
        int y = this.height - 26;

        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, text);
        // TODO
        //  Disable when connecting server is not compatible
        //  button.setEnabled();
        addButton(button, new LoadExplorer_ButtonActionListener(this));
    }
}
//?}
