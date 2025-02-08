package com.vulpeus.kyoyu.mixins;

//? if !PAPER && >=1.20.2 <=1.20.4 {
/*
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.KyoyuPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerboundCustomPayloadPacket.class)
public class ServerboundCustomPayloadPacketMixin {
    @Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
    private static void onReadPayload(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf, CallbackInfoReturnable<CustomPacketPayload> cir) {
        if (resourceLocation.equals(KyoyuPacketPayload.identifier)) {
            cir.setReturnValue(new KyoyuPacketPayload(friendlyByteBuf));
        }
    }
}
*/
//?}