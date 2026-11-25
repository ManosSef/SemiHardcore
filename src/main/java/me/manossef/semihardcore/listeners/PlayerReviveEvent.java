package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is revived, either by a player sacrificing themselves or by the {@code /revive} command.
 * Canceling the event prevents the entire revival sequence from happening, including the life change, but not the
 * sacrifice if there is one.
 */
public class PlayerReviveEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final int lives;
    private boolean cancelled;

    public PlayerReviveEvent(Player player, int lives) {
        this.player = player;
        this.lives = lives;
        this.cancelled = false;
    }

    /**
     * Returns the player that was revived.
     * @return The player in question.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the number of lives the player received.
     * @return The life count.
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Returns a string representation of the event.
     * @return A string representation of the event.
     */
    public String toString() {
        return "PlayerReviveEvent[player=" + this.player.getName() + ", lives=" + this.lives + "]";
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}