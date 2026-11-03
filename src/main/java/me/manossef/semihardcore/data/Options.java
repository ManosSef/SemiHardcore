package me.manossef.semihardcore.data;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores the plugin's configuration options. The plugin creates an instance of this record when it's enabled, and uses
 * it throughout the running of the server. Creating any new instances of this record has no effect on the plugin.
 * @param startingLivesMin The minimum number of lives a player can start with.
 * @param startingLivesMax The maximum number of lives a player can start with.
 * @param hardcoreHearts Whether the server will show players' hearts above the hotbar as hardcore hearts.
 * @param forceHardDifficulty Whether the server enforces hard difficulty on all worlds.
 * @param livesLimit The maximum number of lives a player can have at any time.
 * @param playersCanBeGenerous Whether players are allowed to give away lives using the {@code /givelife} command.
 * @param lightningStrikeOnLastDeath Whether the plugin creates a fake lightning effect at a player's position on their
 *                                   final death.
 * @param sacrificesForRevival Whether players are allowed to sacrifice themselves (give away their last life) to revive
 *                             a dead player.
 * @param playersCanGiveAwayLastLife Whether players are allowed to give away their last life to an alive player.
 * @param lifestealEnabled Whether players can steal lives by killing other players.
 * @param maxLivesToLifesteal The highest life count that still allows players to steal lives by killing other players.
 * @param minLivesToBeLiferobbed The lowest life count that still leaves players vulnerable to being robbed of lives by
 *                               being killed by other players.
 */
public record Options(int startingLivesMin, int startingLivesMax, boolean hardcoreHearts, boolean forceHardDifficulty,
                      int livesLimit, boolean playersCanBeGenerous, boolean lightningStrikeOnLastDeath,
                      boolean sacrificesForRevival,
                      boolean playersCanGiveAwayLastLife, boolean lifestealEnabled, int maxLivesToLifesteal,
                      int minLivesToBeLiferobbed) {
    /**
     * Logs the current state of the plugin's options to the specified logger in a human-readable way.
     * @param logger The logger to log the options to.
     */
    public void logOptions(Logger logger) {
        logger.log(Level.INFO, "SemiHardcore settings:");
        logger.log(Level.INFO, "Min lives: " + this.startingLivesMin + ", max lives: " + this.startingLivesMax);
        logger.log(Level.INFO, "Hardcore hearts: " + this.hardcoreHearts);
        logger.log(Level.INFO, "Forces hard difficulty: " + this.forceHardDifficulty);
        logger.log(Level.INFO, "Upper limit of lives: " + this.livesLimit);
        logger.log(Level.INFO, "Players can give lives away: " + this.playersCanBeGenerous);
        logger.log(Level.INFO, "Players can sacrifice themselves to revive dead players: " + this.sacrificesForRevival);
        logger.log(Level.INFO, "Players can give their last life away: " + this.playersCanGiveAwayLastLife);
        logger.log(Level.INFO, "Lightning strikes players on last death: " + this.lightningStrikeOnLastDeath);
        logger.log(Level.INFO, "Life stealing: " + this.lifestealEnabled);
        logger.log(Level.INFO, "Maximum lives to be able to steal lives: " + this.maxLivesToLifesteal);
        logger.log(Level.INFO, "Minimum lives to be able to be robbed of lives: " + this.minLivesToBeLiferobbed);
    }

    /**
     * Saves the options to the config.yml file.
     * @param file The config.yml file configuration.
     */
    public void saveToFile(FileConfiguration file) {
        file.set("starting-lives.min", this.startingLivesMin);
        file.set("starting-lives.max", this.startingLivesMax);
        file.set("hardcore-hearts", this.hardcoreHearts);
        file.set("force-hard-difficulty", this.forceHardDifficulty);
        file.set("lives-limit", this.livesLimit);
        file.set("players-can-be-generous", this.playersCanBeGenerous);
        file.set("lightning-strike-on-last-death", this.lightningStrikeOnLastDeath);
        file.set("sacrifices-for-revival", this.sacrificesForRevival);
        file.set("players-can-give-away-last-life", this.playersCanGiveAwayLastLife);
        file.set("lifesteal.enabled", this.lifestealEnabled);
        file.set("lifesteal.max-lives-to-steal", this.maxLivesToLifesteal);
        file.set("lifesteal.min-lives-to-be-robbed", this.minLivesToBeLiferobbed);
    }
}