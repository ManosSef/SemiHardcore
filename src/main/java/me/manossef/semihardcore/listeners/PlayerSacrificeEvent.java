package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player gives away their last life, either to an alive player or to revive a dead player. Canceling the
 * event prevents both life changes involved (the sacrifice and the receiver gaining a life), and the receiver's entire
 * revival sequence if they were dead, from happening.
 */
public class PlayerSacrificeEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean cancelled;

    public PlayerSacrificeEvent(Player player) {
        this.player = player;
        this.cancelled = false;
    }

    /**
     * Returns the player that gave away their last life
     * @return The player in question.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns a string representation of the event.
     * @return A string representation of the event.
     */
    public String toString() {
        return "PlayerSacrificeEvent[player=" + this.player.getName() + "]";
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