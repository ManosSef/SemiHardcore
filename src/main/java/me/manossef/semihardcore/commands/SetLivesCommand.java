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
 * Handles the implementation of the plugin's {@code /setlives} command.
 */
public final class SetLivesCommand implements CommandExecutor {
    private final SemiHardcore plugin;

    public SetLivesCommand(SemiHardcore plugin) {
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
        if(args.length == 1) {
            sender.sendMessage(Messages.color(plugin.getMessages().setlivesUnspecifiedLives()));
            return true;
        }
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(!TypeChecks.isInt(args[1])) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandInvalidLives()));
            return true;
        }
        int livesAfterwards = Integer.parseInt(args[1]);
        if(livesAfterwards < 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandInvalidLives()));
            return true;
        }
        int livesPreviously = lifeChanges.getLives(player);
        if(livesPreviously != 0 && livesAfterwards == 0) {
            if(!lifeChanges.setLivesZeroAndKill(player)) {
                sender.sendMessage(Messages.color(plugin.getMessages().commandCanceled()));
                return true;
            }
            sender.sendMessage(Messages.color(plugin.getMessages().setlivesSuccess().replace("%player%", args[0]).replace("%lives%", args[1])));
        } else if(livesPreviously == 0 && livesAfterwards != 0) {
            sender.sendMessage(Messages.color(plugin.getMessages().commandCannotRevive()));
        } else {
            if(!lifeChanges.setLives(player, livesAfterwards)) {
                sender.sendMessage(Messages.color(plugin.getMessages().commandCanceled()));
                return true;
            }
            int newCount = lifeChanges.getLives(player);
            lifeChanges.livesMessage(player);
            sender.sendMessage(livesAfterwards > newCount
                ? Messages.color(plugin.getMessages().setlivesLimitedSuccess().replace("%player%", args[0]).replace("%lives%", String.valueOf(livesAfterwards)).replace("%limit%",
                Integer.toString(lifeChanges.getLivesLimit())))
                : Messages.color(plugin.getMessages().setlivesSuccess().replace("%player%", args[0]).replace("%lives%", String.valueOf(newCount))));
        }
        return true;
    }
}