package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.CompatibleUtils;
import com.google.gson.Gson;
import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.KyoyuConfig;
import com.vulpeus.kyoyu.client.KyoyuClient;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;

import java.nio.charset.StandardCharsets;

public class HandshakePacket extends IKyoyuPacket {

    private final String version;
    private final KyoyuConfig config;

    public HandshakePacket(String version, KyoyuConfig config) {
        this.version = version;
        this.config = config;
    }

    public HandshakePacket(byte[] bytes) {
        Gson gson = new Gson();
        HandshakePacket handshakePacket = gson.fromJson(new String(bytes, StandardCharsets.UTF_8), HandshakePacket.class);
        this.version = handshakePacket.version;
        this.config = handshakePacket.config;
    }

    @Override
    public byte[] encode() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(CompatibleUtils.KyoyuPlayer player) {
        Kyoyu.LOGGER.info("Login `{}` with compatible client version `{}`", this.version, player.getName());

        Kyoyu.PLAYERS.getServerPlayer(player.getUUID()).setCompatible(true);

        HandshakePacket handshakePacket = new HandshakePacket(Kyoyu.MOD_VERSION, Kyoyu.CONFIG);
        KyoyuPacketManager.sendS2C(handshakePacket, player);
    }

    //? if client {
    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Login to compatible server version `{}`", version);
        KyoyuClient.init(version, config);
    }
    //?}
}
