package com.vulpeus.kyoyu.client;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.placement.KyoyuPlacement;

import java.util.List;
import java.util.UUID;

//? if client {
import com.vulpeus.kyoyu.client.gui.Explorer_GuiList;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.GuiUtils;
//?}

public class KyoyuClient {

    private static KyoyuClient instance = null;

    private final String serverVersion;

    public KyoyuClient(String serverVersion) {
        this.serverVersion = serverVersion;
    }
    private String serverVersion() {
        return serverVersion;
    }

    public static void init(String serverVersion) {
        if (instance != null) {
            Kyoyu.LOGGER.error("Duplicate client init");
            return;
        }
        Kyoyu.LOGGER.info("Kyoyu client init");
        instance = new KyoyuClient(serverVersion);
    }

    public static void deinitClient() {
        if (instance != null) {
            Kyoyu.LOGGER.info("Kyoyu client deinit");
            instance = null;
        }
    }

    public static KyoyuClient getInstance() {
        return instance;
    }

//? if client {
    public void openExplorer(List<KyoyuPlacement> kyoyuPlacementList) {
        if (Kyoyu.isClient() && KyoyuClient.getInstance() != null && GuiUtils.getCurrentScreen() instanceof GuiBase) {
            GuiBase currentGui = (GuiBase) GuiUtils.getCurrentScreen();
            GuiBase.openGui(new Explorer_GuiList(currentGui.getParent(), kyoyuPlacementList));
        }
    }

    public SchematicPlacement findSchematicPlacement(UUID uuid) {
        for (SchematicPlacement schematicPlacement: DataManager.getSchematicPlacementManager().getAllSchematicsPlacements()) {
            UUID id = ((ISchematicPlacement) schematicPlacement).kyoyu$getKyoyuId();
            if (id == uuid) {
                return schematicPlacement;
            }
        }
        return null;
    }
//?}
}
