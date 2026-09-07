package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.LifeChanges;
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
 * Handles the implementation of the plugin's {@code /setlives} command.
 */
public class SetLivesCommand implements TabExecutor {

    private final SemiHardcore plugin;

    public SetLivesCommand(SemiHardcore instance) {

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
        if(args.length == 1) {

            sender.sendMessage(color(plugin.getMessages().setlivesUnspecifiedLives));
            return true;

        }
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(!isInt(args[1])) {

            sender.sendMessage(color(plugin.getMessages().setlivesInvalidLives));
            return true;

        }
        if(Integer.parseInt(args[1]) < 0) {

            sender.sendMessage(color(plugin.getMessages().setlivesInvalidLives));
            return true;

        }
        int livesPreviously = lifeChanges.getLives(player);
        int livesAfterwards = Integer.parseInt(args[1]);
        if(livesPreviously != 0 && livesAfterwards == 0) {

            lifeChanges.setLivesZeroAndKill(player);
            sender.sendMessage(color(plugin.getMessages().setlivesSuccess.replace("%player%", args[0]).replace("%lives%", args[1])));

        } else if(livesPreviously == 0 && livesAfterwards != 0) {

            sender.sendMessage(color(plugin.getMessages().commandCannotRevive));

        } else {

            lifeChanges.setLives(player, livesAfterwards);
            int newCount = lifeChanges.getLives(player);
            lifeChanges.livesMessage(player);
            sender.sendMessage(livesAfterwards > newCount
                ? color(plugin.getMessages().setlivesLimitedSuccess.replace("%player%", args[0]).replace("%lives%", String.valueOf(livesAfterwards)).replace("%limit%",
                Integer.toString(lifeChanges.getLivesLimit())))
                : color(plugin.getMessages().setlivesSuccess.replace("%player%", args[0]).replace("%lives%", String.valueOf(newCount))));

        }
        return true;

    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 1) {

            List<String> arguments = new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            arguments.removeIf(argument -> !argument.toLowerCase().startsWith(args[0].toLowerCase()));
            return arguments;

        }
        if(args.length == 2) {

            List<String> arguments = new ArrayList<>(List.of("1", "2", "3", "4", "5"));
            arguments.removeIf(argument -> !argument.toLowerCase().startsWith(args[0].toLowerCase()));
            return arguments;

        }
        return new ArrayList<>();

    }

    private String color(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    private boolean isInt(String input) {

        try {

            Integer.parseInt(input);
            return true;

        } catch(Exception e) {

            return false;

        }

    }

}
