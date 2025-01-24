package com.vulpeus.kyoyu;

import com.vulpeus.kyoyu.placement.KyoyuPlacement;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Kyoyu {
    public static final String MOD_ID = "kyoyu";
    public static final String MOD_VERSION = /*$ mod_version*/ "unknown";

    public static final KyoyuLogger LOGGER = new KyoyuLogger();

    public static KyoyuConfig CONFIG = null;

    public static List<ServerPlayer> PLAYERS = new ArrayList<>();

    private static boolean isClient = false;

    public static void setEnv(boolean isClient) {
        Kyoyu.isClient = isClient;
    }

    public static boolean isClient() {
        return isClient;
    }
    public static boolean isServer() {
        return !isClient;
    }

    public static void loadConfig() {
        File kyoyuDir;
        if (isClient()) {
            kyoyuDir = Paths.get("schematics/kyoyu").toFile();
        } else {
            kyoyuDir = Paths.get("kyoyu/files").toFile();
        }
        if (kyoyuDir.mkdirs()) {
            LOGGER.info("File not found. generated: {}", kyoyuDir);
        }

        File configDir = Paths.get("config").toFile();
        if (configDir.mkdir()) {
            LOGGER.info("File not found. generated: {}", configDir);
        }

        try {
            Path configFile = Paths.get("config/kyoyu.json");
            if (!configFile.toFile().exists()) {
                InputStream istream = Kyoyu.class.getResourceAsStream("/assets/kyoyu/kyoyu.json");
                if (istream == null) return;
                FileUtils.copyInputStreamToFile(istream, configFile.toFile());
                LOGGER.info("File not found. generated: {}", configFile);
            }
            String json = new String(Files.readAllBytes(configFile), StandardCharsets.UTF_8);
            CONFIG = KyoyuConfig.fromJson(json);
            LOGGER.setChatLevel(CONFIG.chatLogLevel());
            LOGGER.setConsoleLevel(CONFIG.consoleLogLevel());
        } catch (IOException e) {
            LOGGER.error("cannot read config");
            LOGGER.error(e);
        }
    }

    public static Path getConfigDir() {
        return Paths.get("kyoyu");
    }

    public static Path getSaveSchemeDir() {
        if (isClient()) return Paths.get("schematics/kyoyu");
        return Paths.get("kyoyu/files");
    }

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

        Path configPath = getConfigDir().resolve(uuid.toString() + ".json");
        if (!configPath.toFile().exists()) return null;

        return readPlacement(configPath);
    }

    public static List<KyoyuPlacement> getAllPlacement() {
        File[] files = getConfigDir().toFile().listFiles();
        if (files != null) {
            return Arrays.stream(files)
                    .filter(file -> file.getName().endsWith(".json"))
                    .map(file -> readPlacement(file.toPath())).collect(Collectors.toList());
        }
        return null;
    }

    public static void savePlacement(KyoyuPlacement kyoyuPlacement) {
        Path configPath = getConfigDir().resolve(kyoyuPlacement.getUuid().toString() + ".json");
        try {
            Files.write(configPath, Collections.singletonList(kyoyuPlacement.toJson()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Kyoyu.savePlacement: save KyoyuPlacement from {}", kyoyuPlacement.getUuid());
            LOGGER.error(e);
        }
    }

    public static void unlinkPlacement(KyoyuPlacement kyoyuPlacement) {
        Path configPath = getConfigDir().resolve(kyoyuPlacement.getUuid().toString() + ".json");
        try {
            Files.delete(configPath);
        } catch (IOException e) {
            LOGGER.error("Kyoyu.unlinkPlacement: unlink KyoyuPlacement {}", kyoyuPlacement.getUuid());
            LOGGER.error(e);
        }
    }
}
