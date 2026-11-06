package me.manossef.semihardcore.listeners;

import me.manossef.semihardcore.LifeChanges;
import me.manossef.semihardcore.SemiHardcore;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * SemiHardcore's listener class. This class should be of no interest to add-ons.
 */
public final class Listeners implements Listener {
    private final SemiHardcore plugin;
    private final Map<UUID, Integer> legacyLives;

    public Listeners(SemiHardcore plugin, Map<UUID, Integer> legacyLives) {
        this.plugin = plugin;
        this.legacyLives = legacyLives;
    }

    /**
     * Listens to players joining the server.
     * @param event The event being listened to.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(!lifeChanges.hasLives(player)) {
            if(this.legacyLives != null && this.legacyLives.containsKey(player.getUniqueId())) {
                Integer lives = this.legacyLives.get(player.getUniqueId());
                player.setGameMode(lives > 0 ? GameMode.SURVIVAL : GameMode.SPECTATOR);
                lifeChanges.setLives(player, lives);
                lifeChanges.livesMessage(player);
                this.legacyLives.remove(player.getUniqueId());
                if(this.legacyLives.isEmpty()) {
                    plugin.getLogger().info("All legacy life data has been converted. Deleting the lives.yml file.");
                    if(!new File(plugin.getDataFolder().toPath() + "/lives.yml").delete())
                        plugin.getLogger().warning("Failed to delete the lives.yml file.");
                }
            } else {
                player.setGameMode(GameMode.SURVIVAL);
                lifeChanges.setLives(player, lifeChanges.getLivesForNewPlayer());
                lifeChanges.welcomeMessage(player);
            }
        } else {
            int lives = lifeChanges.getLives(player);
            player.setGameMode(lives > 0 ? GameMode.SURVIVAL : GameMode.SPECTATOR);
            lifeChanges.setLives(player, lives);
            lifeChanges.livesMessage(player);
        }
    }

    /**
     * Listens to players dying.
     * @param event The event being listened to.
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        LifeChanges lifeChanges = plugin.getLifeChanges();
        int previousLifeCount = lifeChanges.getLives(player);
        lifeChanges.setLives(player, Math.max(previousLifeCount - 1, 0));
        if(lifeChanges.isDead(player)) {
            NamespacedKey sacrificed = new NamespacedKey(plugin, SemiHardcore.SACRIFICED_STATUS_KEY);
            if(!player.getPersistentDataContainer().has(sacrificed, PersistentDataType.BYTE))
                lifeChanges.livesMessageAfterDeath(player);
            else
                player.getPersistentDataContainer().remove(sacrificed);
            if(plugin.getOptions().lightningStrikeOnLastDeath())
                player.getWorld().strikeLightningEffect(player.getLocation());
        } else
            lifeChanges.livesMessageAfterDeath(player);
        Player killer = player.getKiller();
        if(killer == null)
            return;
        if(lifeChanges.isDead(killer))
            return;
        if(previousLifeCount >= plugin.getOptions().minLivesToBeLiferobbed() && lifeChanges.getLives(killer) <= plugin.getOptions().maxLivesToLifesteal() && plugin.getOptions().lifestealEnabled()) {
            lifeChanges.addLife(killer);
            lifeChanges.livesMessageAfterLifesteal(killer, player);
        }
    }

    /**
     * Listens to players respawning after death.
     * @param event The event being listened to.
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        LifeChanges lifeChanges = plugin.getLifeChanges();
        new BukkitRunnable() {

            public void run() {
                if(lifeChanges.getLives(player) > 0)
                    player.setGameMode(GameMode.SURVIVAL);
                else
                    player.setGameMode(GameMode.SPECTATOR);
            }

        }.runTaskLater(plugin, 2);
    }

    /**
     * Listens to worlds being loaded.
     * @param event The event being listened to.
     */
    @EventHandler
    public void onNewWorld(WorldLoadEvent event) {
        event.getWorld().setHardcore(plugin.getOptions().hardcoreHearts());
        if(plugin.getOptions().forceHardDifficulty())
            event.getWorld().setDifficulty(Difficulty.HARD);
    }
}