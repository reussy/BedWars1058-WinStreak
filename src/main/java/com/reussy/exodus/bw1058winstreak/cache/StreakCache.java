package com.reussy.exodus.bw1058winstreak.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StreakCache {

    private final Map<UUID, StreakProperties> cache;

    public StreakCache() {
        this.cache = new HashMap<>();
    }

    public void load(UUID uuid, StreakProperties streakProperties) {

        if (uuid == null || streakProperties == null) return;

        cache.put(uuid, streakProperties);
    }

    public void destroy(UUID uuid) {

        if (uuid == null) return;

        cache.remove(uuid);
    }

    public StreakProperties get(UUID uuid) {

        StreakProperties streakProperties = cache.get(uuid);

        if (streakProperties == null) {
            throw new IllegalStateException("[BW1058-WinStreak DEBUG]: The streak cache for " + uuid.toString() + " is null!");
        }

        return streakProperties;
    }

    public boolean isInCache(UUID uuid){

        if (uuid == null) return false;

        return getCacheMap().containsKey(uuid);
    }

    public StreakProperties getMap(UUID uuid) {
        return cache.get(uuid);
    }

    public Map<UUID, StreakProperties> getCacheMap() {
        return this.cache;
    }
}
