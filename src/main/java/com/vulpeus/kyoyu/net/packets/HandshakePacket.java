package com.vulpeus.kyoyu.net.packets;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.IKyoyuPacket;
import net.minecraft.world.entity.player.Player;

import java.nio.charset.StandardCharsets;

public class HandshakePacket extends IKyoyuPacket {

    private final String version;

    public HandshakePacket(String version) {
        super(version.getBytes(StandardCharsets.UTF_8));
        this.version = version;
    }

    public HandshakePacket(byte[] bytes) {
        super(bytes);
        this.version = new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] encode() {
        return version.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void onServer(Player player) {
        Kyoyu.LOGGER.info("Login `{}` with compatible client version `{}`", version, player.getName().getString());

        // TODO
        //  packet = new HandshakePacket(Kyoyu.MOD_VERSION);
        //  packet.send(player);
    }

    @Override
    public void onClient() {
        Kyoyu.LOGGER.info("Login to compatible server version `{}`", version);
        // TODO
        //  initClient(version);
    }
}
