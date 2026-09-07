package me.manossef.semihardcore;

import me.manossef.semihardcore.listeners.PlayerLifeChangeEvent;
import me.manossef.semihardcore.listeners.PlayerReviveEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the class responsible for every change of any player's life count or death status, as well as for sending
 * messages not specific to the plugin's commands.
 *
 * <p>
 * Every action related to players gaining or losing lives is done by this class. A lot of other utility methods meant
 * to be used by add-ons are also contained in this class.
 */
public class LifeChanges {

    private final SemiHardcore plugin;
    private final Set<UUID> sacrificedPlayers;

    protected LifeChanges(SemiHardcore instance) {

        plugin = instance;
        sacrificedPlayers = new HashSet<>();

    }

    /**
     * Returns a set containing the UUIDs of all players currently marked as being sacrificed. Note that this almost
     * always returns an empty set, since the set is only used to temporarily store a player being sacrificed so the
     * plugin can show them the correct message.
     *
     * @return The set of players being sacrificed.
     */
    public Set<UUID> getSacrificedPlayers() {

        return this.sacrificedPlayers;

    }

    /**
     * Adds a player's UUID to the set of UUIDs of players currently being sacrificed.
     *
     * @param uuid The player's UUID.
     */
    public void addSacrificedPlayer(UUID uuid) {

        this.sacrificedPlayers.add(uuid);

    }

    /**
     * Removes a player's UUID from the set of UUIDs of players currently being sacrificed, if it's currently contained
     * in the set.
     *
     * @param uuid The player's UUID.
     */
    public void removeSacrificedPlayer(UUID uuid) {

        this.sacrificedPlayers.remove(uuid);

    }

    /**
     * Returns the number of lives the specified player currently has.
     *
     * @param player The player in question.
     * @return The player's life count.
     */
    public int getLives(Player player) {

        return plugin.getLifeData().getLives(player.getUniqueId());

    }

    /**
     * Returns whether the specified player has a life count. Note that this will almost always return true, since a
     * player only lacks a life count when they join the server for the first time, when the plugin's listeners
     * immediately give a new life count to the player.
     *
     * @param player The player in question.
     * @return {@code true} if the player has a life count, {@code false} otherwise.
     */
    public boolean hasLives(Player player) {

        return plugin.getLifeData().hasLives(player.getUniqueId());

    }

    /**
     * Returns a map of all online players to their life counts. This can be used to save all players' life counts at a
     * certain point in time. The map will not contain any player for whom {@link #hasLives(Player)} returns false.
     *
     * @return A map of all players to their life counts.
     */
    public Map<Player, Integer> getOnlinePlayerLifeMap() {

        Map<Player, Integer> lives = new HashMap<>();
        Bukkit.getOnlinePlayers().forEach(player -> {

            if(hasLives(player))
                lives.put(player, getLives(player));

        });
        return lives;

    }

    /**
     * Returns whether the specified player has zero lives.
     *
     * @param player The player in question.
     * @return {@code true} if the player is dead, {@code false} otherwise.
     */
    public boolean isDead(Player player) {

        if(!hasLives(player))
            return false;
        return getLives(player) == 0;

    }

    /**
     * Returns a set of all players that currently have zero lives.
     *
     * @return A set of all dead players.
     */
    public Set<Player> getDeadOnlinePlayers() {

        Set<Player> deadPlayers = new HashSet<>();
        Bukkit.getOnlinePlayers().forEach(player -> {

            if(isDead(player))
                deadPlayers.add(player);

        });
        return deadPlayers;

    }

    /**
     * Sets the specified player's life count to the specified number. Note that this will obey the plugin's life limit,
     * so it is not possible to set a player's life count to a number above the limit. If the life count after this
     * method is run is different, a {@link PlayerLifeChangeEvent} is called.
     *
     * @param player The player in question.
     * @param lives  The new life count.
     * @throws IllegalArgumentException If {@code lives} is negative.
     */
    public void setLives(Player player, int lives) {

        if(lives < 0)
            throw new IllegalArgumentException("Life count cannot be negative");
        int currentCount = plugin.getLifeData().hasLives(player.getUniqueId()) ? plugin.getLifeData().getLives(player.getUniqueId()) : 0;
        int newCount = plugin.getLifeData().setLives(player.getUniqueId(), lives);
        if(currentCount != newCount) Bukkit.getPluginManager().callEvent(new PlayerLifeChangeEvent(player, newCount));

    }

    /**
     * Sets the specified player's life count to the specified number, obeying the plugin's life limit. This is a
     * convenience method that runs {@link #setLives(Player, int)} and then {@link #respectLivesLimit(Player)}.
     *
     * @param player The player in question.
     * @param lives  The new life count.
     * @throws IllegalArgumentException If {@code lives} is negative.
     * @deprecated It is no longer possible to breach the life limit using {@link #setLives(Player, int)}, therefore
     * this method has no use.
     */
    @Deprecated(since = "2.7.0")
    public void setLivesUpToLimit(Player player, int lives) {

        this.setLives(player, lives);
        this.respectLivesLimit(player);

    }

    /**
     * Sets the specified player's life count to zero and kills them.
     *
     * @param player The player in question.
     */
    public void setLivesZeroAndKill(Player player) {

        plugin.getLifeData().setLives(player.getUniqueId(), 0);
        player.setHealth(0);

    }

    /**
     * Adds or removes the specified player from the dead players list, based on the value of the {@code dead}
     * argument.
     *
     * @param player The player in question.
     * @param dead   Whether the player will be set to dead or alive.
     * @deprecated The dead players list has been removed, therefore there wouldn't be a point in using this method
     * anymore. Using {@link #setLives} with a life count of 0 is enough.
     */
    @Deprecated(since = "2.7.0")
    public void setDead(Player player, boolean dead) {

    }

    /**
     * Sets the specified player's life count to their current life count plus one life. Note that this will obey the
     * plugin's life limit, so it is not possible to set a player's life count to a number above the limit. If the life
     * count after this method is run is different, a {@link PlayerLifeChangeEvent} is called.
     *
     * @param player The player in question.
     */
    public void addLife(Player player) {

        this.setLives(player, this.getLives(player) + 1);

    }

    /**
     * Sets the specified player's life count to their current life count plus one life, obeying the plugin's life
     * limit. This is a convenience method that runs {@link #addLife(Player)} and then
     * {@link #respectLivesLimit(Player)}.
     *
     * @param player The player in question.
     * @deprecated It is no longer possible to breach the life limit using {@link #addLife(Player)}, therefore this
     * method has no use.
     */
    @Deprecated(since = "2.7.0")
    public void addLifeUpToLimit(Player player) {

        this.addLife(player);
        this.respectLivesLimit(player);

    }

    /**
     * Returns a new random number of lives between the plugin's minimum starting lives (inclusive) and the maximum
     * starting lives (inclusive).
     *
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
     *
     * @param player The player in question.
     */
    public void revivePlayer(Player player) {

        if(this.getLives(player) > 0)
            return;
        this.setLives(player, 1);
        Bukkit.getPluginManager().callEvent(new PlayerReviveEvent(player));
        Location loc = ((player.getBedSpawnLocation() == null)
            ? this.getMainWorld().getHighestBlockAt(this.getMainWorld().getSpawnLocation()).getLocation().add(0.5, 1, 0.5)
            : player.getBedSpawnLocation());
        player.teleport(loc);
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        sendTitle(player, color(plugin.getMessages().reviveTitle));
        player.sendMessage(color(plugin.getMessages().revive.replace("%lives%", Integer.toString(this.getLives(player)))));

    }

    /**
     * Returns the maximum number of lives a player can have at any time.
     *
     * @return The plugin's upper limit of lives.
     */
    public int getLivesLimit() {

        return plugin.getOptions().livesLimit();

    }

    /**
     * Checks if the specified player's life count is above the life limit, and reduces it down to the life limit if it
     * is.
     *
     * @param player The player in question.
     * @return {@code true} if the player's life count was changed, {@code false} otherwise.
     * @deprecated It is no longer possible for a player to ever have a life count above the limit, therefore this
     * method has no use.
     */
    @Deprecated(since = "2.7.0")
    public boolean respectLivesLimit(Player player) {

        if(this.getLivesLimit() <= 0)
            return false;
        if(this.getLives(player) > this.getLivesLimit()) {

            this.setLives(player, this.getLivesLimit());
            return true;

        }
        return false;

    }

    /**
     * Sends the message shown by the {@code /lives} command to the specified player.
     *
     * @param player The player in question.
     */
    public void livesMessage(Player player) {

        if(this.getLives(player) == 1) {

            player.sendMessage(color(plugin.getMessages().livesSingular.replace("%lives%", Integer.toString(this.getLives(player)))));

        } else if(this.getLives(player) > 1) {

            player.sendMessage(color(plugin.getMessages().livesPlural.replace("%lives%", Integer.toString(this.getLives(player)))));

        } else {

            player.sendMessage(color(plugin.getMessages().livesNone.replace("%lives%", Integer.toString(this.getLives(player)))));

        }

    }

    /**
     * Sends the message shown to the receiver of the {@code /givelife} command to the specified player.
     *
     * @param player The player to be sent the message.
     * @param donor  The player to be shown as the donor.
     */
    public void livesMessageAfterGenerosity(Player player, Player donor) {

        sendTitle(player, color(plugin.getMessages().livesTitleAfterDonation));
        if(this.getLives(player) == 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterDonationSingular.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%donor%", donor.getName())));

        } else if(this.getLives(player) > 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterDonationPlural.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%donor%", donor.getName())));

        } else {

            player.sendMessage(color(plugin.getMessages().livesAfterDonationNone.replace("%lives%", Integer.toString(this.getLives(player))).replace("%donor%",
                donor.getName())));

        }

    }

    /**
     * Sends the message shown to the donor of the {@code /givelife} command to the specified player.
     *
     * @param player   The player to be sent the message.
     * @param receiver The player to be shown as the receiver.
     */
    public void livesMessageAfterBeingGenerous(Player player, Player receiver) {

        if(this.getLives(player) == 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterBeingGenerousSingular.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%receiver%", receiver.getName())));

        } else if(this.getLives(player) > 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterBeingGenerousPlural.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%receiver%", receiver.getName())));

        } else {

            player.sendMessage(color(plugin.getMessages().livesAfterBeingGenerousNone.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%receiver%", receiver.getName())));

        }

    }

    /**
     * Sends the message shown to a player when they die to the specified player.
     *
     * @param player The player in question.
     */
    public void livesMessageAfterDeath(Player player) {

        if(this.getLives(player) == 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterDeathSingular.replace("%lives%", Integer.toString(this.getLives(player)))));

        } else if(this.getLives(player) > 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterDeathPlural.replace("%lives%", Integer.toString(this.getLives(player)))));

        } else {

            player.sendMessage(color(plugin.getMessages().livesAfterDeathNone.replace("%lives%", Integer.toString(this.getLives(player)))));
            sendTitle(player, color(plugin.getMessages().livesAfterDeathNoneTitle.replace("%lives%", Integer.toString(this.getLives(player)))));

        }

    }

    /**
     * Sends the message shown to a player when they steal a life to the specified player.
     *
     * @param player The player to be sent the message.
     * @param victim The player to be shown as the victim.
     */
    public void livesMessageAfterLifesteal(Player player, Player victim) {

        if(this.getLives(player) == 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterLifestealSingular.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%victim%", victim.getName())));

        } else if(this.getLives(player) > 1) {

            player.sendMessage(color(plugin.getMessages().livesAfterLifestealPlural.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%victim%", victim.getName())));

        } else {

            player.sendMessage(color(plugin.getMessages().livesAfterLifestealNone.replace("%lives%", Integer.toString(this.getLives(player)))
                .replace("%victim%", victim.getName())));

        }

    }

    /**
     * Sends the welcome message to the specified player.
     *
     * @param player The player in question.
     */
    public void welcomeMessage(Player player) {

        if(plugin.getOptions().startingLivesMin() == plugin.getOptions().startingLivesMax()) {

            player.sendMessage(color(plugin.getMessages().welcomeConstant.replace("%lives%", Integer.toString(plugin.getOptions().startingLivesMin()))));

        } else {

            player.sendMessage(color(plugin.getMessages().welcomeRange.replace("%min%", Integer.toString(plugin.getOptions().startingLivesMin()))
                .replace("%max%", Integer.toString(plugin.getOptions().startingLivesMax()))
                .replace("%lives%", Integer.toString(this.getLives(player)))));

        }

    }

    private World getMainWorld() {

        Properties pr = new Properties();
        String worldName = null;
        try {

            FileInputStream in = new FileInputStream("server.properties");
            pr.load(in);
            worldName = pr.getProperty("level-name");
            in.close();

        } catch(IOException e) {

            plugin.getLogger().severe(e.getMessage());

        }
        if(worldName == null)
            return Bukkit.getWorld("world");
        return Bukkit.getWorld(worldName);

    }

    private void sendTitle(Player player, String title) {

        player.sendTitle(title, "", 10, 70, 20);

    }

    private String color(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

}
