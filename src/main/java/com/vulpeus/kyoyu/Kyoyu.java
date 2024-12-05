package com.vulpeus.kyoyu;

import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Kyoyu {
    public static final String MOD_ID = "kyoyu";
    public static final String MOD_VERSION = /*$ mod_version*/ "unknown";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static boolean isClient = false;
    private static KyoyuClient kyoyuClient = null;

    public static void setEnv(boolean isClient) {
        Kyoyu.isClient = isClient;
    }

    public static boolean isClient() {
        return isClient;
    }
    public static boolean isServer() {
        return !isClient;
    }

    public static KyoyuClient getClient() {
        return kyoyuClient;
    }

    public static void initClient(String serverVersion) {
        if (kyoyuClient != null) {
            LOGGER.error("Duplicate client init");
            return;
        }
        LOGGER.info("Kyoyu client init");
        kyoyuClient = new KyoyuClient(serverVersion);
    }

    public static void deinitClient() {
        LOGGER.info("Kyoyu client deinit");
        kyoyuClient = null;
    }

    public static class KyoyuClient {

        private final String serverVersion;

        public KyoyuClient(String serverVersion) {
            this.serverVersion = serverVersion;
        }
        private String serverVersion() {
            return serverVersion;
        }
    }

    private static final Path configsDir = Paths.get("kyoyu");

    private static KyoyuPlacement readPlacement(Path configPath) {
        try {
            String json = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
            return KyoyuPlacement.fromJson(json);
        } catch (IOException e) {
            LOGGER.error("Kyoyu.readPlacement: read KyoyuPlacement from {}", configPath);
            LOGGER.error(e);
        }
        return null;
    }

    public static KyoyuPlacement findPlacement(UUID uuid) {

        if (uuid == null) return null;

        Path configPath = configsDir.resolve(uuid.toString() + ".json");
        if (!configPath.toFile().exists()) return null;

        return readPlacement(configPath);
    }

    public static List<KyoyuPlacement> getAllPlacement() {
        File[] files = configsDir.toFile().listFiles();
        if (files != null) {
            return Arrays.stream(files).map(file -> readPlacement(file.toPath())).collect(Collectors.toList());
        }
        return null;
    }

    public static void savePlacement(KyoyuPlacement kyoyuPlacement) {
        Path configPath = configsDir.resolve(kyoyuPlacement.getUuid().toString() + ".json");
        try {
            Files.write(configPath, Collections.singletonList(kyoyuPlacement.toJson()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Kyoyu.savePlacement: save KyoyuPlacement from {}", kyoyuPlacement.getUuid());
            LOGGER.error(e);
        }
    }
}
