package com.reussy.exodus.bw1058winstreak.cache;

import java.util.UUID;

public class StreakProperties {

    private final UUID UUID;
    private int STREAK;
    private int BEST_STREAK;

    public StreakProperties(UUID uuid) {
        this.UUID = uuid;
    }

    public UUID getUUID() {
        return UUID;
    }

    public int getStreak() {
        return STREAK;
    }

    public void setStreak(int streak) {
        this.STREAK = streak;
    }

    public int getBestStreak() {
        return BEST_STREAK;
    }

    public void setBestStreak(int bestStreak) {
        this.BEST_STREAK = bestStreak;
    }
}
