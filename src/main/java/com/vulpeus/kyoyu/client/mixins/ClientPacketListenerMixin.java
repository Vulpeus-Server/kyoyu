package com.vulpeus.kyoyu.client.mixins;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import com.vulpeus.kyoyu.net.packets.HandshakePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.6 {
import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.net.KyoyuPacketPayload;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Unique;
import java.nio.charset.StandardCharsets;
//?}

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    //? if >=1.20.6
    @Unique private static final ResourceLocation MC_REGISTER_CHANNEL = CompatibleUtils.identifier("minecraft", "register");

    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void onJoin(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {

        //? if >=1.20.6 {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(MC_REGISTER_CHANNEL);
        buf.writeBytes(KyoyuPacketPayload.identifier.toString().getBytes(StandardCharsets.US_ASCII));
        Minecraft.getInstance().getConnection().send(ServerboundCustomPayloadPacket.STREAM_CODEC.decode(buf));
        //?}

        //? if >=1.19.3 {
            if (Minecraft.getInstance().isSingleplayer()) return;
        //?} else {
            /* if (Minecraft.getInstance().isLocalServer()) return; */
        //?}
        Kyoyu.LOGGER.info("Login to Server");
        KyoyuPacketManager.sendC2S(new HandshakePacket(Kyoyu.MOD_VERSION, Kyoyu.CONFIG));
    }
}
//?}
