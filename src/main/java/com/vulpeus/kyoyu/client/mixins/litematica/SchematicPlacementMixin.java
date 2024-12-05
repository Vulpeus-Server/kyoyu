package com.vulpeus.kyoyu.client.mixins.litematica;

//? if client {
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.LitematicHelper;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.malilib.util.JsonUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(SchematicPlacement.class)
public class SchematicPlacementMixin implements ISchematicPlacement {

    @Unique
    private UUID kyoyu_id;

    @Override
    public void kyoyu$setKyoyuId(UUID uuid) {
        kyoyu_id = uuid;
    }

    @Override
    public UUID kyoyu$getKyoyuId() {
        return kyoyu_id;
    }

    @Inject(method = "onModified(Lfi/dy/masa/litematica/schematic/placement/SchematicPlacementManager;)V", at = @At("HEAD"), remap = false)
    public void onModified(SchematicPlacementManager manager, CallbackInfo ci) {
        // TODO
        //  attention when connecting server is compatible
        //  if (getClient() != null)
    }

    @Inject(method = "toJson", at = @At("RETURN"), remap = false)
    public void saveToJson(CallbackInfoReturnable<JsonObject> cir) {
        JsonObject saveData = cir.getReturnValue();
        if (saveData != null) {
            saveData.add("kyoyu_id", new JsonPrimitive(kyoyu_id.toString()));
        }
    }

    @Inject(method = "fromJson", at = @At("RETURN"), remap = false, cancellable = true)
    private static void loadFromJson(JsonObject obj, CallbackInfoReturnable<SchematicPlacement> cir) {
        if (JsonUtils.hasString(obj, "kyoyu_id")) {
            SchematicPlacement self = cir.getReturnValue();
            if (self != null) {
                UUID uuid = UUID.fromString(obj.get("kyoyu_id").getAsString());
                ((ISchematicPlacement) self).kyoyu$setKyoyuId(uuid);
                cir.setReturnValue(null);

                KyoyuPlacement kyoyuPlacement = Kyoyu.findPlacement(uuid);
                if (kyoyuPlacement != null) {
                    LitematicHelper.newSchematicPlacementFromKyoyuPlacement(self, kyoyuPlacement);
                }
            }
        }
    }
}
//?}
