package com.vulpeus.kyoyu.placement;

import com.vulpeus.kyoyu.utils.compatible.BlockMirror;
import com.vulpeus.kyoyu.utils.compatible.BlockPos;
import com.vulpeus.kyoyu.utils.compatible.BlockRotation;

public class KyoyuRegion {

    private final BlockPos pos;
    private final BlockMirror mirror;
    private final BlockRotation rotation;
    private final String name;

    public KyoyuRegion(BlockPos pos, BlockMirror mirror, BlockRotation rotation, String name) {
        this.pos = pos;
        this.mirror = mirror;
        this.rotation = rotation;
        this.name = name;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockMirror getMirror() {
        return mirror;
    }

    public BlockRotation getRotation() {
        return rotation;
    }

    public String getName() {
        return name;
    }
}
