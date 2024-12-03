package com.vulpeus.kyoyu.placement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class KyoyuPlacement {

    private final KyoyuRegion region;
    private final List<KyoyuRegion> subRegions;
    private final String ownerName;
    private String updaterName;
//    private final String hash;
    private transient File file;

    public KyoyuPlacement(KyoyuRegion region, List<KyoyuRegion> subRegions, String ownerName, String updaterName, File file) {
        this.region = region;
        this.subRegions = subRegions;
        this.ownerName = ownerName;
        this.updaterName = updaterName;
        this.file = file;
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

    public File getFile() {
        return file;
    }

    private static final Type listType = new TypeToken<List<KyoyuPlacement>>() {}.getType();
    public static List<KyoyuPlacement> fromJson(String json) {
        Gson gson = new Gson();
        List<KyoyuPlacement> kyoyuPlacementList = gson.fromJson(json, listType);
        for (KyoyuPlacement kyoyuPlacement: kyoyuPlacementList) {
            // TODO
            //  some process for kyoyuPlacement
        }
        return kyoyuPlacementList;
    }

    public static String toJson(List<KyoyuPlacement> kyoyuPlacementList) {
        List<KyoyuPlacement> target = new LinkedList<KyoyuPlacement>(kyoyuPlacementList);
        Gson gson = new Gson();
        return gson.toJson(target, listType);
    }
}
