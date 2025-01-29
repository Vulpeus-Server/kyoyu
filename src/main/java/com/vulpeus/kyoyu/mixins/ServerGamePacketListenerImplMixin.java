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

//? if >=1.20.2
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

//? if <=1.20.1 {
    /* import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket; */
//?} else {
    import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//?}


//? if <=1.20.1 {
/*
    import net.minecraft.resources.ResourceLocation;
    import net.minecraft.network.FriendlyByteBuf;
*/
//?}

@Mixin(
        //? if >=1.20.2 <1.20.5 {
            /* net.minecraft.server.network.ServerCommonPacketListenerImpl.class */
        //?} else {
            net.minecraft.server.network.ServerGamePacketListenerImpl.class
        //?}
)
public class ServerGamePacketListenerImplMixin {
    //? if >1.20.1 {
        @Shadow protected GameProfile playerProfile() { return null; }
    //?} else {
        /* @Shadow net.minecraft.server.level.ServerPlayer player; */
    //?}

    @Unique
    private CompatibleUtils.KyoyuPlayer getPlayer() {
        //? if >1.20.1 {
            return Kyoyu.PLAYERS.getServerPlayer(playerProfile().getId());
        //?} else {
            /* return new CompatibleUtils.KyoyuPlayer(player); */
        //?}
    }

    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayload(ServerboundCustomPayloadPacket customPayloadPacket, CallbackInfo ci) {
        KyoyuPacketPayload kyoyuPacketPayload = null;
        //? if >=1.20.2 {
            CustomPacketPayload payload = customPayloadPacket.payload();
            if (payload instanceof KyoyuPacketPayload) {
                kyoyuPacketPayload = (KyoyuPacketPayload) payload;
            }
        //?} else {
        /*
            //? if <=1.16.5 {
                ResourceLocation identifier = ((ServerboundCustomPayloadPacketIMixin) customPayloadPacket).getIdentifier();
                FriendlyByteBuf data = ((ServerboundCustomPayloadPacketIMixin) customPayloadPacket).getData();
            //?} else {
                ResourceLocation identifier = customPayloadPacket.getIdentifier();
                FriendlyByteBuf data = customPayloadPacket.getData();
            //?}
            if (identifier.equals(KyoyuPacketPayload.identifier)) {
                byte[] payload = data.readByteArray();
                kyoyuPacketPayload = new KyoyuPacketPayload(payload);
            }
        */
        //?}
        if (kyoyuPacketPayload != null) {
            kyoyuPacketPayload.onPacketServer(getPlayer());
            ci.cancel();
        }
    }
}
//?}
