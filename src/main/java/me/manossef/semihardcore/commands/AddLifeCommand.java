package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.SemiHardcore;
import me.manossef.semihardcore.data.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the implementation of the plugin's {@code /addlife} command.
 */
public final class AddLifeCommand implements CommandExecutor {
    private final SemiHardcore plugin;

    public AddLifeCommand(SemiHardcore plugin) {
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
        if(plugin.getLifeChanges().getLives(player) == 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandCannotRevive()));
            return true;
        }
        int currentCount = plugin.getLifeChanges().getLives(player);
        plugin.getLifeChanges().addLife(player);
        int newCount = plugin.getLifeChanges().getLives(player);
        plugin.getLifeChanges().livesMessage(player);
        sender.sendMessage(currentCount == newCount ? Messages.color(plugin.getMessages().addlifeLivesLimitHit().replace("%player%", args[0]))
            : Messages.color(plugin.getMessages().addlifeSuccess().replace("%player%", args[0])));
        return true;
    }
}