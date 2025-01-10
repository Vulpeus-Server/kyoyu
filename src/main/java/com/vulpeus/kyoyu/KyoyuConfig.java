package com.vulpeus.kyoyu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.ArrayList;
import java.util.List;

public class KyoyuConfig {

    private final String logLevel = "info";

    private final String modify = "blacklist";
    private final List<String> modify_whitelist = new ArrayList<>();
    private final List<String> modify_blacklist = new ArrayList<>();

    private final String remove = "blacklist";
    private final List<String> remove_whitelist = new ArrayList<>();
    private final List<String> remove_blacklist = new ArrayList<>();

    public static KyoyuConfig fromJson(String json) {
        Gson gson = new Gson();
        Kyoyu.LOGGER.info("{}", json);
        return gson.fromJson(json, KyoyuConfig.class);
    }

    public void setLogLevel() {
        String log_level = this.logLevel.toUpperCase();
        if (log_level.equals("OFF")) Configurator.setLevel(Kyoyu.MOD_ID, Level.OFF);
        if (log_level.equals("INFO")) Configurator.setLevel(Kyoyu.MOD_ID, Level.INFO);
        if (log_level.equals("DEBUG")) Configurator.setLevel(Kyoyu.MOD_ID, Level.DEBUG);
        if (log_level.equals("ERROR")) Configurator.setLevel(Kyoyu.MOD_ID, Level.ERROR);
        if (log_level.equals("WARN")) Configurator.setLevel(Kyoyu.MOD_ID, Level.WARN);
        if (log_level.equals("ALL")) Configurator.setLevel(Kyoyu.MOD_ID, Level.ALL);
    }

    private boolean containsCaseInsensitive(List<String> list, String value) {
        for (String otherValue: list) {
            if (value.equalsIgnoreCase(otherValue)) return true;
        }
        return false;
    }

    private boolean isAllowed(String name, String type, List<String> whilelist, List<String> blacklist) {
        if (type.equalsIgnoreCase("WHITELIST")) {
            return containsCaseInsensitive(whilelist, name);
        } else {
            return !containsCaseInsensitive(blacklist, name);
        }
    }

    public boolean isAllowedModify(String playerName) {
        return isAllowed(playerName, modify, modify_whitelist, modify_blacklist);
    }

    public boolean isAllowedRemove(String playerName) {
        return isAllowed(playerName, remove, remove_whitelist, remove_blacklist);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
