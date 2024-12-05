package com.vulpeus.kyoyu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
}
