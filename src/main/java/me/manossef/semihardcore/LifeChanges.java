package me.manossef.semihardcore;

import me.manossef.api.alexandria.Worlds;
import me.manossef.semihardcore.data.Messages;
import me.manossef.semihardcore.listeners.PlayerLifeChangeEvent;
import me.manossef.semihardcore.listeners.PlayerReviveEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the class responsible for every change of any player's life count or death status, as well as for sending
 * messages not specific to the plugin's commands.
 * <p>
 * Every action related to players gaining or losing lives is done by this class. A lot of other utility methods meant
 * to be used by add-ons are also contained in this class.
 */
public final class LifeChanges {
    private final SemiHardcore plugin;

    LifeChanges(SemiHardcore plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the number of lives the specified player currently has.
     * @param player The player in question.
     * @return The player's life count.
     * @throws NoSuchElementException If the specified player has no life count yet.
     */
    public int getLives(Player player) {
        Integer lives = player.getPersistentDataContainer().get(new NamespacedKey(plugin, SemiHardcore.LIFE_COUNT_KEY), PersistentDataType.INTEGER);
        if(lives == null) throw new NoSuchElementException("Player " + player.getName() + " has no lives yet");
        return lives;
    }

    /**
     * Returns whether the specified player has a life count. Note that this will almost always return true, since a
     * player only lacks a life count when they join the server for the first time, when the plugin's listeners
     * immediately give a new life count to the player.
     * @param player The player in question.
     * @return {@code true} if the player has a life count, {@code false} otherwise.
     */
    public boolean hasLives(Player player) {
        return player.getPersistentDataContainer().has(new NamespacedKey(plugin, SemiHardcore.LIFE_COUNT_KEY), PersistentDataType.INTEGER);
    }

    /**
     * Returns a map of all online players to their life counts. This can be used to save all players' life counts at a
     * certain point in time. The map will not contain any player for whom {@link #hasLives(Player)} returns false.
     * @return A map of all players to their life counts.
     */
    public Map<Player, Integer> getOnlinePlayerLifeMap() {
        Map<Player, Integer> lives = new HashMap<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(this.hasLives(player))
                lives.put(player, getLives(player));
        });
        return lives;
    }

    /**
     * Returns whether the specified player has zero lives.
     * @param player The player in question.
     * @return {@code true} if the player is dead, {@code false} otherwise.
     */
    public boolean isDead(Player player) {
        if(!this.hasLives(player))
            return false;
        return this.getLives(player) == 0;
    }

    /**
     * Returns a set of all players that currently have zero lives.
     * @return A set of all dead players.
     */
    public Set<Player> getDeadOnlinePlayers() {
        Set<Player> deadPlayers = new HashSet<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(this.isDead(player))
                deadPlayers.add(player);
        });
        return deadPlayers;
    }

    /**
     * Sets the specified player's life count to the specified number. Note that this will obey the plugin's life limit,
     * so it is not possible to set a player's life count to a number above the limit. If the life count after this
     * method is run is different, a {@link PlayerLifeChangeEvent} is called.
     * @param player The player in question.
     * @param lives The new life count.
     * @throws IllegalArgumentException If {@code lives} is negative.
     */
    public void setLives(Player player, int lives) {
        if(lives < 0)
            throw new IllegalArgumentException("Life count cannot be negative");
        int currentCount = this.hasLives(player) ? this.getLives(player) : 0;
        int limit = plugin.getOptions().livesLimit();
        int newCount = limit > 0 ? Math.min(lives, limit) : lives;
        player.getPersistentDataContainer().set(new NamespacedKey(plugin, SemiHardcore.LIFE_COUNT_KEY), PersistentDataType.INTEGER, newCount);
        if(currentCount != newCount) Bukkit.getPluginManager().callEvent(new PlayerLifeChangeEvent(player, newCount));
    }

    /**
     * Sets the specified player's life count to zero and kills them.
     * @param player The player in question.
     */
    public void setLivesZeroAndKill(Player player) {
        this.setLives(player, 0);
        player.setHealth(0);
    }

    /**
     * Sets the specified player's life count to their current life count plus one life. Note that this will obey the
     * plugin's life limit, so it is not possible to set a player's life count to a number above the limit. If the life
     * count after this method is run is different, a {@link PlayerLifeChangeEvent} is called.
     * @param player The player in question.
     */
    public void addLife(Player player) {
        this.setLives(player, this.getLives(player) + 1);
    }

    /**
     * Returns a new random number of lives between the plugin's minimum starting lives (inclusive) and the maximum
     * starting lives (inclusive).
     * @return A new random life count.
     */
    public int getLivesForNewPlayer() {
        return ThreadLocalRandom.current().nextInt((plugin.getOptions().startingLivesMax() - plugin.getOptions().startingLivesMin()) + 1)
            + plugin.getOptions().startingLivesMin();
    }

    /**
     * Revives the specified player, if they are dead. Reviving includes the following steps:
     * <ul>
     * <li>The player's life count is set to 1.</li>
     * <li>The player is teleported to their last spawn point, or the world spawn
     * point if there was none.</li>
     * <li>The player's health is set to their maximum health.</li>
     * <li>The player's hunger and saturation values are set to 20.</li>
     * <li>The player's game mode is set to survival.</li>
     * </ul>
     * If the player is not dead, this method does nothing.
     * @param player The player in question.
     */
    public void revivePlayer(Player player) {
        if(this.getLives(player) > 0)
            return;
        this.setLives(player, 1);
        Bukkit.getPluginManager().callEvent(new PlayerReviveEvent(player));
        Location loc = ((player.getBedSpawnLocation() == null)
            ? Worlds.getMainWorld().getHighestBlockAt(Worlds.getMainWorld().getSpawnLocation()).getLocation().add(0.5, 1, 0.5)
            : player.getBedSpawnLocation());
        player.teleport(loc);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        this.sendTitle(player, Messages.color(plugin.getMessages().reviveTitle()));
        player.sendMessage(Messages.color(plugin.getMessages().revive().replace("%lives%", Integer.toString(this.getLives(player)))));
    }

    /**
     * Returns the maximum number of lives a player can have at any time.
     * @return The plugin's upper limit of lives.
     */
    public int getLivesLimit() {
        return plugin.getOptions().livesLimit();
    }

    /**
     * Sends the message shown by the {@code /lives} command to the specified player.
     * @param player The player in question.
     */
    public void livesMessage(Player player) {
        if(this.getLives(player) == 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesSingular().replace("%lives%", Integer.toString(this.getLives(player)))));
        else if(this.getLives(player) > 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesPlural().replace("%lives%", Integer.toString(this.getLives(player)))));
        else
            player.sendMessage(Messages.color(plugin.getMessages().livesNone().replace("%lives%", Integer.toString(this.getLives(player)))));
    }

    /**
     * Sends the message shown to the receiver of the {@code /givelife} command to the specified player.
     * @param player The player to be sent the message.
     * @param donor The player to be shown as the donor.
     */
    public void livesMessageAfterGenerosity(Player player, Player donor) {
        this.sendTitle(player, Messages.color(plugin.getMessages().livesTitleAfterDonation()));
        if(this.getLives(player) == 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterDonationSingular().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%donor%", donor.getName())));
        else if(this.getLives(player) > 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterDonationPlural().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%donor%", donor.getName())));
         else
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterDonationNone().replace("%lives%", Integer.toString(this.getLives(player))).replace("%donor%",
                donor.getName())));
    }

    /**
     * Sends the message shown to the donor of the {@code /givelife} command to the specified player.
     * @param player The player to be sent the message.
     * @param receiver The player to be shown as the receiver.
     */
    public void livesMessageAfterBeingGenerous(Player player, Player receiver) {
        if(this.getLives(player) == 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterBeingGenerousSingular().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%receiver%", receiver.getName())));
        else if(this.getLives(player) > 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterBeingGenerousPlural().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%receiver%", receiver.getName())));
        else
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterBeingGenerousNone().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%receiver%", receiver.getName())));
    }

    /**
     * Sends the message shown to a player when they die to the specified player.
     * @param player The player in question.
     */
    public void livesMessageAfterDeath(Player player) {
        if(this.getLives(player) == 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterDeathSingular().replace("%lives%", Integer.toString(this.getLives(player)))));
        else if(this.getLives(player) > 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterDeathPlural().replace("%lives%", Integer.toString(this.getLives(player)))));
        else {
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterDeathNone().replace("%lives%", Integer.toString(this.getLives(player)))));
            this.sendTitle(player, Messages.color(plugin.getMessages().livesAfterDeathNoneTitle().replace("%lives%", Integer.toString(this.getLives(player)))));
        }
    }

    /**
     * Sends the message shown to a player when they steal a life to the specified player.
     *
     * @param player The player to be sent the message.
     * @param victim The player to be shown as the victim.
     */
    public void livesMessageAfterLifesteal(Player player, Player victim) {
        if(this.getLives(player) == 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterLifestealSingular().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%victim%", victim.getName())));
        else if(this.getLives(player) > 1)
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterLifestealPlural().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%victim%", victim.getName())));
        else
            player.sendMessage(Messages.color(plugin.getMessages().livesAfterLifestealNone().replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%victim%", victim.getName())));
    }

    /**
     * Sends the welcome message to the specified player.
     * @param player The player in question.
     */
    public void welcomeMessage(Player player) {
        if(plugin.getOptions().startingLivesMin() == plugin.getOptions().startingLivesMax())
            player.sendMessage(Messages.color(plugin.getMessages().welcomeConstant().replace("%lives%", Integer.toString(plugin.getOptions().startingLivesMin()))));
        else
            player.sendMessage(Messages.color(plugin.getMessages().welcomeRange().replace("%min%", Integer.toString(plugin.getOptions().startingLivesMin()))
                .replace("%max%", Integer.toString(plugin.getOptions().startingLivesMax()))
                .replace("%lives%", Integer.toString(this.getLives(player)))));
    }

    private void sendTitle(Player player, String title) {
        player.sendTitle(title, "", 10, 70, 20);
    }
}