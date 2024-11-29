package com.vulpeus.kyoyu.placement;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class KyoyuPlacement {

    private final UUID id;
    private final KyoyuRegion region;
    private final List<KyoyuRegion> subRegions;
    private final String ownerName;
    private String updaterName;
//    private final String hash;
    private transient File file;

    public KyoyuPlacement(UUID id, KyoyuRegion region, List<KyoyuRegion> subRegions, String ownerName, String updaterName, File file) {
        this.id = id;
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
}
