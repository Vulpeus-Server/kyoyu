package com.vulpeus.kyoyu.net;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.vulpeus.kyoyu.Kyoyu;
import net.minecraft.resources.ResourceLocation;

//? if >=1.20.6 {
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
//?} else {
/*
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
*/
//?}


//? if FABRIC {
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
    //? if >1.20.4 {
    import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
    //?} else {
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
*/
//?}
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

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


    private static final ResourceLocation identifier = CompatibleUtils.identifier(Kyoyu.MOD_ID, "kyoyu");

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

    //? FABRIC {
    public static void register() {
        //? if >1.20.4 {
        PayloadTypeRegistry.playC2S().register(TYPE, CODEC);
        PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
        //?}

        if (Kyoyu.isClient()) {
            ClientPlayNetworking.registerGlobalReceiver(
                    //? if >1.20.4 {
                    TYPE, KyoyuPacketPayload::onPacketClient
                    //?} else {
                    /*
                    identifier, (client, handler, buf, responseSender) -> {
                        KyoyuPacketPayload packetPayload = new KyoyuPacketPayload(buf.readByteArray());
                        packetPayload.onPacketClient(client, handler, responseSender);
                    }
                    */
                    //?}
            );
        } else {
            ServerPlayNetworking.registerGlobalReceiver(
                    //? if >1.20.4 {
                    TYPE, KyoyuPacketPayload::onPacketServer
                    //?} else {
                    /*
                    identifier, (server, player, handler, buf, responseSender) -> {
                        KyoyuPacketPayload packetPayload = new KyoyuPacketPayload(buf.readByteArray());
                        packetPayload.onPacketServer(server, player, handler, responseSender);
                    }
                    */
                    //?}
            );
        }
    }
    //?} elif NEOFORGE {
    /*
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                KyoyuPacketPayload.TYPE,
                KyoyuPacketPayload.CODEC,
                new DirectionalPayloadHandler<>(
                        KyoyuPacketPayload::onPacketServer,
                        KyoyuPacketPayload::onPacketClient
                )
        );
    }
    */
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
    public void sendS2C(ServerPlayer player) {
        //? FABRIC {
            //? >=1.20.6 {
            ServerPlayNetworking.send(player, this);
            //?} else {
            /*
            FriendlyByteBuf packetbb = new FriendlyByteBuf(Unpooled.buffer());
            packetbb.writeByteArray(this.content);
            ServerPlayNetworking.send(player, identifier, packetbb);
            */
        //?}
        //?} elif NEOFORGE {
        /* PacketDistributor.sendToServer(this); */
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
        //? if FABRIC && <=1.20.4 {
        /* Kyoyu.LOGGER.info("onPacketServer {} {} {} {}", server, player, handler, sender); */
        //?} else {
        Kyoyu.LOGGER.info("onPacketServer {}", context);
        //?}

        //? if FABRIC {
        KyoyuPacketManager.handleC2S(
                this.content,
                //? if FABRIC && <=1.20.4 {
                    /* player */
                //?} else {
                    context.player()
                //?}
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
        //? if FABRIC && <=1.20.4 {
        /* Kyoyu.LOGGER.info("onPacketClient {} {} {}", client, handler, sender); */
        //?} else {
        Kyoyu.LOGGER.info("onPacketClient {}", context);
        //?}

        KyoyuPacketManager.handleS2C(this.content);
    }
}
