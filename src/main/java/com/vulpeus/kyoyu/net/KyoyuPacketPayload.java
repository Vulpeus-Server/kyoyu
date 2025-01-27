package com.vulpeus.kyoyu.net;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

//? if >=1.20.6 {
    import net.minecraft.network.codec.ByteBufCodecs;
    import net.minecraft.network.codec.StreamCodec;
    import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
    import net.minecraft.network.RegistryFriendlyByteBuf;
//?} else {
/*
    import net.minecraft.network.FriendlyByteBuf;
    import net.minecraft.server.network.ServerGamePacketListenerImpl;
*/
//?}


//? if FABRIC {
    import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
    import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
    //? if <=1.20.4 {
    /*
        import io.netty.buffer.Unpooled;
        import net.fabricmc.fabric.api.networking.v1.PacketSender;
        import net.minecraft.client.multiplayer.ClientPacketListener;
        import net.minecraft.client.Minecraft;
        import net.minecraft.server.MinecraftServer;
    */
    //?}
//?} elif NEOFORGE {
/*
    import net.neoforged.neoforge.network.PacketDistributor;
    import net.neoforged.neoforge.network.handling.IPayloadContext;
*/
//?}

public class KyoyuPacketPayload
        //? if >1.20.4
        implements CustomPacketPayload
{

    public final byte[] content;
    public KyoyuPacketPayload(byte[] content) {
        this.content = content;
    }
    public byte[] content() {
        return this.content;
    }


    public static final ResourceLocation identifier = CompatibleUtils.identifier(Kyoyu.MOD_ID, "kyoyu");

    //? if >1.20.4 {
        private static final Type<KyoyuPacketPayload> TYPE = new Type<>(identifier);
        private static final StreamCodec<RegistryFriendlyByteBuf, KyoyuPacketPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.BYTE_ARRAY, KyoyuPacketPayload::content, KyoyuPacketPayload::new
        );
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    //?}

    public void sendC2S() {
        //? FABRIC {
            //? >=1.20.6 {
                ClientPlayNetworking.send(this);
            //?} else {
            /*
                FriendlyByteBuf packetbb = new FriendlyByteBuf(Unpooled.buffer());
                packetbb.writeByteArray(this.content);
                ClientPlayNetworking.send(identifier, packetbb);
            */
            //?}
        //?} elif NEOFORGE {
            /* PacketDistributor.sendToServer(this); */
        //?}
    }
    public void sendS2C(CompatibleUtils.KyoyuPlayer player) {
        //? FABRIC {
            //? >=1.20.6 {
                ServerPlayNetworking.send(player.player(), this);
            //?} else {
            /*
                FriendlyByteBuf packetbb = new FriendlyByteBuf(Unpooled.buffer());
                packetbb.writeByteArray(this.content);
                ServerPlayNetworking.send(player.player(), identifier, packetbb);
            */
            //?}
        //?} elif NEOFORGE {
            /* PacketDistributor.sendToPlayer(player.player(), this); */
        //?}
    }

    public void onPacketServer(
            //? FABRIC {
                //? if >1.20.4 {
                    ServerPlayNetworking.Context context
                //?} else {
                    /* MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, PacketSender sender */
                //?}
            //?} elif NEOFORGE {
                /* IPayloadContext context */
            //?} else {
                /* Object context */
            //?}
    ) {
        //? if FABRIC || NEOFORGE {
        KyoyuPacketManager.handleC2S(
                this.content,
                new CompatibleUtils.KyoyuPlayer(
                    //? if FABRIC && <=1.20.4 {
                        /* player */
                    //?} else {
                        (ServerPlayer) context.player()
                    //?}
                )
        );
        //?}
    }

    public void onPacketClient(
            //? FABRIC {
                //? if >1.20.4 {
                    ClientPlayNetworking.Context context
                //?} else {
                    /* Minecraft client, ClientPacketListener handler, PacketSender sender */
                //?}
            //?} elif NEOFORGE {
                /* IPayloadContext context */
            //?} else {
                /* Object context */
            //?}
    ) {
        KyoyuPacketManager.handleS2C(this.content);
    }
}
