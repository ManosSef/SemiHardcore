package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is revived, either by a player sacrificing themselves or by the {@code /revive} command.
 */
public class PlayerReviveEvent extends Event {
    private final Player player;
    private final int lives;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerReviveEvent(Player player, int lives) {
        this.player = player;
        this.lives = lives;
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
}