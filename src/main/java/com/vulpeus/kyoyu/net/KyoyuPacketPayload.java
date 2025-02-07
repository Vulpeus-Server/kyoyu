package com.vulpeus.kyoyu.net;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.FriendlyByteBuf;

//? if >=1.20.2 {
    import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
    import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
    import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
    /*
    import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
    import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
    import io.netty.buffer.Unpooled;
    */
//?}

//? if >=1.20.6 {
    import net.minecraft.network.codec.ByteBufCodecs;
    import net.minecraft.network.codec.StreamCodec;
    import net.minecraft.network.RegistryFriendlyByteBuf;
//?}


//? if !PAPER {
    import net.minecraft.client.Minecraft;
//?}

public class KyoyuPacketPayload
        //? if >=1.20.2
        implements CustomPacketPayload
{

    public byte[] content;
    public KyoyuPacketPayload(byte[] content) {
        this.content = content;
    }

    public KyoyuPacketPayload(FriendlyByteBuf input) {
        this(input.readByteArray());
    }

    public byte[] content() {
        return this.content;
    }


    public static final ResourceLocation identifier = CompatibleUtils.identifier(Kyoyu.MOD_ID, "kyoyu");

    //? if >=1.20.6 {
        private static final StreamCodec<RegistryFriendlyByteBuf, KyoyuPacketPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.BYTE_ARRAY, KyoyuPacketPayload::content, KyoyuPacketPayload::new
        );
        private static final Type<KyoyuPacketPayload> TYPE = new Type<>(identifier);
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    //?}

    //? if >=1.20.2 <1.20.5 {
    /* @Override */
    //?}
    public ResourceLocation id() {
        return identifier;
    }

    //? if >=1.20.2 <1.20.5 {
    /* @Override */
    //?}
    public void write(FriendlyByteBuf output) {
        output.writeByteArray(content);
    }

    public static void register() {
        //? if FABRIC && >=1.20.6 {
            net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S().register(TYPE, CODEC);
            net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
        //?}
    }

    public void sendC2S() {
        //? !PAPER {
            //? >=1.20.2 {
                Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(this));
            //?} else {
            /*
                FriendlyByteBuf packetbb = new FriendlyByteBuf(Unpooled.buffer());
                write(packetbb);
                Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(identifier, packetbb));
            */
            //?}
        //?}
    }
    public void sendS2C(CompatibleUtils.KyoyuPlayer player) {
        //? !PAPER {
            //? >=1.20.2 {
                player.player().connection.send(new ClientboundCustomPayloadPacket(this));
            //?} else {
            /*
                FriendlyByteBuf packetbb = new FriendlyByteBuf(Unpooled.buffer());
                write(packetbb);
                player.player().connection.send(new ClientboundCustomPayloadPacket(identifier, packetbb));
            */
            //?}
        //?}
    }

    public void onPacketServer(CompatibleUtils.KyoyuPlayer player) {
        KyoyuPacketManager.handleC2S(this.content, player);
    }

    public void onPacketClient() {
        KyoyuPacketManager.handleS2C(this.content);
    }
}
