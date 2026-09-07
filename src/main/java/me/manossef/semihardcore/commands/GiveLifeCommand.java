package me.manossef.semihardcore.commands;

import me.manossef.semihardcore.LifeChanges;
import me.manossef.semihardcore.SemiHardcore;
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
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the implementation of the plugin's {@code /givelife} command.
 */
public class GiveLifeCommand implements TabExecutor {

    private final SemiHardcore plugin;

    public GiveLifeCommand(SemiHardcore instance) {

        plugin = instance;

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player donor)) {

            sender.sendMessage(ChatColor.RED + "The console does not have lives!");
            return true;

        }
        if(!plugin.getOptions().playersCanBeGenerous()) {

            donor.sendMessage(color(plugin.getMessages().givelifeDisabled));
            return true;

        }
        if(args.length == 0) {

            donor.sendMessage(color(plugin.getMessages().commandUnspecifiedPlayer));
            return true;

        }
        Player receiver = Bukkit.getPlayer(args[0]);
        if(receiver == null) {

            donor.sendMessage(color(plugin.getMessages().commandNotFound));
            return true;

        }
        LifeChanges lifeChanges = plugin.getLifeChanges();
        if(receiver == donor) {

            donor.sendMessage(color(plugin.getMessages().givelifeSelfReference));
            return true;

        }
        if(lifeChanges.getLives(donor) < 1) {

            donor.sendMessage(color(plugin.getMessages().givelifeNoLivesLeft));
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
                    receiver.sendMessage(color(plugin.getMessages().givelifeRevivalWarning.replace("%donor%", donor.getName())));
                    donor.spigot().sendMessage(
                        new ComponentBuilder(color(plugin.getMessages().givelifeSacrificeConfirmation.replace("%receiver%", receiver.getName())))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(color(plugin.getMessages().givelifeSacrificeHover))))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/semihardcore:givelife " + args[0] + " confirm")).create());

                } else
                    donor.sendMessage(color(plugin.getMessages().givelifeOneLifeLeft));

            } else {

                if(plugin.getOptions().playersCanGiveAwayLastLife()) {

                    if(lifeChanges.getLivesLimit() > 0)
                        if(lifeChanges.getLives(receiver) >= lifeChanges.getLivesLimit()) {

                            donor.sendMessage(color(plugin.getMessages().givelifeLivesLimitHit));
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
                        new ComponentBuilder(color(plugin.getMessages().givelifeGiveawayConfirmation.replace("%receiver%", receiver.getName())))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(color(plugin.getMessages().givelifeGiveawayHover))))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/semihardcore:givelife " + args[0] + " confirm")).create());

                } else
                    donor.sendMessage(color(plugin.getMessages().givelifeOneLifeLeft));

            }
            return true;

        }
        if(lifeChanges.getLives(receiver) == 0) {

            donor.sendMessage(color(plugin.getMessages().givelifeCannotRevive));
            return true;

        }
        if(lifeChanges.getLivesLimit() > 0)
            if(lifeChanges.getLives(receiver) >= lifeChanges.getLivesLimit()) {

                donor.sendMessage(color(plugin.getMessages().givelifeLivesLimitHit));
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
