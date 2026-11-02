package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.LifeChanges;
import me.manossef.semihardcore.SemiHardcore;
import me.manossef.semihardcore.data.Messages;
import me.manossef.semihardcore.listeners.PlayerLifeGiveawayEvent;
import me.manossef.semihardcore.listeners.PlayerSacrificeEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the implementation of the plugin's {@code /givelife} command.
 */
public class GiveLifeCommand implements CommandExecutor {
    private final SemiHardcore plugin;

    public GiveLifeCommand(SemiHardcore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player donor)) {
            sender.sendMessage(ChatColor.RED + "The console does not have lives!");
            return true;
        }
        if(!plugin.getOptions().playersCanBeGenerous()) {
            donor.sendMessage(Messages.color(plugin.getMessages().givelifeDisabled()));
            return true;
        }
        if(args.length == 0) {
            donor.sendMessage(Messages.color(plugin.getMessages().commandUnspecifiedPlayer()));
            return true;
        }
        Player receiver = Bukkit.getPlayer(args[0]);
        if(receiver == null) {
            donor.sendMessage(Messages.color(plugin.getMessages().commandNotFound()));
            return true;
        }
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(receiver == donor) {
            donor.sendMessage(Messages.color(plugin.getMessages().givelifeSelfReference()));
            return true;
        }
        if(lifeChanges.getLives(donor) < 1) {
            donor.sendMessage(Messages.color(plugin.getMessages().givelifeNoLivesLeft()));
            return true;
        }
        if(lifeChanges.getLives(donor) == 1) {
            if(lifeChanges.getLives(receiver) == 0) {
                if(plugin.getOptions().sacrificesForRevival()) {
                    if(args.length > 1)
                        if(args[1].equals("confirm")) {
                            lifeChanges.addSacrificedPlayer(donor.getUniqueId());
                            lifeChanges.setLivesZeroAndKill(donor);
                            Bukkit.getPluginManager().callEvent(new PlayerSacrificeEvent(donor));
                            Bukkit.getPluginManager().callEvent(new PlayerLifeGiveawayEvent(donor, receiver));
                            lifeChanges.revivePlayer(receiver);
                            donor.playEffect(EntityEffect.TOTEM_RESURRECT);
                            lifeChanges.livesMessageAfterBeingGenerous(donor, receiver);
                            return true;
                        }
                    receiver.sendMessage(Messages.color(plugin.getMessages().givelifeRevivalWarning().replace("%donor%", donor.getName())));
                    donor.spigot().sendMessage(
                        new ComponentBuilder(Messages.color(plugin.getMessages().givelifeSacrificeConfirmation().replace("%receiver%", receiver.getName())))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Messages.color(plugin.getMessages().givelifeSacrificeHover()))))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/semihardcore:givelife " + args[0] + " confirm")).create());
                } else
                    donor.sendMessage(Messages.color(plugin.getMessages().givelifeOneLifeLeft()));
            } else {
                if(plugin.getOptions().playersCanGiveAwayLastLife()) {
                    if(lifeChanges.getLivesLimit() > 0)
                        if(lifeChanges.getLives(receiver) >= lifeChanges.getLivesLimit()) {
                            donor.sendMessage(Messages.color(plugin.getMessages().givelifeLivesLimitHit()));
                            return true;
                        }
                    if(args.length > 1)
                        if(args[1].equals("confirm")) {
                            lifeChanges.addSacrificedPlayer(donor.getUniqueId());
                            lifeChanges.setLivesZeroAndKill(donor);
                            Bukkit.getPluginManager().callEvent(new PlayerSacrificeEvent(donor));
                            Bukkit.getPluginManager().callEvent(new PlayerLifeGiveawayEvent(donor, receiver));
                            lifeChanges.setLives(receiver, lifeChanges.getLives(receiver) + 1);
                            donor.playEffect(EntityEffect.TOTEM_RESURRECT);
                            receiver.spawnParticle(Particle.VILLAGER_HAPPY, receiver.getLocation().getX(), receiver.getLocation().getY(),
                                receiver.getLocation().getZ(), 100, 1, 1, 1);
                            lifeChanges.livesMessageAfterGenerosity(receiver, donor);
                            lifeChanges.livesMessageAfterBeingGenerous(donor, receiver);
                            return true;
                        }
                    donor.spigot().sendMessage(
                        new ComponentBuilder(Messages.color(plugin.getMessages().givelifeGiveawayConfirmation().replace("%receiver%", receiver.getName())))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Messages.color(plugin.getMessages().givelifeGiveawayHover()))))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/semihardcore:givelife " + args[0] + " confirm")).create());
                } else
                    donor.sendMessage(Messages.color(plugin.getMessages().givelifeOneLifeLeft()));
            }
            return true;

        }
        if(lifeChanges.getLives(receiver) == 0) {
            donor.sendMessage(Messages.color(plugin.getMessages().givelifeCannotRevive()));
            return true;
        }
        if(lifeChanges.getLivesLimit() > 0)
            if(lifeChanges.getLives(receiver) >= lifeChanges.getLivesLimit()) {
                donor.sendMessage(Messages.color(plugin.getMessages().givelifeLivesLimitHit()));
                return true;
            }
        lifeChanges.setLives(receiver, lifeChanges.getLives(receiver) + 1);
        lifeChanges.setLives(donor, lifeChanges.getLives(donor) - 1);
        Bukkit.getPluginManager().callEvent(new PlayerLifeGiveawayEvent(donor, receiver));
        donor.playEffect(EntityEffect.TOTEM_RESURRECT);
        receiver.spawnParticle(Particle.VILLAGER_HAPPY, receiver.getLocation().getX(), receiver.getLocation().getY(), receiver.getLocation().getZ(), 100, 1, 1,
            1);
        lifeChanges.livesMessageAfterGenerosity(receiver, donor);
        lifeChanges.livesMessageAfterBeingGenerous(donor, receiver);
        return true;
    }
}