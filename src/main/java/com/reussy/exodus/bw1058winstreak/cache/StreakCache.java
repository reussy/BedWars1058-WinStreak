package com.reussy.exodus.bw1058winstreak.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StreakCache {

    private final Map<UUID, StreakProperties> propertiesMap;

    public StreakCache() {
        this.propertiesMap = new HashMap<>();
    }

    public void put(UUID uuid, StreakProperties streakProperties) {

        propertiesMap.put(uuid, streakProperties);
    }

    public void remove(UUID uuid) {

        propertiesMap.remove(uuid);
    }

    public StreakProperties get(UUID uuid) {

        StreakProperties streakProperties = propertiesMap.get(uuid);

        if (streakProperties == null) {
            throw new IllegalStateException("[BW1058-WinStreak DEBUG]: The streak cache for " + uuid.toString() + " is null!");
        }

        return streakProperties;
    }

    public StreakProperties getMap(UUID uuid) {
        return propertiesMap.get(uuid);
    }

    public Map<UUID, StreakProperties> getPlayerProfilePropertiesMap() {
        return this.propertiesMap;
    }
}
