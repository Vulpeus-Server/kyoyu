package com.vulpeus.kyoyu.utils.compatible;

//? if !PAPER {
import static net.minecraft.util.BlockRotation.*;
//?} else {
/* import static net.minecraft.world.level.block.Rotation.*; */
//?}

public enum BlockRotation {
    none(NONE),
    clockwise_90(CLOCKWISE_90),
    clockwise_180(CLOCKWISE_180),
    counterclockwise_90(COUNTERCLOCKWISE_90);

    //? !PAPER {
    private final net.minecraft.util.BlockRotation rotation;
    BlockRotation(net.minecraft.util.BlockRotation rotation) {
        this.rotation = rotation;
    }
    public net.minecraft.util.BlockRotation get() {
        return rotation;
    }
    //?} else {
    /*
    private final net.minecraft.world.level.block.Rotation rotation;
    BlockRotation(net.minecraft.world.level.block.Rotation rotation) {
        this.rotation = rotation;
    }
    public net.minecraft.world.level.block.Rotation get() {
        return rotation;
    }
    */
    //?}
}
