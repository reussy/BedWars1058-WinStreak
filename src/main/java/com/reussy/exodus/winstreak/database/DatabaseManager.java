package com.reussy.exodus.winstreak.database;

import com.reussy.exodus.winstreak.cache.StreakProperties;

import java.util.UUID;

public interface DatabaseManager {

    void initializeTable();

    boolean hasStreakProfile(UUID uuid);

    StreakProperties initializeStreakProperties(UUID uuid);

    void saveStreakProperties(StreakProperties streakProperties);
}
