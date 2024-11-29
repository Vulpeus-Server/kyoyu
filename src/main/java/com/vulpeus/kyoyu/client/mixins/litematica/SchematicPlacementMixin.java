package com.vulpeus.kyoyu.client.mixins.litematica;

//? if client {
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SchematicPlacement.class)
public class SchematicPlacementMixin {
    @Inject(method = "onModified(Lfi/dy/masa/litematica/schematic/placement/SchematicPlacementManager;)V", at = @At("HEAD"), remap = false)
    public void onModified(SchematicPlacementManager manager, CallbackInfo ci) {
        // TODO
        //  attention when connecting server is compatible
        //  if (getClient() != null)
    }
}
//?}
