package com.vulpeus.kyoyu.client.mixins;

//? if client {
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.client.KyoyuClient;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(method = "channelInactive", at = @At("HEAD"), remap = false)
    private void onLeave(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (KyoyuClient.getInstance() == null) return;
        Kyoyu.LOGGER.info("Leave from Server");
        KyoyuClient.deinit();
    }
}
//?}
