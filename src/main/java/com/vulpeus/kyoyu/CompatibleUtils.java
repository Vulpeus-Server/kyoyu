package com.vulpeus.kyoyu;

import net.minecraft.resources.ResourceLocation;

public class CompatibleUtils {
    public static ResourceLocation identifier(String namespace, String path) {
        //? if >1.20.6 {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?} elif =1.20.6 {
        /* return ResourceLocation.tryBuild(namespace, path); */
        //?} else {
        /* return new ResourceLocation(namespace, path); */
        //?}
    }
}
