package com.reussy.exodus.bw1058winstreak.database;

import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;

import java.util.UUID;

public interface DatabaseManager {

    void initializeTable();

    boolean hasStreakProfile(UUID uuid);

    StreakProperties initializeStreakProperties(UUID uuid);

    void saveStreakProperties(StreakProperties streakProperties);
}
