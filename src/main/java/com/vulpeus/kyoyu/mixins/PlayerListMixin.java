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

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    //? if >=1.20.2 {
        public void onPlayerConnect(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
    //?} else {
        /* public void onPlayerConnect(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) { */
    //?}
        CompatibleUtils.KyoyuPlayer player = new CompatibleUtils.KyoyuPlayer(serverPlayer);
        Kyoyu.PLAYERS.add(player.getUUID(), player.getName(), player);
    }
    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(ServerPlayer serverPlayer, CallbackInfo ci) {
        Kyoyu.PLAYERS.remove(serverPlayer.getUUID());
    }
}
//?}
