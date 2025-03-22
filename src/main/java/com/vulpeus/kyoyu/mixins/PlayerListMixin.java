package com.vulpeus.kyoyu.mixins;

//? if !PAPER {
import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.2
import net.minecraft.server.network.CommonListenerCookie;

//? if >=1.20.6 {
import com.vulpeus.kyoyu.net.KyoyuPacketPayload;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Unique;
import java.nio.charset.StandardCharsets;
//?}

@Mixin(PlayerList.class)
public class PlayerListMixin {

    //? if >=1.20.6
    @Unique private static final ResourceLocation MC_REGISTER_CHANNEL = CompatibleUtils.identifier("minecraft", "register");

    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    //? if >=1.20.2 {
        public void onPlayerConnect(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
    //?} else {
        /* public void onPlayerConnect(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) { */
    //?}
        CompatibleUtils.KyoyuPlayer player = new CompatibleUtils.KyoyuPlayer(serverPlayer);
        Kyoyu.PLAYERS.add(player.getUUID(), player.getName(), player);

        //? if >=1.20.6 {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeResourceLocation(MC_REGISTER_CHANNEL);
            buf.writeBytes(KyoyuPacketPayload.identifier.toString().getBytes(StandardCharsets.US_ASCII));
            connection.send(ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC.decode(buf));
        //?}
    }

    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(ServerPlayer serverPlayer, CallbackInfo ci) {
        Kyoyu.PLAYERS.remove(serverPlayer.getUUID());
    }
}
//?}
