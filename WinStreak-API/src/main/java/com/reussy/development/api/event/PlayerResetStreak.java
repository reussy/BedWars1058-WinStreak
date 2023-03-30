package com.reussy.development.api.event;

import com.reussy.development.api.user.IUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player's streak is reset to 0.
 * This event is called when the player loses a game.
 */
public class PlayerResetStreak extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final IUser user;
    private final int streak;
    private boolean cancelled;

    public PlayerResetStreak(IUser user, int streak) {
        this.user = user;
        this.streak = streak;
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
     * Get the streak before it was reset.
     *
     * @return the streak before it was reset.
     */
    public int getStreak() {
        return streak;
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

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
