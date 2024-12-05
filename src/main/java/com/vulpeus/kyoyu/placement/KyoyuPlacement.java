package com.vulpeus.kyoyu.placement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vulpeus.kyoyu.Kyoyu;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class KyoyuPlacement {

    private final UUID uuid;
    private final KyoyuRegion region;
    private final List<KyoyuRegion> subRegions;
    private final String ownerName;
    private String updaterName;
    private final String filename;
    private transient File file = null;

    public KyoyuPlacement(KyoyuRegion region, List<KyoyuRegion> subRegions, String ownerName, String updaterName, File file) {
        this(UUID.randomUUID(), region, subRegions, ownerName, updaterName, file);
    }

    public KyoyuPlacement(UUID uuid, KyoyuRegion region, List<KyoyuRegion> subRegions, String ownerName, String updaterName, File file) {
        this.uuid = uuid;
        this.region = region;
        this.subRegions = subRegions;
        this.ownerName = ownerName;
        this.updaterName = updaterName;
        this.filename = file.getName();
        this.file = file;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return this.region.getName();
    }

    public List<KyoyuRegion> getSubRegions() {
        return subRegions;
    }

    public KyoyuRegion getRegion() {
        return region;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public File getFile() {
        return file;
    }

    public boolean existFile() {
        return file != null && file.exists();
    }

    private static final Type listType = new TypeToken<List<KyoyuPlacement>>() {}.getType();
    public static List<KyoyuPlacement> fromJsonList(String json) {
        Gson gson = new Gson();
        List<KyoyuPlacement> kyoyuPlacementList = gson.fromJson(json, listType);
        for (KyoyuPlacement kyoyuPlacement: kyoyuPlacementList) {
            kyoyuPlacement.file = Kyoyu.getSaveSchemeDir().resolve(kyoyuPlacement.filename).toFile();
        }
        return kyoyuPlacementList;
    }

    public static String toJsonList(List<KyoyuPlacement> kyoyuPlacementList) {
        List<KyoyuPlacement> target = new LinkedList<KyoyuPlacement>(kyoyuPlacementList);
        Gson gson = new Gson();
        return gson.toJson(target, listType);
    }

    public static KyoyuPlacement fromJson(String json) {
        Gson gson = new Gson();
        KyoyuPlacement kyoyuPlacement = gson.fromJson(json, KyoyuPlacement.class);
        kyoyuPlacement.file = Kyoyu.getSaveSchemeDir().resolve(kyoyuPlacement.filename).toFile();
        return kyoyuPlacement;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
