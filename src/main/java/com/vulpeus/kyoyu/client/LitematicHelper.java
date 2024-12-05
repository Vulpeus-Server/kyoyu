package com.vulpeus.kyoyu.client;

//? if client {
import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import com.vulpeus.kyoyu.placement.KyoyuRegion;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;

public class LitematicHelper {

    public static void newSchematicPlacementFromKyoyuPlacement(SchematicPlacement schematicPlacement, KyoyuPlacement kyoyuPlacement) {

        changeSchematicPlacementFromKyoyuPlacement(schematicPlacement, kyoyuPlacement);
        DataManager.getSchematicPlacementManager().addSchematicPlacement(schematicPlacement, true);
        ((ISchematicPlacement) schematicPlacement).kyoyu$setKyoyuId(kyoyuPlacement.getUuid());

    }

    public static void changeSchematicPlacementFromKyoyuPlacement(SchematicPlacement schematicPlacement, KyoyuPlacement kyoyuPlacement) {

        if (schematicPlacement.isLocked()) {
            schematicPlacement.toggleLocked();
        }

        schematicPlacement.setMirror(kyoyuPlacement.getRegion().getMirror(), null);
        schematicPlacement.setRotation(kyoyuPlacement.getRegion().getRotation(), null);
        for (KyoyuRegion subRegion: kyoyuPlacement.getSubRegions()) {
            String subRegionName = subRegion.getName();

            schematicPlacement.moveSubRegionTo(subRegionName, subRegion.getPos(), null);
            schematicPlacement.setSubRegionMirror(subRegionName, subRegion.getMirror(), null);
            schematicPlacement.setSubRegionRotation(subRegionName, subRegion.getRotation(), null);
        }
        schematicPlacement.toggleLocked();

    }
}
//?}
