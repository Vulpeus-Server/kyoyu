package com.vulpeus.kyoyu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kyoyu {
    public static final String MOD_ID = "kyoyu";
    public static final String MOD_VERSION = /*$ mod_version*/ "unknown";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

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
}
