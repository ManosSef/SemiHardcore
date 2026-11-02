package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.SemiHardcore;
import me.manossef.semihardcore.data.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the implementation of the plugin's {@code /revive} command.
 */
public class ReviveCommand implements CommandExecutor {
    private final SemiHardcore plugin;

    public ReviveCommand(SemiHardcore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandUnspecifiedPlayer()));
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandNotFound()));
            return true;
        }
        if(plugin.getLifeChanges().getLives(player) != 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().reviveAlivePlayer()));
            return true;
        }
        plugin.getLifeChanges().revivePlayer(player);
        sender.sendMessage(Messages.color(plugin.getMessages().reviveSuccess()).replace("%player%", args[0]));
        return true;
    }
}