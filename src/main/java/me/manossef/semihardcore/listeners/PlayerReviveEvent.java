package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is revived, either by a player sacrificing themselves or by the {@code /revive} command.
 */
public class PlayerReviveEvent extends Event {
    private final Player player;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerReviveEvent(Player player) {
        this.player = player;
    }

    /**
     * Returns the player that was revived.
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
        return "PlayerReviveEvent[player=" + this.player.getName() + "]";
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}