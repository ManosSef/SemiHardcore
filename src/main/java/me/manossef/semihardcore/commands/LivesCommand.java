package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.SemiHardcore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the implementation of the plugin's {@code /lives} command.
 */
public class LivesCommand implements TabExecutor {

    private final SemiHardcore plugin;

    public LivesCommand(SemiHardcore instance) {

        plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player player)) {

            sender.sendMessage(ChatColor.RED + "The console does not have lives!");
            return true;

        }
        plugin.getLifeChanges().livesMessage(player);
        return true;

    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        return new ArrayList<>();

    }

}
