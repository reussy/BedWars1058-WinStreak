package com.reussy.exodus.bw1058winstreak.cache;

import java.util.UUID;

public class StreakProperties {

    private final UUID UUID;
    private int CURRENT_STREAK;
    private int BEST_STREAK;

    public StreakProperties(UUID uuid) {
        this.UUID = uuid;
    }

    public UUID getUuid() {
        return UUID;
    }

    public int getCurrentStreak() {
        return CURRENT_STREAK;
    }

    public void setCurrentStreak(int currentStreak) {
        this.CURRENT_STREAK = currentStreak;
    }

    public int getBestStreak() {
        return BEST_STREAK;
    }

    public void setBestStreak(int bestStreak) {
        this.BEST_STREAK = bestStreak;
    }
}
