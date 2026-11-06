package me.manossef.semihardcore.data;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.manossef.semihardcore.LifeChanges;
import me.manossef.semihardcore.SemiHardcore;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * The {@link PlaceholderExpansion} for this plugin. It handles placeholders in text when PlaceholderAPI is installed.
 * This class should be of no interest to add-ons.
 */
public final class Placeholders extends PlaceholderExpansion {
    private final SemiHardcore plugin;

    public Placeholders(SemiHardcore plugin) {
        this.plugin = plugin;
    }

    public String getAuthor() {
        return "ManosSef";
    }

    public String getIdentifier() {
        return "semihardcore";
    }

    public String getVersion() {
        return "2.6.0"; // only bump this to the release target when the onRequest() method is edited
    }

    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        LifeChanges lc = plugin.getLifeChanges();
        Options options = plugin.getOptions();
        if(params.equalsIgnoreCase("lives")) {
            if(!(player instanceof Player online))
                return null;
            if(!lc.hasLives(online))
                return null;
            return String.valueOf(lc.getLives(online));
        }
        if(params.equalsIgnoreCase("lives_limit"))
            return String.valueOf(lc.getLivesLimit());
        if(params.equalsIgnoreCase("starting_lives_min"))
            return String.valueOf(options.startingLivesMin());
        if(params.equalsIgnoreCase("starting_lives_max"))
            return String.valueOf(options.startingLivesMax());
        return null;
    }
}