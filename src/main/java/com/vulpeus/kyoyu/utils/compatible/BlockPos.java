package com.vulpeus.kyoyu.utils.compatible;

public class BlockPos extends
        //? if !PAPER {
        net.minecraft.util.math.BlockPos
        //?} else {
        /* net.minecraft.core.BlockPos */
        //?}
{
    public BlockPos(int i, int j, int k) {
        super(i, j, k);
    }
}
