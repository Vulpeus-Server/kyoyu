package com.vulpeus.kyoyu.utils.compatible;

//? if !PAPER {
import static net.minecraft.util.BlockMirror.*;
//?} else {
/* import static net.minecraft.world.level.block.Mirror.*; */
//?}

public enum BlockMirror {
    none(NONE),
    left_right(LEFT_RIGHT),
    front_back(FRONT_BACK);

    //? if !PAPER {
    private final net.minecraft.util.BlockMirror mirror;
    BlockMirror(net.minecraft.util.BlockMirror mirror) {
        this.mirror = mirror;
    }
    public net.minecraft.util.BlockMirror get() {
        return mirror;
    }
    //?} else {
    /*
    private final net.minecraft.world.level.block.Mirror mirror;
    BlockMirror(net.minecraft.world.level.block.Mirror mirror) {
        this.mirror = mirror;
    }
    public net.minecraft.world.level.block.Mirror get() {
        return mirror;
    }
    */
    //?}
}
