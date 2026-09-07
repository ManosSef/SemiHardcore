package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.SemiHardcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the implementation of the plugin's {@code /revive} command.
 */
public class ReviveCommand implements TabExecutor {

    private final SemiHardcore plugin;

    public ReviveCommand(SemiHardcore instance) {

        plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0) {

            sender.sendMessage(color(plugin.getMessages().commandUnspecifiedPlayer));
            return true;

        }
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {

            sender.sendMessage(color(plugin.getMessages().commandNotFound));
            return true;

        }
        if(plugin.getLifeChanges().getLives(player) != 0) {

            sender.sendMessage(color(plugin.getMessages().reviveAlivePlayer));
            return true;

        }
        plugin.getLifeChanges().revivePlayer(player);
        sender.sendMessage(color(plugin.getMessages().reviveSuccess).replace("%player%", args[0]));
        return true;

    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 1) {

            List<String> arguments = new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            arguments.removeIf(argument -> !argument.toLowerCase().startsWith(args[0].toLowerCase()));
            return arguments;

        }
        return new ArrayList<>();

    }

    private String color(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

}
