package com.vulpeus.kyoyu.client.mixins;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.net.packets.HandshakePacket;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void onJoin(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {
        Kyoyu.LOGGER.info("Login to Server");
        KyoyuPacketManager.sendC2S(new HandshakePacket(Kyoyu.MOD_VERSION, Kyoyu.CONFIG));
    }
}
//?}
