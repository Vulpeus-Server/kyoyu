package com.vulpeus.kyoyu;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerList<T> {
    private final Map<UUID, Player<T>> playerMap;
    private final Map<String, UUID> mcidMap;

    public PlayerList() {
        this.playerMap = new HashMap<>();
        this.mcidMap = new HashMap<>();
    }

    public int size() {
        return this.playerMap.size();
    }

    public boolean isEmpty() {
        return this.playerMap.isEmpty();
    }

    public void clear() {
        this.playerMap.clear();
        this.mcidMap.clear();
    }

    // Get
    public T getServerPlayer(@NotNull String mcid) {
        UUID uuid = this.getUUID(mcid);
        if (uuid == null) return null;
        return this.getServerPlayer(uuid);
    }
    public T getServerPlayer(@NotNull UUID uuid) {
        Player<T> player = playerMap.get(uuid);
        if (player == null) return null;
        return player.serverPlayer();
    }
    public UUID getUUID(@NotNull String mcid) {
        return mcidMap.get(mcid.toLowerCase());
    }

    // Contains
    public boolean contains(@NotNull String mcid) {
        return this.mcidMap.containsKey(mcid.toLowerCase());
    }
    public boolean contains(@NotNull UUID uuid) {
        return this.playerMap.containsKey(uuid);
    }

    // Add
    public boolean add(@NotNull UUID uuid, @NotNull String mcid) {
        Player<T> player = new Player<>(uuid, mcid);
        return this.add(player);
    }
    public boolean add(@NotNull UUID uuid, @NotNull String mcid, @NotNull T serverPlayer) {
        Player<T> player = new Player<>(uuid, mcid, serverPlayer);
        return this.add(player);
    }
    private boolean add(@NotNull Player<T> player) {
        Player<T> i = this.playerMap.put(player.uuid, player);
        UUID j = this.mcidMap.put(player.mcid, player.uuid);
        return i == null && j == null;
    }

    // Remove
    public boolean remove(@NotNull String mcid) {
        return this.remove(this.getUUID(mcid));
    }
    public boolean remove(@NotNull UUID uuid) {
        return this.remove(this.playerMap.get(uuid));
    }
    private boolean remove(@NotNull Player<T> player) {
        Player<T> i = this.playerMap.remove(player.uuid);
        UUID j = this.mcidMap.remove(player.mcid);
        return i != null && j != null;
    }

    // Get All
    @NotNull
    public Set<UUID> getAll() {
        return playerMap.keySet();
    }

    private static class Player<T> {
        private final String mcid;
        private final UUID uuid;
        private T serverPlayer = null;

        public Player(@NotNull UUID uuid, @NotNull String mcid) {
            this.uuid = uuid;
            this.mcid = mcid.toLowerCase();
        }

        public Player(@NotNull UUID uuid, @NotNull String mcid, @NotNull T serverPlayer) {
            this.uuid = uuid;
            this.mcid = mcid.toLowerCase();
            this.serverPlayer = serverPlayer;
        }

        public void setServerPlayer(@NotNull T serverPlayer) {
            this.serverPlayer = serverPlayer;
        }
        public T serverPlayer() {
            return this.serverPlayer;
        }

        public UUID uuid() {
            return this.uuid;
        }

        public String mcid() {
            return this.mcid;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Player)) return false;
            return ((Player<?>) obj).uuid.equals(this.uuid);
        }
    }
}
