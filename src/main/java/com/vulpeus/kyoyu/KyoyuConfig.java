package com.vulpeus.kyoyu;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class KyoyuConfig {

    private String log_level;

    public static KyoyuConfig fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, KyoyuConfig.class);
    }

    public void setLogLevel() {
        log_level = log_level.toUpperCase();
        if (log_level.equals("OFF")) Configurator.setLevel(Kyoyu.MOD_ID, Level.OFF);
        if (log_level.equals("INFO")) Configurator.setLevel(Kyoyu.MOD_ID, Level.INFO);
        if (log_level.equals("DEBUG")) Configurator.setLevel(Kyoyu.MOD_ID, Level.DEBUG);
        if (log_level.equals("ERROR")) Configurator.setLevel(Kyoyu.MOD_ID, Level.ERROR);
        if (log_level.equals("WARN")) Configurator.setLevel(Kyoyu.MOD_ID, Level.WARN);
        if (log_level.equals("ALL")) Configurator.setLevel(Kyoyu.MOD_ID, Level.ALL);
    }
}
