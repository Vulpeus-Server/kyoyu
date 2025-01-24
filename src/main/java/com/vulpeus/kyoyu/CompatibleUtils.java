package com.vulpeus.kyoyu;

import net.minecraft.resources.ResourceLocation;

//? if >=1.19 {
    import net.minecraft.network.chat.Component;
    import net.minecraft.network.chat.MutableComponent;
//?} else {
    /* import net.minecraft.network.chat.TextComponent; */
//?}

public class CompatibleUtils {
    //? if >=1.19 {
        public static MutableComponent text(String msg) { return Component.literal(msg); }
    //?} else {
        /* public static TextComponent text(String msg) { return new TextComponent(msg); } */
    //?}
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
