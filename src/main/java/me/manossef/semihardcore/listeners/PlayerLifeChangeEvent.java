package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when the life count of a player is changed in any way.
 */
public class PlayerLifeChangeEvent extends Event {

    private final Player player;
    private final int newLives;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerLifeChangeEvent(Player player, int newLives) {

        this.player = player;
        this.newLives = newLives;

    }

    /**
     * Returns the player whose life count changed.
     *
     * @return The player in question.
     */
    public Player getPlayer() {

        return this.player;

    }

    /**
     * Returns the player's life count after the life change.
     *
     * @return The new life count.
     */
    public int getNewLives() {

        return this.newLives;

    }

    /**
     * Returns a string representation of the event.
     *
     * @return A string representation of the event.
     */
    public String toString() {

        return "PlayerLifeChangeEvent[player=" + this.player.getName() + ", newLives=" + this.newLives + "]";

    }

    public HandlerList getHandlers() {

        return HANDLERS;

    }

    public static HandlerList getHandlerList() {

        return HANDLERS;

    }

}
