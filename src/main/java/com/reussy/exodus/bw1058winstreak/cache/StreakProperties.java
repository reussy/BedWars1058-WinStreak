package com.reussy.exodus.bw1058winstreak.cache;

import java.util.UUID;

public class StreakProperties {

    private final UUID uuid;
    private int currentStreak;
    private int bestStreak;

    public StreakProperties(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }
}
