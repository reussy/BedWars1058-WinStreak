package com.reussy.exodus.bw1058winstreak.cache;

import java.util.UUID;

public class StreakProperties {

    private final UUID uuid;
    private int streak;
    private int bestStreak;

    public StreakProperties(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }
}
