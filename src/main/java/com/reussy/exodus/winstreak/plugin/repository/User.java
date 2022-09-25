package com.reussy.exodus.winstreak.plugin.repository;

import com.reussy.exodus.winstreak.api.user.IUser;

import java.util.UUID;

public class User implements IUser {

    private final UUID uuid;
    private int streak;
    private int bestStreak;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the UUID of the player.
     *
     * @return the UUID of the player.
     */
    @Override
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Get the current win streak of the player.
     *
     * @return the current win streak of the player.
     */
    @Override
    public int getStreak() {
        return streak;
    }

    /**
     * Set the current win streak of the player.
     *
     * @param streak the current win streak of the player.
     */
    @Override
    public void setStreak(int streak) {
        this.streak = streak;
    }

    /**
     * Get the best win streak of the player.
     *
     * @return the best win streak of the player.
     */
    @Override
    public int getBestStreak() {
        return bestStreak;
    }

    /**
     * Set the best win streak of the player.
     *
     * @param bestStreak the best win streak of the player.
     */
    @Override
    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }
}
