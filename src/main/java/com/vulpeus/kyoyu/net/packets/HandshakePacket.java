package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import com.vulpeus.kyoyu.net.KyoyuPacketManager;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;

public class HandshakePacket extends IKyoyuPacket {

    private final String version;

    public HandshakePacket(String version) {
        super();
        this.version = version;
    }

    public HandshakePacket(byte[] bytes) {
        super();
        this.version = new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] encode() {
        return version.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(ServerPlayer player) {
        Kyoyu.LOGGER.info("Login `{}` with compatible client version `{}`", version, player.getName().getString());

        HandshakePacket handshakePacket = new HandshakePacket(Kyoyu.MOD_VERSION);
        KyoyuPacketManager.sendS2C(handshakePacket, player);
    }

    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Login to compatible server version `{}`", version);
        Kyoyu.initClient(version);
    }
}
