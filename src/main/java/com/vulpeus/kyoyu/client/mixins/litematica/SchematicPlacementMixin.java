package com.vulpeus.kyoyu.client.mixins.litematica;

//? if client {
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.ISchematicPlacement;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.net.packets.PlacementMetaPacket;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import com.vulpeus.kyoyu.placement.KyoyuRegion;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.spongepowered.asm.mixin.Mixin;
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

    @Unique
    private UUID kyoyu_id;

    @Unique
    private boolean ignore_update = false;

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
                Minecraft.getInstance().player.getName().getString(),
                Minecraft.getInstance().player.getName().getString(),
                file
        );
    }

    @Unique
    private BlockPos blockPosMirror(BlockPos pos, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return new BlockPos(pos.getX(), pos.getY(), -pos.getZ());
            case FRONT_BACK:
                return new BlockPos(-pos.getX(), pos.getY(), pos.getZ());
        }
        return pos;
    }

    @Override
    public void kyoyu$updateFromKyoyuPlacement(KyoyuPlacement kyoyuPlacement) {
        SchematicPlacement self = (SchematicPlacement) (Object) this;
        this.ignore_update = true;
        if (self.isLocked()) {
            self.toggleLocked();
        }

        BlockPos originPos = kyoyuPlacement.getRegion().getPos();
        Mirror originMirror = kyoyuPlacement.getRegion().getMirror();
        Rotation originRotation = kyoyuPlacement.getRegion().getRotation();

        self.setOrigin(originPos, null);
        self.setMirror(originMirror, null);
        self.setRotation(originRotation, null);
        if (self.ignoreEntities() != kyoyuPlacement.getRegion().ignoreEntities()) {
            self.toggleIgnoreEntities(null);
        }
        self.setEnabled(kyoyuPlacement.getRegion().isEnable());

        for (KyoyuRegion subRegion: kyoyuPlacement.getSubRegions()) {
            String subRegionName = subRegion.getName();

            BlockPos regionPos = blockPosMirror(
                    subRegion.getPos(),
                    originMirror
            )
                    .rotate(originRotation)
                    .offset(originPos);

            self.moveSubRegionTo(subRegionName, regionPos, null);
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
        this.ignore_update = false;
    }

    @Unique
    private void onModified() {
        if (this.ignore_update) return;
        KyoyuClient instance = KyoyuClient.getInstance();
        if (instance == null) return;
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) return;
        if (!instance.serverConfig().isAllowedModify(localPlayer.getName().getString())) {
            InfoUtils.showGuiOrInGameMessage(Message.MessageType.ERROR, "kyoyu.error.disallowed_modify");
            return;
        }
        KyoyuPlacement kyoyuPlacement = this.kyoyu$toKyoyuPlacement();
        if (kyoyuPlacement == null) return;
        PlacementMetaPacket placementMetaPacket = new PlacementMetaPacket(kyoyuPlacement);
        KyoyuPacketManager.sendC2S(placementMetaPacket);
    }

    @Inject(method = "onModified(Lfi/dy/masa/litematica/schematic/placement/SchematicPlacementManager;)V", at = @At("RETURN"), remap = false)
    public void onModified(SchematicPlacementManager manager, CallbackInfo ci) {
        onModified();
    }

    @Inject(method = "onModified(Ljava/lang/String;Lfi/dy/masa/litematica/schematic/placement/SchematicPlacementManager;)V", at = @At("RETURN"), remap = false)
    public void onModified(String regionName, SchematicPlacementManager manager, CallbackInfo ci) {
        onModified();
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
