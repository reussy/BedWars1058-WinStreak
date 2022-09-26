package com.reussy.development.api.event;

import com.reussy.development.api.user.IUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player's best win streak is incremented.
 */
public class PlayerIncrementStreak extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final IUser user;
    private final int oldStreak;
    private final int newStreak;
    private boolean cancelled;

    public PlayerIncrementStreak(IUser user, int oldStreak, int newStreak) {
        this.user = user;
        this.oldStreak = oldStreak;
        this.newStreak = newStreak;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Get the user involved in the event.
     *
     * @return the user involved in the event.
     */
    public IUser getUser() {
        return user;
    }

    /**
     * Get the old win streak of the player.
     *
     * @return the old win streak of the player.
     */
    public int getOldStreak() {
        return oldStreak;
    }

    /**
     * Get the new win streak of the player.
     *
     * @return the new win streak of the player.
     */
    public int getNewStreak() {
        return newStreak;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
