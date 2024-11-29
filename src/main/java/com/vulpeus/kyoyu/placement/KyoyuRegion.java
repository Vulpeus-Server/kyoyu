package com.vulpeus.kyoyu.placement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public class KyoyuRegion {

    private final BlockPos pos;
    private final Mirror mirror;
    private final Rotation rotation;
    private final String name;

    public KyoyuRegion(BlockPos pos, Mirror mirror, Rotation rotation, String name) {
        this.pos = pos;
        this.mirror = mirror;
        this.rotation = rotation;
        this.name = name;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public String getName() {
        return name;
    }
}
