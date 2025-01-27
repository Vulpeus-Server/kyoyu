package com.vulpeus.kyoyu;

import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.UUID;

public class KyoyuLogger {
    private final String prefix;
    private Level chatLevel;
    private Level consoleLevel;

    private final Logger logger = LogManager.getLogger(Kyoyu.MOD_ID);

    public KyoyuLogger() {
        this.prefix = "Kyoyu";
        this.chatLevel = Level.INFO;
        this.consoleLevel = Level.INFO;
    }
    public void info(Object obj) {
        info("{}", obj);
    }
    public void info(String fmt, Object... objects) {
        log(Level.INFO, fmt, objects);
    }

    public void warn(Object obj) {
        warn("{}", obj);
    }
    public void warn(String fmt, Object... objects) {
        log(Level.WARN, fmt, objects);
    }

    public void debug(Object obj) {
        debug("{}", obj);
    }
    public void debug(String fmt, Object... objects) {
        log(Level.DEBUG, fmt, objects);
    }

    public void error(Object obj) {
        error("{}", obj);
    }
    public void error(String fmt, Object... objects) {
        log(Level.ERROR, fmt, objects);
    }

    private void log(Level level, String fmt, Object... objects) {
        String message = format(fmt, objects);
        if (shouldLog(level, this.chatLevel)) {
            for (UUID uuid: Kyoyu.PLAYERS.getAll()) {
                CompatibleUtils.KyoyuPlayer player = Kyoyu.PLAYERS.getServerPlayer(uuid);
                //? if PAPER {
                    /* player.player().sendMessage(message); */
                //?} else {
                    //? if >=1.19 {
                        player.player().sendSystemMessage(CompatibleUtils.text(message));
                    //?} elif >=1.16 {
                        /* player.player().sendMessage(CompatibleUtils.text(message), net.minecraft.Util.NIL_UUID); */
                    //?} else {
                        /* player.player().sendMessage(CompatibleUtils.text(message)); */
                    //?}
                //?}
            }
        }
        if (shouldLog(level, this.consoleLevel)) {
            logger.info("[{}] {} : {}", this.prefix, level.level, message);
        }
    }
    private boolean shouldLog(Level messageLevel, Level configuredLevel) {
        if (configuredLevel == Level.OFF) return false;
        if (configuredLevel == Level.ALL) return true;
        return messageLevel.ordinal() >= configuredLevel.ordinal();
    }

    private String format(String formatter, Object[] objects) {
        return ParameterizedMessage.format(formatter, objects);
    }

    public void setChatLevel(Level level) {
        this.chatLevel = level;
    }
    public void setConsoleLevel(Level level) {
        this.consoleLevel = level;
    }

    public enum Level {
        ALL("all"),
        DEBUG("debug"),
        INFO("info"),
        WARN("warn"),
        ERROR("error"),
        OFF("off");

        private final String level;

        Level(String str) {
            this.level = str.toUpperCase();
        }

        public static Level fromString(String str) {
            if (str.equalsIgnoreCase("all")) return Level.ALL;
            if (str.equalsIgnoreCase("debug")) return Level.DEBUG;
            if (str.equalsIgnoreCase("info")) return Level.INFO;
            if (str.equalsIgnoreCase("warn")) return Level.WARN;
            if (str.equalsIgnoreCase("error")) return Level.ERROR;
            return Level.OFF;
        }
    }
}
