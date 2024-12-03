package com.vulpeus.kyoyu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Kyoyu {
    public static final String MOD_ID = "kyoyu";
    public static final String MOD_VERSION = /*$ mod_version*/ "unknown";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static boolean isClient = false;
    private static Optional<KyoyuClient> kyoyuClient = Optional.empty();

    public static void setEnv(boolean isClient) {
        Kyoyu.isClient = isClient;
    }

    public static boolean isClient() {
        return isClient;
    }
    public static boolean isServer() {
        return !isClient;
    }

    public static Optional<KyoyuClient> getClient() {
        return kyoyuClient;
    }

    public static void initClient(String serverVersion) {
        if (kyoyuClient.isPresent()) {
            LOGGER.error("Duplicate client init");
            return;
        }
        LOGGER.info("Kyoyu client init");
        kyoyuClient = Optional.of(new KyoyuClient(serverVersion));
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
}
