package com.vulpeus.kyoyu.net;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.packets.HandshakePacket;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class KyoyuPacketManager {
    private static final Map<String, Class<? extends IKyoyuPacket>> packetRegistry = new HashMap<>();

    static {
        packetRegistry.put("handshake", HandshakePacket.class);
    }

    private static IKyoyuPacket decode(byte[] raw) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(raw); DataInputStream dis = new DataInputStream(bais)) {
            String key = dis.readUTF();
            int len = dis.readInt();
            byte[] data = new byte[len];
            dis.readFully(data);
            Kyoyu.LOGGER.info("KyoyuPacketManager decode {}", key);
            Class<? extends IKyoyuPacket> packetClass = packetRegistry.get(key);
            if (packetClass != null) {
                try {
                    IKyoyuPacket packet = packetClass.getDeclaredConstructor(byte[].class).newInstance((Object) data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Kyoyu.LOGGER.info("Unknow Packet {}", data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private static byte[] encode(IKyoyuPacket packet) {
        byte[] res = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            String key = null;
            for (Map.Entry<String, Class<? extends IKyoyuPacket>> entry: packetRegistry.entrySet()) {
                if (entry.getValue() == packet.getClass()) {
                    key = entry.getKey();
                    break;
                }
            }
            if (key == null) return null;
            byte[] data = packet.encode();
            dos.writeUTF(key);
            dos.writeInt(data.length);
            dos.write(data);
            res = baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public static void handleC2S(byte[] data, ServerPlayer player) {
        IKyoyuPacket packet = decode(data);
        if (packet==null) return;
        packet.onServer(player);
    }
    public static void handleS2C(byte[] data) {
        IKyoyuPacket packet = decode(data);
        if (packet==null) return;
        packet.onClient();
    }

    public static void sendC2S(IKyoyuPacket packet) {
        KyoyuPacketPayload packetPayload = new KyoyuPacketPayload(encode(packet));
        packetPayload.sendC2S();
    }
    public static void sendS2C(IKyoyuPacket packet, ServerPlayer player) {
        KyoyuPacketPayload packetPayload = new KyoyuPacketPayload(encode(packet));
        packetPayload.sendS2C(player);
    }
}
