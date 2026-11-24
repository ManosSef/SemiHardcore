package me.manossef.semihardcore.commands;

import me.manossef.api.alexandria.TypeChecks;
import me.manossef.semihardcore.LifeChanges;
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
public final class ReviveCommand implements CommandExecutor {
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
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(lifeChanges.getLives(player) != 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().reviveAlivePlayer()));
            return true;
        }
        if(args.length == 1) {
            lifeChanges.revivePlayer(player);
            sender.sendMessage(Messages.color(plugin.getMessages().reviveSuccess()).replace("%player%", args[0]).replace("%lives%", String.valueOf(1)));
            return true;
        }
        if(!TypeChecks.isInt(args[1])) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandInvalidLives()));
            return true;
        }
        int lives = Integer.parseInt(args[1]);
        if(lives <= 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandInvalidLives()));
            return true;
        }
        lifeChanges.revivePlayer(player, lives);
        int newCount = lifeChanges.getLives(player);
        if(newCount != lives) sender.sendMessage(Messages.color(plugin.getMessages().reviveLimitedSuccess()).replace("%player%", args[0]).replace("%lives%", args[1]).replace("%limit%", String.valueOf(newCount)));
        else sender.sendMessage(Messages.color(plugin.getMessages().reviveSuccess()).replace("%player%", args[0]).replace("%lives%", args[1]));
        return true;
    }
}