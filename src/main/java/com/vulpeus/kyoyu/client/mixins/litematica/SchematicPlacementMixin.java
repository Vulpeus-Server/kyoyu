package com.vulpeus.kyoyu.client.mixins.litematica;

//? if client {
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import com.vulpeus.kyoyu.placement.KyoyuRegion;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(SchematicPlacement.class)
public class SchematicPlacementMixin implements ISchematicPlacement {

    @Shadow private BlockPos origin;
    @Unique
    private UUID kyoyu_id;

    @Override
    public void kyoyu$setKyoyuId(UUID uuid) {
        this.kyoyu_id = uuid;
    }

    @Override
    public UUID kyoyu$getKyoyuId() {
        return this.kyoyu_id;
    }

    @Override
    public KyoyuPlacement kyoyu$toKyoyuPlacement() {
        if (this.kyoyu_id == null) {
            return null;
        }

        SchematicPlacement self = (SchematicPlacement) (Object) this;
        File file = self.getSchematicFile();
        if (file == null) {
            LitematicaSchematic litematicaSchematic = self.getSchematic();
            litematicaSchematic.writeToFile(Kyoyu.getSaveSchemeDir().toFile(), "temporary.litematic", true);
            file = Kyoyu.getSaveSchemeDir().resolve("temporary.litematic").toFile();
        }
        return new KyoyuPlacement(
                this.kyoyu_id,
                new KyoyuRegion(
                        self.getOrigin(),
                        self.getMirror(),
                        self.getRotation(),
                        self.getName(),
                        self.ignoreEntities(),
                        self.isEnabled()
                ),
                self.getAllSubRegionsPlacements().stream().map(x ->
                        new KyoyuRegion(
                                x.getPos(),
                                x.getMirror(),
                                x.getRotation(),
                                x.getName(),
                                x.ignoreEntities(),
                                x.isEnabled()
                        )
                ).collect(Collectors.toList()),
                Minecraft.getInstance().name(),
                Minecraft.getInstance().name(),
                file
        );
    }

    @Override
    public void kyoyu$updateFromKyoyuPlacement(KyoyuPlacement kyoyuPlacement) {
        SchematicPlacement self = (SchematicPlacement) (Object) this;
        if (self.isLocked()) {
            self.toggleLocked();
        }

        self.setMirror(kyoyuPlacement.getRegion().getMirror(), null);
        self.setRotation(kyoyuPlacement.getRegion().getRotation(), null);
        if (self.ignoreEntities() != kyoyuPlacement.getRegion().ignoreEntities()) {
            self.toggleIgnoreEntities(null);
        }
        self.setEnabled(kyoyuPlacement.getRegion().isEnable());

        BlockPos origin = kyoyuPlacement.getRegion().getPos();
        for (KyoyuRegion subRegion: kyoyuPlacement.getSubRegions()) {
            String subRegionName = subRegion.getName();

            self.moveSubRegionTo(subRegionName, subRegion.getPos().offset(origin.getX(), origin.getY(), origin.getZ()), null);
            self.setSubRegionMirror(subRegionName, subRegion.getMirror(), null);
            self.setSubRegionRotation(subRegionName, subRegion.getRotation(), null);

            SubRegionPlacement subRegionPlacement = self.getRelativeSubRegionPlacement(subRegionName);
            if (subRegionPlacement != null && subRegionPlacement.ignoreEntities() != subRegion.ignoreEntities()) {
                self.toggleSubRegionIgnoreEntities(subRegionName, null);
            }
            if (subRegionPlacement != null && subRegionPlacement.isEnabled() != subRegion.isEnable()) {
                self.toggleSubRegionEnabled(subRegionName, null);
            }
        }
        self.toggleLocked();
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
        if (saveData != null && this.kyoyu_id != null) {
            saveData.add("kyoyu_id", new JsonPrimitive(kyoyu_id.toString()));
        }
    }

    @Inject(method = "fromJson", at = @At("RETURN"), remap = false)
    private static void loadFromJson(JsonObject obj, CallbackInfoReturnable<SchematicPlacement> cir) {
        if (JsonUtils.hasString(obj, "kyoyu_id")) {
            SchematicPlacement self = cir.getReturnValue();
            if (self != null) {
                UUID uuid = UUID.fromString(obj.get("kyoyu_id").getAsString());
                ((ISchematicPlacement) self).kyoyu$setKyoyuId(uuid);
            }
        }
    }
}
//?}
