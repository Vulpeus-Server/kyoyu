package com.vulpeus.kyoyu.mixins;

//? if !PAPER && >=1.20.6 {
import com.vulpeus.kyoyu.net.KyoyuPacketPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/network/protocol/common/custom/CustomPacketPayload$1")
public abstract class CustomPacketPayload1Mixin<B extends FriendlyByteBuf> implements StreamCodec<B, CustomPacketPayload> {
    @Inject(method = "findCodec", at = @At("HEAD"), cancellable = true)
    private void findCodec(ResourceLocation id, CallbackInfoReturnable<StreamCodec<RegistryFriendlyByteBuf, ?>> cir) {
        if (id.equals(KyoyuPacketPayload.identifier)) {
            cir.setReturnValue(KyoyuPacketPayload.CODEC);
        }
    }
}
//?}
