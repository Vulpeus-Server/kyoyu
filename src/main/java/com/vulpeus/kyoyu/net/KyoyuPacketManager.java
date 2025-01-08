package com.vulpeus.kyoyu.net;

import com.vulpeus.kyoyu.Kyoyu;
import com.vulpeus.kyoyu.net.packets.*;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KyoyuPacketManager {
    private static final List<Packet> packetRegistry = new ArrayList<>();

    private static class Packet {
        private final String key;
        private final Class<? extends IKyoyuPacket> clazz;

        Packet(String key, Class<? extends IKyoyuPacket> clazz) {
            this.key = key;
            this.clazz = clazz;
        }
    }

    static {
        packetRegistry.add(new Packet("handshake", HandshakePacket.class));
        packetRegistry.add(new Packet("load_explorer", LoadExplorerPacket.class));
        packetRegistry.add(new Packet("placement_meta", PlacementMetaPacket.class));
        packetRegistry.add(new Packet("file_request", FileRequestPacket.class));
        packetRegistry.add(new Packet("file_response", FileResponsePacket.class));
        packetRegistry.add(new Packet("remove_placement", RemovePlacementPacket.class));
    }

    private static IKyoyuPacket decode(byte[] raw) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(raw); DataInputStream dis = new DataInputStream(bais)) {
            String key = dis.readUTF();
            int len = dis.readInt();
            byte[] data = new byte[len]; dis.readFully(data);

            for (Packet packet: packetRegistry) if (key.equals(packet.key)) {
                try {
                    return packet.clazz.getConstructor(byte[].class).newInstance((Object) data);
                } catch (Exception e) {
                    Kyoyu.LOGGER.error("Failed to decode packet {}", key);
                    Kyoyu.LOGGER.error(e);
                    return null;
                }
            }
            Kyoyu.LOGGER.error("Unknow Packet {}", key);
        } catch (IOException e) {
            Kyoyu.LOGGER.error("Shiranai Error");
            Kyoyu.LOGGER.error(e);
        }

        return null;
    }

    private static byte[] encode(IKyoyuPacket kyoyuPacket) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
            String key = null;
            for (Packet packet: packetRegistry) if (kyoyuPacket.getClass() == packet.clazz) {
                key = packet.key; break;
            }
            if (key == null) return null;

            byte[] data = kyoyuPacket.encode();
            dos.writeUTF(key);
            dos.writeInt(data.length);
            dos.write(data);
            return baos.toByteArray();
        } catch (IOException e) {
            Kyoyu.LOGGER.error(e);
            return null;
        }
    }

    public static void handleC2S(byte[] data, ServerPlayer player) {
        IKyoyuPacket packet = decode(data);
        if (packet==null) {
            Kyoyu.LOGGER.warn("packet[C2S] Not Found Packet from {}. Please check kyoyu version!", player.getName().getString());
            return;
        }
        Kyoyu.LOGGER.debug("packet[C2S] packet {} from {}", packet, player.getName().getString());
        packet.onServer(player);
    }
    public static void handleS2C(byte[] data) {
        IKyoyuPacket packet = decode(data);
        if (packet==null) {
            Kyoyu.LOGGER.warn("packet[S2C] Not Found Packet from server. Please check kyoyu version!");
            return;
        }
        Kyoyu.LOGGER.debug("packet[S2C] packet {} from server", packet);
        packet.onClient();
    }

    public static void sendC2S(IKyoyuPacket packet) {
        Kyoyu.LOGGER.debug("packet[C2S] packet {} to server", packet);
        KyoyuPacketPayload packetPayload = new KyoyuPacketPayload(encode(packet));
        packetPayload.sendC2S();
    }
    public static void sendS2C(IKyoyuPacket packet, ServerPlayer player) {
        Kyoyu.LOGGER.debug("packet[S2C] packet {} to {}", packet, player.getName().getString());
        KyoyuPacketPayload packetPayload = new KyoyuPacketPayload(encode(packet));
        packetPayload.sendS2C(player);
    }
}
