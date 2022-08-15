package com.reussy.exodus.winstreak.cache;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StreakCache {

    private final Map<UUID, StreakProperties> cache;

    public StreakCache() {
        this.cache = new ConcurrentHashMap<>();
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

        return cache.get(uuid);
    }

    public boolean isInCache(UUID uuid) {

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
