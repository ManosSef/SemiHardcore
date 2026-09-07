package me.manossef.semihardcore.listeners;

import me.manossef.semihardcore.LifeChanges;
import me.manossef.semihardcore.SemiHardcore;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * SemiHardcore's listener class. This class should be of no interest to add-ons.
 */
public class Listeners implements Listener {

    private final SemiHardcore plugin;

    public Listeners(SemiHardcore instance) {

        plugin = instance;

    }

    /**
     * Listens to players joining the server.
     *
     * @param event The event being listened to.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(!lifeChanges.hasLives(player)) {

            player.setGameMode(GameMode.SURVIVAL);
            lifeChanges.setLives(player, lifeChanges.getLivesForNewPlayer());
            lifeChanges.welcomeMessage(player);

        } else if(lifeChanges.getLives(player) > 0) {

            player.setGameMode(GameMode.SURVIVAL);
            lifeChanges.livesMessage(player);

        } else if(lifeChanges.isDead(player)) {

            player.setGameMode(GameMode.SPECTATOR);
            lifeChanges.livesMessage(player);

        } else {

            player.setGameMode(GameMode.SURVIVAL);
            lifeChanges.setLives(player, lifeChanges.getLivesForNewPlayer());
            lifeChanges.welcomeMessage(player);

        }

    }

    /**
     * Listens to players dying.
     *
     * @param event The event being listened to.
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        LifeChanges lifeChanges = plugin.getLifeChanges();
        int previousLifeCount = lifeChanges.getLives(player);
        lifeChanges.setLives(player, Math.max(previousLifeCount - 1, 0));
        if(lifeChanges.isDead(player)) {

            if(!lifeChanges.getSacrificedPlayers().contains(player.getUniqueId())) {

                lifeChanges.livesMessageAfterDeath(player);

            } else {

                lifeChanges.removeSacrificedPlayer(player.getUniqueId());

            }
            if(plugin.getOptions().lightningStrikeOnLastDeath()) {

                player.getWorld().strikeLightningEffect(player.getLocation());

            }

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
     *
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
     *
     * @param event The event being listened to.
     */
    @EventHandler
    public void onNewWorld(WorldLoadEvent event) {

        event.getWorld().setHardcore(plugin.getOptions().hardcoreHearts());
        if(plugin.getOptions().forceHardDifficulty())
            event.getWorld().setDifficulty(Difficulty.HARD);

    }

    /**
     * Listens to plugins being enabled.
     *
     * @param event The event being listened to.
     */
    @EventHandler
    public void onAddonLoad(PluginEnableEvent event) {

        plugin.checkAddonForUpdates(event.getPlugin().getName());

    }

}
