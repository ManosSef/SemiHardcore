package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.SemiHardcore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the implementation of the plugin's {@code /lives} command.
 */
public class LivesCommand implements CommandExecutor {
    private final SemiHardcore plugin;

    public LivesCommand(SemiHardcore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "The console does not have lives!");
            return true;
        }
        plugin.getLifeChanges().livesMessage(player);
        return true;
    }
}