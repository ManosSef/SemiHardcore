package me.manossef.semihardcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player gives a life away using the {@code /givelife} command, even if that life is the player's last
 * life.
 */
public class PlayerLifeGiveawayEvent extends Event {
    private final Player player;
    private final Player receiver;
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerLifeGiveawayEvent(Player player, Player receiver) {
        this.player = player;
        this.receiver = receiver;
    }

    /**
     * Returns the player that gave away the life.
     * @return The player in question.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the player that received the life.
     * @return The player that received the life.
     */
    public Player getReceiver() {
        return this.receiver;
    }

    /**
     * Returns a string representation of the event.
     * @return A string representation of the event.
     */
    public String toString() {
        return "PlayerLifeGiveawayEvent[player=" + this.player.getName() + ", receiver=" + this.receiver.getName()
            + "]";
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}