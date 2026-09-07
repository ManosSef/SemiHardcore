package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player gives away their last life, either to an alive player or to revive a dead player.
 */
public class PlayerSacrificeEvent extends Event {

    private final Player player;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerSacrificeEvent(Player player) {

        this.player = player;

    }

    /**
     * Returns the player that gave away their last life
     *
     * @return The player in question.
     */
    public Player getPlayer() {

        return this.player;

    }

    /**
     * Returns a string representation of the event.
     *
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

}
