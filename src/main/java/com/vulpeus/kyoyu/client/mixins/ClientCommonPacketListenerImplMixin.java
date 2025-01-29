package com.vulpeus.kyoyu.client.mixins;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.KyoyuPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.2
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

//? if <=1.20.1 {
    /* import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket; */
//?} else {
    import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//?}

@Mixin(
        //? if >=1.20.2 {
            net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl.class
        //?} else {
            /* net.minecraft.client.multiplayer.ClientPacketListener.class */
        //?}
)
public class ClientCommonPacketListenerImplMixin {
    @Unique
    private final String targetMethod =
            //? if >=1.20.2 {
                "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V";
            //?} else {
                /* "handleCustomPayload"; */
            //?}

    @Inject(method = targetMethod, at = @At("HEAD"), cancellable = true)
    private void handleCustomPayload(ClientboundCustomPayloadPacket customPayloadPacket, CallbackInfo ci) {
        KyoyuPacketPayload kyoyuPacketPayload = null;
        //? if >=1.20.2 {
            CustomPacketPayload payload = customPayloadPacket.payload();
            if (payload instanceof KyoyuPacketPayload) {
                kyoyuPacketPayload = (KyoyuPacketPayload) payload;
            }
        //?} else {
        /*
            if (customPayloadPacket.getIdentifier().equals(KyoyuPacketPayload.identifier)) {
                byte[] payload = customPayloadPacket.getData().readByteArray();
                kyoyuPacketPayload = new KyoyuPacketPayload(payload);
            }
        */
        //?}
        if (kyoyuPacketPayload != null) {
            kyoyuPacketPayload.onPacketClient();
            ci.cancel();
        }
    }
}
//?}
