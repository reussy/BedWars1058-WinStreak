package com.reussy.development.api.user;

import java.util.UUID;

public interface IUser {

    /**
     * Get the UUID of the player.
     *
     * @return the UUID of the player.
     */
    UUID getUUID();

    /**
     * Get the current win streak of the player.
     *
     * @return the current win streak of the player.
     */
    int getStreak();

    /**
     * Set the current win streak of the player.
     *
     * @param streak the current win streak of the player.
     */
    void setStreak(int streak);

    /**
     * Get the best win streak of the player.
     *
     * @return the best win streak of the player.
     */
    int getBestStreak();

    /**
     * Set the best win streak of the player.
     *
     * @param bestStreak the best win streak of the player.
     */
    void setBestStreak(int bestStreak);
}
