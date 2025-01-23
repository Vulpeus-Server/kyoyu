package com.vulpeus.kyoyu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class KyoyuConfig {

    private final String logLevel_chat;
    private final String logLevel_console;

    private final String modify;
    private final List<String> modify_whitelist;
    private final List<String> modify_blacklist;

    private final String remove;
    private final List<String> remove_whitelist;
    private final List<String> remove_blacklist;

    public KyoyuConfig() {
        this.logLevel_chat = "info";
        this.logLevel_console = "info";

        this.modify = "blacklist";
        this.modify_whitelist = new ArrayList<>();
        this.modify_blacklist = new ArrayList<>();

        this.remove = "blacklist";
        this.remove_whitelist = new ArrayList<>();
        this.remove_blacklist = new ArrayList<>();
    }

    public KyoyuLogger.Level chatLogLevel() {
        return KyoyuLogger.Level.fromString(logLevel_chat);
    }
    public KyoyuLogger.Level consoleLogLevel() {
        return KyoyuLogger.Level.fromString(logLevel_console);
    }

    public static KyoyuConfig fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, KyoyuConfig.class);
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
