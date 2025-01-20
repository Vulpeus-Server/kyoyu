package com.vulpeus.kyoyu.mixins;

//? if !PAPER {
import com.mojang.authlib.GameProfile;
import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.KyoyuPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


//? if >=1.20.2 {
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

//? if >=1.20.2 <1.20.5 {
    /* import net.minecraft.server.network.ServerCommonPacketListenerImpl; */
//?} else {
    import net.minecraft.server.network.ServerGamePacketListenerImpl;
//?}

//? if <=1.19.4 {
    /* import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket; */
//?} else {
    import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//?}


@Mixin(
        //? if >=1.20.2 <1.20.5 {
            /* ServerCommonPacketListenerImpl.class */
        //?} else {
            ServerGamePacketListenerImpl.class
        //?}
)
public class ServerGamePacketListenerImplMixin {
    //? if >1.19.4 {
        @Shadow protected GameProfile playerProfile() { return null; }
    //?} else {
        /* @Shadow ServerPlayer player; */
    //?}

    @Unique
    private CompatibleUtils.KyoyuPlayer getPlayer() {
        //? if >1.19.4 {
            return Kyoyu.PLAYERS.getServerPlayer(playerProfile().getId());
        //?} else {
            /* return new CompatibleUtils.KyoyuPlayer(player); */
        //?}
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayload(ServerboundCustomPayloadPacket customPayloadPacket, CallbackInfo ci) {
        //? if >=1.20.2 {
            CustomPacketPayload payload = customPayloadPacket.payload();
            if (payload instanceof KyoyuPacketPayload) {
                KyoyuPacketPayload kyoyuPacketPayload = (KyoyuPacketPayload) payload;
                // handle(player)
                //? if <1.20.5 {
                /*
                    // handle
                    Kyoyu.LOGGER.info("{}", getPlayer().getName());
                */
                //?} else {
                    // handle
                    Kyoyu.LOGGER.info("{}", getPlayer().getName());
                //?}
                ci.cancel();
            }
        //?} else {
        /*
            if (customPayloadPacket.getIdentifier().equals(KyoyuPacketPayload.identifier)) {
                byte[] payload = customPayloadPacket.getData().readByteArray();
                Kyoyu.LOGGER.info("{}", getPlayer().getName());
                // handle(player)
                ci.cancel();
            }
        */
        //?}
    }
}
//?}
