package com.vulpeus.kyoyu;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.ArrayList;
import java.util.List;

public class KyoyuConfig {

    private final String log_level = "info";

    private final String modify = "blacklist";
    private final List<String> modifyWhitelist = new ArrayList<>();
    private final List<String> modifyBlacklist = new ArrayList<>();

    public static KyoyuConfig fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, KyoyuConfig.class);
    }

    public void setLogLevel() {
        String log_level = this.log_level.toUpperCase();
        if (log_level.equals("OFF")) Configurator.setLevel(Kyoyu.MOD_ID, Level.OFF);
        if (log_level.equals("INFO")) Configurator.setLevel(Kyoyu.MOD_ID, Level.INFO);
        if (log_level.equals("DEBUG")) Configurator.setLevel(Kyoyu.MOD_ID, Level.DEBUG);
        if (log_level.equals("ERROR")) Configurator.setLevel(Kyoyu.MOD_ID, Level.ERROR);
        if (log_level.equals("WARN")) Configurator.setLevel(Kyoyu.MOD_ID, Level.WARN);
        if (log_level.equals("ALL")) Configurator.setLevel(Kyoyu.MOD_ID, Level.ALL);
    }

    public boolean isAllowedModify(String playerName) {
        playerName = playerName.toUpperCase();
        String modify = this.modify.toUpperCase();
        if (modify.equals("WHITELIST")) {
            for (String otherPlayerName: modifyWhitelist) {
                if (otherPlayerName.toUpperCase().equals(playerName)) return true;
            }
            return false;
        } else {
            for (String otherPlayerName: modifyBlacklist) {
                if (otherPlayerName.toUpperCase().equals(playerName)) return false;
            }
            return true;
        }
    }
}
