package me.manossef.semihardcore.data;

import me.manossef.api.alexandria.files.YamlFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * This class stores all messages used by the plugin. The plugin creates an instance of this class when it's enabled,
 * and uses it throughout the running of the server. Creating any new instances of this class has no effect on the
 * plugin.
 * @param welcomeConstant Message "welcome-message.constant". Appears when a player joins the server for the first time,
 *                        and the minimum and maximum starting lives are the name number.
 * @param welcomeRange Message "welcome-message.range". Appears when a player joins the server for the first time, and
 *                     the minimum and maximum starting lives are different numbers.
 * @param livesPlural Message "lives-message.plural". Appears when a player uses the {@code /lives} command and they
 *                    have more than one life left.
 * @param livesSingular Message "lives-message.singular". Appears when a player uses the {@code /lives} command and they
 *                      have one life left.
 * @param livesNone Message "lives-message.none". Appears when a player uses the {@code /lives} command and they have
 *                  zero lives left.
 * @param livesAfterDeathPlural Message "lives-message-after-death.plural". Appears when a player dies and is left with
 *                              more than one life.
 * @param livesAfterDeathSingular Message "lives-message-after-death.singular". Appears when a player dies and is left
 *                                with one life.
 * @param livesAfterDeathNone Message "lives-message-after-death.none". Appears when a player dies their final death.
 * @param livesAfterDeathNoneTitle Message "lives-message-after-death.none-title". Appears as a title when a player dies
 *                                 their final death.
 * @param livesTitleAfterDonation Message "lives-title-after-donation". Appears as a title when a player receives a life
 *                                through the {@code /givelife} command.
 * @param livesAfterDonationPlural Message "lives-message-after-donation.plural". Appears when a player receives a life
 *                                 and now has more than one life left.
 * @param livesAfterDonationSingular Message "lives-message-after-donation.singular". Appears when a player is revived
 *                                   by another player using the {@code /givelife} command.
 * @param livesAfterDonationNone Message "lives-message-after-donation.none". This message shouldn't ever appear, but it
 *                               exists just in case.
 * @param livesAfterBeingGenerousPlural Message "lives-message-after-being-generous.plural". Appears when a player gives
 *                                      away a life and is left with more than one life.
 * @param livesAfterBeingGenerousSingular Message "lives-message-after-being-generous.singular". Appears when a player
 *                                        gives away a life and is left with one life.
 * @param livesAfterBeingGenerousNone Message "lives-message-after-being-generous.none". Appears when a player gives
 *                                    away their last life.
 * @param livesAfterLifestealPlural Message "lives-message-after-lifesteal.plural". Appears when a player steals a life
 *                                  and is left with more than one life.
 * @param livesAfterLifestealSingular Message "lives-message-after-lifesteal.singular". This message shouldn't ever
 *                                    appear, but it exists just in case.
 * @param livesAfterLifestealNone Message "lives-message-after-lifesteal.none". This message shouldn't ever appear, but
 *                                it exists just in case.
 * @param revivePlural Message "revive-message.plural". Appears when a player is revived by the {@code /revive} command
 *                     and receives more than one life.
 * @param reviveSingular Message "revive-message.singular". Appears when a player is revived by the {@code /revive}
 *                       command and receives exactly one life.
 * @param reviveTitle Message "revive-title". Appears as a title when a player is revived.
 * @param commandUnspecifiedPlayer Message "command-messages.unspecified-player". Appears when a SemiHardcore command is
 *                                 used without a player specified.
 * @param commandNotFound Message "command-messages.not-found". Appears when a SemiHardcore command is used but the
 *                        specified player is not found.
 * @param commandCannotRevive Message "command-messages.cannot-revive". Appears when either the {@code /addlife} or
 *                            {@code /setlives} command is used on a dead player.
 * @param commandInvalidLives Message "command-messages.invalid-lives". Appears when either the {@code /revive} or
 *                            {@code /setlives} command is used with an invalid life count or a life count out of
 *                            bounds.
 * @param commandCanceled Message "command-messages.canceled". Appears when a life change caused by a command is
 *                        canceled.
 * @param givelifeDisabled Message "givelife-command-errors.disabled". Appears when a player tries to use the {@code
 *                         /givelife} command but giving away lives is disabled.
 * @param givelifeSelfReference Message "givelife-command-errors.self-reference". Appears when a player tries to use the
 *                              {@code /givelife} command on themself.
 * @param givelifeNoLivesLeft Message "givelife-command-errors.no-lives-left". Appears when a dead player tries to use
 *                            the {@code /givelife} command.
 * @param givelifeOneLifeLeft Message "givelife-command-errors.one-life-left". Appears when sacrificing/giving away a
 *                            last life is disabled and a player with only one life tries to use the {@code /givelife}
 *                            command.
 * @param givelifeCannotRevive Message "givelife-command-errors.cannot-revive". Appears when a player with more than one
 *                             life tries to give a life to a dead player, or if sacrificing is disabled and any alive
 *                             player tries to give a life to a dead player.
 * @param givelifeLivesLimitHit Message "givelife-command-errors.lives-limit-hit". Appears when a player tries to give a
 *                              life to a player who has hit the upper limit of lives.
 * @param givelifeDonorInDisabledWorld Message "givelife-command-errors.donor-in-disabled-world". Appears when a player
 *                                     tries to give a life while being in a world where life changes are disabled.
 * @param givelifeReceiverInDisabledWorld Message "givelife-command-errors.receiver-in-disabled-world". Appears when a
 *                                        players tries to give a life to a player in a world where life changes are
 *                                        disabled.
 * @param givelifeRevivalWarning Message "givelife-command-messages.revival-warning". Appears when a dead player is
 *                               about to be revived by an alive player being sacrificed.
 * @param givelifeSacrificeConfirmation Message "givelife-command-messages.sacrifice-confirmation". Appears when a
 *                                      player with one life tries to revive a player by sacrificing themselves, so that
 *                                      they can confirm their decision.
 * @param givelifeSacrificeHover Message "givelife-command-messages.sacrifice-hover". Appears as the hover event for the
 *                               {@link #givelifeSacrificeConfirmation} message.
 * @param givelifeGiveawayConfirmation Message "givelife-command-messages.giveaway-confirmation". Appears when a player
 *                                     with one life tries to give away their life to an alive player, so that they can
 *                                     confirm their decision.
 * @param givelifeGiveawayHover Message "givelife-command-messages.giveaway-hover". Appears as the hover event for the
 *                              {@link #givelifeGiveawayConfirmation} message.
 * @param addlifeLivesLimitHit Message "addlife-command-messages.lives-limit-hit". Appears when the {@code /addlife}
 *                             command tries to add a life to a player who has hit the upper limit of lives.
 * @param addlifeSuccess Message "addlife-command-messages.success". Appears when the {@code /addlife} command
 *                       successfully adds a life to a player.
 * @param setlivesUnspecifiedLives Message "setlives-command-messages.unspecified-lives". Appears when the {@code
 *                                 /setlives} command is used without a life count specified.
 * @param setlivesLimitedSuccess Message "setlives-command-messages.limited-success". Appears when the {@code /setlives}
 *                               command successfully changes a player's life count to the upper limit of lives because
 *                               the life count specified was greater than that.
 * @param setlivesSuccess Message "setlives-command-messages.success". Appears when the {@code /setlives} command
 *                        successfully changes a player's life count to the specified number.
 * @param reviveAlivePlayer Message "revive-command-messages.alive-player". Appears when the {@code /revive} command is
 *                          used on an alive player.
 * @param reviveSuccess Message "revive-command-messages.success". Appears when the {@code /revive} command successfully
 *                      revives a player.
 * @param reviveLimitedSuccess Message "revive-command-messages.limited-success". Appears when the {@code /revive}
 *                             command successfully revives a player with the upper limit of lives as the life count
 *                             because the life count specified was greater than that.
 */
public record Messages(
    String welcomeConstant,
    String welcomeRange,
    String livesPlural,
    String livesSingular,
    String livesNone,
    String livesAfterDeathPlural,
    String livesAfterDeathSingular,
    String livesAfterDeathNone,
    String livesAfterDeathNoneTitle,
    String livesTitleAfterDonation,
    String livesAfterDonationPlural,
    String livesAfterDonationSingular,
    String livesAfterDonationNone,
    String livesAfterBeingGenerousPlural,
    String livesAfterBeingGenerousSingular,
    String livesAfterBeingGenerousNone,
    String livesAfterLifestealPlural,
    String livesAfterLifestealSingular,
    String livesAfterLifestealNone,
    String revivePlural,
    String reviveSingular,
    String reviveTitle,
    String commandUnspecifiedPlayer,
    String commandNotFound,
    String commandCannotRevive,
    String commandInvalidLives,
    String commandCanceled,
    String givelifeDisabled,
    String givelifeSelfReference,
    String givelifeNoLivesLeft,
    String givelifeOneLifeLeft,
    String givelifeCannotRevive,
    String givelifeLivesLimitHit,
    String givelifeDonorInDisabledWorld,
    String givelifeReceiverInDisabledWorld,
    String givelifeRevivalWarning,
    String givelifeSacrificeConfirmation,
    String givelifeSacrificeHover,
    String givelifeGiveawayConfirmation,
    String givelifeGiveawayHover,
    String addlifeLivesLimitHit,
    String addlifeSuccess,
    String setlivesUnspecifiedLives,
    String setlivesLimitedSuccess,
    String setlivesSuccess,
    String reviveAlivePlayer,
    String reviveSuccess,
    String reviveLimitedSuccess
) {
    public Messages(YamlFile messagesFile) {
        this(
            messagesFile.getConfig().getString("welcome-message.constant",
                "&aWelcome to SemiHardcore! Every player starts with %lives% lives, and every time one dies they lose one. When one loses all their lives, they will from then on be a spectator. You can use /lives to see how many lives you have at any specific time."),
            messagesFile.getConfig().getString("welcome-message.range",
                "&aWelcome to SemiHardcore! Every player starts with a random number of lives between %min% and %max%, and every time one dies they lose one. When one loses all their lives, they will from then on be a spectator. You are starting with %lives% lives. You can use /lives to see how many lives you have at any specific time."),
            messagesFile.getConfig().getString("lives-message.plural", "&aYou have %lives% lives left."),
            messagesFile.getConfig().getString("lives-message.singular", "&aYou only have %lives% life left."),
            messagesFile.getConfig().getString("lives-message.none", "&cYou have no lives left. You are a spectator."),
            messagesFile.getConfig().getString("lives-message-after-death.plural", "&cYou lost a life! You have %lives% lives left."),
            messagesFile.getConfig().getString("lives-message-after-death.singular", "&cYou lost a life! You only have %lives% life left."),
            messagesFile.getConfig().getString("lives-message-after-death.none", "&cYou lost a life! You have no lives left. You are now a spectator."),
            messagesFile.getConfig().getString("lives-message-after-death.none-title", "&cYou have run out of lives!"),
            messagesFile.getConfig().getString("lives-title-after-donation", "&bYou received a life!"),
            messagesFile.getConfig().getString("lives-message-after-donation.plural",
                "&aYou received a life from %donor%. You have %lives% lives left."),
            messagesFile.getConfig().getString("lives-message-after-donation.singular",
                "&aYou received a life from %donor%. You only have %lives% life left."),
            messagesFile.getConfig().getString("lives-message-after-donation.none",
                "&cYou received a life from %donor%. You have no lives left. You are a spectator."),
            messagesFile.getConfig().getString("lives-message-after-being-generous.plural",
                "&aYou gave a life to %receiver%. You have %lives% lives left."),
            messagesFile.getConfig().getString("lives-message-after-being-generous.singular",
                "&aYou gave a life to %receiver%. You only have %lives% life left."),
            messagesFile.getConfig().getString("lives-message-after-being-generous.none",
                "&cYou gave a life to %receiver%. You have no lives left. You are a spectator."),
            messagesFile.getConfig().getString("lives-message-after-lifesteal.plural", "&aYou stole a life from %victim%! You have %lives% lives left."),
            messagesFile.getConfig().getString("lives-message-after-lifesteal.singular", "&aYou stole a life from %victim%! You only have %lives% life left."),
            messagesFile.getConfig().getString("lives-message-after-lifesteal.none", "&aYou stole a life from %victim%! You have no lives left. You are a spectator."),
            messagesFile.getConfig().getString("revive-message.plural", "&aYou have been revived! You now have %lives% lives left."),
            reviveMessageSingular(messagesFile),
            messagesFile.getConfig().getString("revive-title", "&dYou are revived!"),
            commandUnspecifiedPlayer(messagesFile),
            commandNotFound(messagesFile),
            commandCannotRevive(messagesFile),
            commandInvalidLives(messagesFile),
            messagesFile.getConfig().getString("command-messages.canceled", "&cThe life change was canceled by an external factor."),
            messagesFile.getConfig().getString("givelife-command-errors.disabled", "&cGenerosity has been turned off."),
            messagesFile.getConfig().getString("givelife-command-errors.self-reference", "&cYou cannot give a life to yourself."),
            messagesFile.getConfig().getString("givelife-command-errors.no-lives-left", "&cYou cannot give a life to anybody if you don't have any."),
            messagesFile.getConfig().getString("givelife-command-errors.one-life-left",
                "&cYou only have one life left. If you gave a life to somebody at this point, you would immediately die."),
            messagesFile.getConfig().getString("givelife-command-errors.cannot-revive", "&cYou cannot give a life to a dead player."),
            messagesFile.getConfig().getString("givelife-command-errors.lives-limit-hit",
                "&cThat player has reached the life limit and, as a result, you can't give them a life."),
            messagesFile.getConfig().getString("givelife-command-errors.donor-in-disabled-world", "&cYou cannot give lives while in this world."),
            messagesFile.getConfig().getString("givelife-command-errors.receiver-in-disabled-world", "&cThat player is in a world where they cannot gain or lose lives."),
            messagesFile.getConfig().getString("givelife-command-messages.revival-warning",
                "&cWarning! %donor% is about to sacrifice themselves so you get revived!"),
            messagesFile.getConfig().getString("givelife-command-messages.sacrifice-confirmation",
                "&c&lAre you sure you want to sacrifice your last life to revive %receiver%? Click this message to confirm your decision."),
            messagesFile.getConfig().getString("givelife-command-messages.sacrifice-hover", "&eClick here to sacrifice your last life"),
            messagesFile.getConfig().getString("givelife-command-messages.giveaway-confirmation",
                "&c&lAre you sure you want to give away your last life to %receiver%? Click this message to confirm your decision."),
            messagesFile.getConfig().getString("givelife-command-messages.giveaway-hover", "&eClick here to give your last life away"),
            messagesFile.getConfig().getString("addlife-command-messages.lives-limit-hit",
                "&cCould not add a life to %player% as they have reached the life limit."),
            messagesFile.getConfig().getString("addlife-command-messages.success", "&aAdded a life to %player%."),
            messagesFile.getConfig().getString("setlives-command-messages.unspecified-lives", "&cYou need to specify a number of lives."),
            messagesFile.getConfig().getString("setlives-command-messages.limited-success",
                "&aSet %player%'s lives to %limit% (your input, %lives%, is above the life limit)."),
            messagesFile.getConfig().getString("setlives-command-messages.success", "&aSet %player%'s lives to %lives%."),
            messagesFile.getConfig().getString("revive-command-messages.alive-player", "&cThat player is alive."),
            messagesFile.getConfig().getString("revive-command-messages.success", "&aRevived %player% with a life count of %lives%."),
            messagesFile.getConfig().getString("revive-command-messages.limited-success",
                "&aRevived %player% with a life count of %limit% (your input, %lives%, is above the life limit).")
        );
    }

    /**
     * Formats the provided string as if by {@link ChatColor#translateAlternateColorCodes(char, String)} with
     * <code>'&'</code> as the <code>altColorChar</code> and returns the final string.
     * @param message The string to format.
     * @return The message with alternate color codes translated.
     */
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Saves the messages to the messages.yml file.
     * @param file The messages.yml file configuration.
     */
    public void saveToFile(FileConfiguration file) {
        file.set("welcome-message.constant", this.welcomeConstant);
        file.set("welcome-message.range", this.welcomeRange);
        file.set("lives-message.plural", this.livesPlural);
        file.set("lives-message.singular", this.livesSingular);
        file.set("lives-message.none", this.livesNone);
        file.set("lives-message-after-death.plural", this.livesAfterDeathPlural);
        file.set("lives-message-after-death.singular", this.livesAfterDeathSingular);
        file.set("lives-message-after-death.none", this.livesAfterDeathNone);
        file.set("lives-message-after-death.none-title", this.livesAfterDeathNoneTitle);
        file.set("lives-title-after-donation", this.livesTitleAfterDonation);
        file.set("lives-message-after-donation.plural", this.livesAfterDonationPlural);
        file.set("lives-message-after-donation.singular", this.livesAfterDonationSingular);
        file.set("lives-message-after-donation.none", this.livesAfterDonationNone);
        file.set("lives-message-after-being-generous.plural", this.livesAfterBeingGenerousPlural);
        file.set("lives-message-after-being-generous.singular", this.livesAfterBeingGenerousSingular);
        file.set("lives-message-after-being-generous.none", this.livesAfterBeingGenerousNone);
        file.set("lives-message-after-lifesteal.plural", this.livesAfterLifestealPlural);
        file.set("lives-message-after-lifesteal.singular", this.livesAfterLifestealSingular);
        file.set("lives-message-after-lifesteal.none", this.livesAfterLifestealNone);
        file.set("revive-message", null);
        file.set("revive-message.plural", this.revivePlural);
        file.set("revive-message.singular", this.reviveSingular);
        file.set("revive-title", this.reviveTitle);
        file.set("command-messages.unspecified-player", this.commandUnspecifiedPlayer);
        file.set("givelife-command-errors.unspecified-player", null);
        file.set("addlife-command-messages.unspecified-player", null);
        file.set("setlives-command-messages.unspecified-player", null);
        file.set("revive-command-messages.unspecified-player", null);
        file.set("command-messages.not-found", this.commandNotFound);
        file.set("givelife-command-errors.not-found", null);
        file.set("addlife-command-messages.not-found", null);
        file.set("setlives-command-messages.not-found", null);
        file.set("revive-command-messages.not-found", null);
        file.set("command-messages.cannot-revive", this.commandCannotRevive);
        file.set("addlife-command-messages.cannot-revive", null);
        file.set("setlives-command-messages.cannot-revive", null);
        file.set("command-messages.invalid-lives", this.commandInvalidLives);
        file.set("command-messages.canceled", this.commandCanceled);
        file.set("givelife-command-errors.disabled", this.givelifeDisabled);
        file.set("givelife-command-errors.self-reference", this.givelifeSelfReference);
        file.set("givelife-command-errors.no-lives-left", this.givelifeNoLivesLeft);
        file.set("givelife-command-errors.one-life-left", this.givelifeOneLifeLeft);
        file.set("givelife-command-errors.cannot-revive", this.givelifeCannotRevive);
        file.set("givelife-command-errors.lives-limit-hit", this.givelifeLivesLimitHit);
        file.set("givelife-command-errors.donor-in-disabled-world", this.givelifeDonorInDisabledWorld);
        file.set("givelife-command-errors.receiver-in-disabled-world", this.givelifeReceiverInDisabledWorld);
        file.set("givelife-command-messages.revival-warning", this.givelifeRevivalWarning);
        file.set("givelife-command-messages.sacrifice-confirmation", this.givelifeSacrificeConfirmation);
        file.set("givelife-command-messages.sacrifice-hover", this.givelifeSacrificeHover);
        file.set("givelife-command-messages.giveaway-confirmation", this.givelifeGiveawayConfirmation);
        file.set("givelife-command-messages.giveaway-hover", this.givelifeGiveawayHover);
        file.set("addlife-command-messages.lives-limit-hit", this.addlifeLivesLimitHit);
        file.set("addlife-command-messages.success", this.addlifeSuccess);
        file.set("setlives-command-messages.unspecified-lives", this.setlivesUnspecifiedLives);
        file.set("setlives-command-messages.invalid-lives", null);
        file.set("setlives-command-messages.limited-success", this.setlivesLimitedSuccess);
        file.set("setlives-command-messages.success", this.setlivesSuccess);
        file.set("revive-command-messages.alive-player", this.reviveAlivePlayer);
        file.set("revive-command-messages.success", this.reviveSuccess);
        file.set("revive-command-messages.limited-success", this.reviveLimitedSuccess);
    }

    private static String reviveMessageSingular(YamlFile messagesFile) {
        FileConfiguration messages = messagesFile.getConfig();
        String singular = messages.getString("revive-message.singular");
        if(singular != null) return singular;
        else return messages.getString("revive-message", "&aYou have been revived! You now have %lives% life left.");
    }

    private static String commandUnspecifiedPlayer(YamlFile messagesFile) {
        FileConfiguration messages = messagesFile.getConfig();
        String command = messages.getString("command-messages.unspecified-player");
        if(command != null) return command;
        String givelife = messages.getString("givelife-command-errors.unspecified-player");
        if(givelife != null) return givelife;
        String addlife = messages.getString("addlife-command-messages.unspecified-player");
        if(addlife != null) return addlife;
        String setlives = messages.getString("setlives-command-messages.unspecified-player");
        if(setlives != null) return setlives;
        return messages.getString("revive-command-messages.unspecified-player", "&cYou need to specify a player.");
    }

    private static String commandNotFound(YamlFile messagesFile) {
        FileConfiguration messages = messagesFile.getConfig();
        String command = messages.getString("command-messages.not-found");
        if(command != null) return command;
        String givelife = messages.getString("givelife-command-errors.not-found");
        if(givelife != null) return givelife;
        String addlife = messages.getString("addlife-command-messages.not-found");
        if(addlife != null) return addlife;
        String setlives = messages.getString("setlives-command-messages.not-found");
        if(setlives != null) return setlives;
        return messages.getString("revive-command-messages.not-found", "&cPlayer not found.");
    }

    private static String commandCannotRevive(YamlFile messagesFile) {
        FileConfiguration messages = messagesFile.getConfig();
        String command = messages.getString("command-messages.cannot-revive");
        if(command != null) return command;
        String addlife = messages.getString("addlife-command-messages.cannot-revive");
        if(addlife != null) return addlife;
        return messages.getString("setlives-command-messages.cannot-revive", "&cPlease use the /revive command to revive players.");
    }

    private static String commandInvalidLives(YamlFile messagesFile) {
        FileConfiguration messages = messagesFile.getConfig();
        String command = messages.getString("command-messages.invalid-lives");
        if(command != null) return command;
        return messages.getString("setlives-command-messages.invalid-lives", "&cInvalid number of lives.");
    }
}