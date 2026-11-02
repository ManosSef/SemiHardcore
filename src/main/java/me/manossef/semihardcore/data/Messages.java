package me.manossef.semihardcore.data;

import me.manossef.api.alexandria.files.YamlFile;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * This class stores all messages used by the plugin. The plugin creates an instance of this class when it's enabled,
 * and uses it throughout the running of the server. Creating any new instances of this class has no effect on the
 * plugin.
 */
public class Messages {

    /**
     * Message "welcome-message.constant". Appears when a player joins the server for the first time, and the minimum
     * and maximum starting lives are the name number.
     */
    public final String welcomeConstant;
    /**
     * Message "welcome-message.range". Appears when a player joins the server for the first time, and the minimum and
     * maximum starting lives are different numbers.
     */
    public final String welcomeRange;
    /**
     * Message "lives-message.plural". Appears when a player uses the {@code /lives} command and they have more than one
     * life left.
     */
    public final String livesPlural;
    /**
     * Message "lives-message.singular". Appears when a player uses the {@code /lives} command and they have one life
     * left.
     */
    public final String livesSingular;
    /**
     * Message "lives-message.none". Appears when a player uses the {@code /lives} command and they have zero lives
     * left.
     */
    public final String livesNone;
    /**
     * Message "lives-message-after-death.plural". Appears when a player dies and is left with more than one life.
     */
    public final String livesAfterDeathPlural;
    /**
     * Message "lives-message-after-death.singular". Appears when a player dies and is left with one life.
     */
    public final String livesAfterDeathSingular;
    /**
     * Message "lives-message-after-death.none". Appears when a player dies their final death.
     */
    public final String livesAfterDeathNone;
    /**
     * Message "lives-message-after-death.none-title". Appears as a title when a player dies their final death.
     */
    public final String livesAfterDeathNoneTitle;
    /**
     * Message "lives-title-after-donation". Appears as a title when a player receives a life through the
     * {@code /givelife} command.
     */
    public final String livesTitleAfterDonation;
    /**
     * Message "lives-message-after-donation.plural". Appears when a player receives a life and now has more than one
     * life left.
     */
    public final String livesAfterDonationPlural;
    /**
     * Message "lives-message-after-donation.singular". Appears when a player is revived by another player using the
     * {@code /givelife} command.
     */
    public final String livesAfterDonationSingular;
    /**
     * Message "lives-message-after-donation.none". This message shouldn't ever appear, but it exists just in case.
     */
    public final String livesAfterDonationNone;
    /**
     * Message "lives-message-after-being-generous.plural". Appears when a player gives away a life and is left with
     * more than one life.
     */
    public final String livesAfterBeingGenerousPlural;
    /**
     * Message "lives-message-after-being-generous.singular". Appears when a player gives away a life and is left with
     * one life.
     */
    public final String livesAfterBeingGenerousSingular;
    /**
     * Message "lives-message-after-being-generous.none". Appears when a player gives away their last life.
     */
    public final String livesAfterBeingGenerousNone;
    /**
     * Message "lives-message-after-lifesteal.plural". Appears when a player steals a life and is left with more than
     * one life.
     */
    public final String livesAfterLifestealPlural;
    /**
     * Message "lives-message-after-lifesteal.singular". This message shouldn't ever appear, but it exists just in
     * case.
     */
    public final String livesAfterLifestealSingular;
    /**
     * Message "lives-message-after-lifesteal.none". This message shouldn't ever appear, but it exists just in case.
     */
    public final String livesAfterLifestealNone;
    /**
     * Message "revive-message". Appears when a player is revived by the {@code /revive} command.
     */
    public final String revive;
    /**
     * Message "revive-title". Appears as a title when a player is revived.
     */
    public final String reviveTitle;
    /**
     * Message "command-messages.unspecified-player". Appears when a SemiHardcore command is used without a player
     * specified.
     */
    public final String commandUnspecifiedPlayer;
    /**
     * Message "command-messages.not-found". Appears when a SemiHardcore command is used but the specified player is not
     * found.
     */
    public final String commandNotFound;
    /**
     * Message "command-messages.cannot-revive". Appears when either the {@code /addlife} or {@code /setlives} command
     * is used on a dead player.
     */
    public final String commandCannotRevive;
    /**
     * Message "givelife-command-errors.disabled". Appears when a player tries to use the {@code /givelife} command but
     * giving away lives is disabled.
     */
    public final String givelifeDisabled;
    /**
     * Message "givelife-command-errors.self-reference". Appears when a player tries to use the {@code /givelife}
     * command on themself.
     */
    public final String givelifeSelfReference;
    /**
     * Message "givelife-command-errors.no-lives-left". Appears when a dead player tries to use the {@code /givelife}
     * command.
     */
    public final String givelifeNoLivesLeft;
    /**
     * Message "givelife-command-errors.one-life-left". Appears when sacrificing/giving away a last life is disabled and
     * a player with only one life tries to use the {@code /givelife} command.
     */
    public final String givelifeOneLifeLeft;
    /**
     * Message "givelife-command-errors.cannot-revive". Appears when a player with more than one life tries to give a
     * life to a dead player, or if sacrificing is disabled and any alive player tries to give a life to a dead player.
     */
    public final String givelifeCannotRevive;
    /**
     * Message "givelife-command-errors.lives-limit-hit". Appears when a player tries to give a life to a player who has
     * hit the upper limit of lives.
     */
    public final String givelifeLivesLimitHit;
    /**
     * Message "givelife-command-messages.revival-warning". Appears when a dead player is about to be revived by an
     * alive player being sacrificed.
     */
    public final String givelifeRevivalWarning;
    /**
     * Message "givelife-command-messages.sacrifice-confirmation". Appears when a player with one life tries to revive a
     * player by sacrificing themselves, so that they can confirm their decision.
     */
    public final String givelifeSacrificeConfirmation;
    /**
     * Message "givelife-command-messages.sacrifice-hover". Appears as the hover event for the
     * {@link #givelifeSacrificeConfirmation} message.
     */
    public final String givelifeSacrificeHover;
    /**
     * Message "givelife-command-messages.giveaway-confirmation". Appears when a player with one life tries to give away
     * their life to an alive player, so that they can confirm their decision.
     */
    public final String givelifeGiveawayConfirmation;
    /**
     * Message "givelife-command-messages.giveaway-hover". Appears as the hover event for the
     * {@link #givelifeGiveawayConfirmation} message.
     */
    public final String givelifeGiveawayHover;
    /**
     * Message "addlife-command-messages.lives-limit-hit". Appears when the {@code /addlife} command tries to add a life
     * to a player who has hit the upper limit of lives.
     */
    public final String addlifeLivesLimitHit;
    /**
     * Message "addlife-command-messages.success". Appears when the {@code /addlife} command successfully adds a life to
     * a player.
     */
    public final String addlifeSuccess;
    /**
     * Message "setlives-command-messages.unspecified-lives". Appears when the {@code /setlives} command is used without
     * a life count specified.
     */
    public final String setlivesUnspecifiedLives;
    /**
     * Message "setlives-command-messages.invalid-lives". Appears when the {@code /setlives} command is used with an
     * invalid life count or a life count out of bounds.
     */
    public final String setlivesInvalidLives;
    /**
     * Message "setlives-command-messages.limited-success". Appears when the {@code /setlives} command successfully
     * changes a player's life count to the upper limit of lives because the life count specified was greater than
     * that.
     */
    public final String setlivesLimitedSuccess;
    /**
     * Message "setlives-command-messages.success". Appears when the {@code /setlives} command successfully changes a
     * player's life count to the specified number.
     */
    public final String setlivesSuccess;
    /**
     * Message "revive-command-messages.alive-player". Appears when the {@code /revive} command is used on an alive
     * player.
     */
    public final String reviveAlivePlayer;
    /**
     * Message "revive-command-messages.success". Appears when the {@code /revive} command successfully revives a
     * player.
     */
    public final String reviveSuccess;
    private final YamlFile messagesFile;

    public Messages(YamlFile messagesFile) {
        this.messagesFile = messagesFile;
        FileConfiguration messages = messagesFile.getConfig();
        this.welcomeConstant = messages.getString("welcome-message.constant",
            "&aWelcome to SemiHardcore! Every player starts with %lives% lives, and every time one dies they lose one. When one loses all their lives, they will from then on be a spectator. You can use /lives to see how many lives you have at any specific time.");
        this.welcomeRange = messages.getString("welcome-message.range",
            "&aWelcome to SemiHardcore! Every player starts with a random number of lives between %min% and %max%, and every time one dies they lose one. When one loses all their lives, they will from then on be a spectator. You are starting with %lives% lives. You can use /lives to see how many lives you have at any specific time.");
        this.livesPlural = messages.getString("lives-message.plural", "&aYou have %lives% lives left.");
        this.livesSingular = messages.getString("lives-message.singular", "&aYou only have %lives% life left.");
        this.livesNone = messages.getString("lives-message.none", "&cYou have no lives left. You are a spectator.");
        this.livesAfterDeathPlural = messages.getString("lives-message-after-death.plural", "&cYou lost a life! You have %lives% lives left.");
        this.livesAfterDeathSingular = messages.getString("lives-message-after-death.singular", "&cYou lost a life! You only have %lives% life left.");
        this.livesAfterDeathNone = messages.getString("lives-message-after-death.none", "&cYou lost a life! You have no lives left. You are now a spectator.");
        this.livesAfterDeathNoneTitle = messages.getString("lives-message-after-death.none-title", "&cYou have run out of lives!");
        this.livesTitleAfterDonation = messages.getString("lives-title-after-donation", "&bYou received a life!");
        this.livesAfterDonationPlural = messages.getString("lives-message-after-donation.plural",
            "&aYou received a life from %donor%. You have %lives% lives left.");
        this.livesAfterDonationSingular = messages.getString("lives-message-after-donation.singular",
            "&aYou received a life from %donor%. You only have %lives% life left.");
        this.livesAfterDonationNone = messages.getString("lives-message-after-donation.none",
            "&cYou received a life from %donor%. You have no lives left. You are a spectator.");
        this.livesAfterBeingGenerousPlural = messages.getString("lives-message-after-being-generous.plural",
            "&aYou gave a life to %receiver%. You have %lives% lives left.");
        this.livesAfterBeingGenerousSingular = messages.getString("lives-message-after-being-generous.singular",
            "&aYou gave a life to %receiver%. You only have %lives% life left.");
        this.livesAfterBeingGenerousNone = messages.getString("lives-message-after-being-generous.none",
            "&cYou gave a life to %receiver%. You have no lives left. You are a spectator.");
        this.livesAfterLifestealPlural = messages.getString("lives-message-after-lifesteal.plural", "&aYou stole a life from %victim%! You have %lives% lives left.");
        this.livesAfterLifestealSingular = messages.getString("lives-message-after-lifesteal.singular", "&aYou stole a life from %victim%! You only have %lives% life left.");
        this.livesAfterLifestealNone = messages.getString("lives-message-after-lifesteal.none", "&aYou stole a life from %victim%! You have no lives left. You are a spectator.");
        this.revive = messages.getString("revive-message", "&aYou have been revived! You now have %lives% life left.");
        this.reviveTitle = messages.getString("revive-title", "&dYou are revived!");
        this.commandUnspecifiedPlayer = commandUnspecifiedPlayer();
        this.commandNotFound = commandNotFound();
        this.commandCannotRevive = commandCannotRevive();
        this.givelifeDisabled = messages.getString("givelife-command-errors.disabled", "&cGenerosity has been turned off.");
        this.givelifeSelfReference = messages.getString("givelife-command-errors.self-reference", "&cYou cannot give a life to yourself.");
        this.givelifeNoLivesLeft = messages.getString("givelife-command-errors.no-lives-left", "&cYou cannot give a life to anybody if you don't have any.");
        this.givelifeOneLifeLeft = messages.getString("givelife-command-errors.one-life-left",
            "&cYou only have one life left. If you gave a life to somebody at this point, you would immediately die.");
        this.givelifeCannotRevive = messages.getString("givelife-command-errors.cannot-revive", "&cYou can't give a life to a dead player.");
        this.givelifeLivesLimitHit = messages.getString("givelife-command-errors.lives-limit-hit",
            "&cThat player has reached the life limit and, as a result, you can't give them a life.");
        this.givelifeRevivalWarning = messages.getString("givelife-command-messages.revival-warning",
            "&cWarning! %donor% is about to sacrifice themselves so you get revived!");
        this.givelifeSacrificeConfirmation = messages.getString("givelife-command-messages.sacrifice-confirmation",
            "&c&lAre you sure you want to sacrifice your last life to revive %receiver%? Click this message to confirm your decision.");
        this.givelifeSacrificeHover = messages.getString("givelife-command-messages.sacrifice-hover", "&eClick here to sacrifice your last life");
        this.givelifeGiveawayConfirmation = messages.getString("givelife-command-messages.giveaway-confirmation",
            "&c&lAre you sure you want to give away your last life to %receiver%? Click this message to confirm your decision.");
        this.givelifeGiveawayHover = messages.getString("givelife-command-messages.giveaway-hover", "&eClick here to give your last life away");
        this.addlifeLivesLimitHit = messages.getString("addlife-command-messages.lives-limit-hit",
            "&cCould not add a life to %player% as they have reached the life limit.");
        this.addlifeSuccess = messages.getString("addlife-command-messages.success", "&aAdded a life to %player%.");
        this.setlivesUnspecifiedLives = messages.getString("setlives-command-messages.unspecified-lives", "&cYou need to specify a number of lives.");
        this.setlivesInvalidLives = messages.getString("setlives-command-messages.invalid-lives", "&cInvalid number of lives.");
        this.setlivesLimitedSuccess = messages.getString("setlives-command-messages.limited-success",
            "&aSet %player%'s lives to %limit% (your input, %lives%, is above the life limit).");
        this.setlivesSuccess = messages.getString("setlives-command-messages.success", "&aSet %player%'s lives to %lives%.");
        this.reviveAlivePlayer = messages.getString("revive-command-messages.alive-player", "&cThat player is alive.");
        this.reviveSuccess = messages.getString("revive-command-messages.success", "&aRevived %player%.");
    }

    /**
     * Saves the messages to the messages.yml file.
     *
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
        file.set("revive-message", this.revive);
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
        file.set("givelife-command-errors.disabled", this.givelifeDisabled);
        file.set("givelife-command-errors.self-reference", this.givelifeSelfReference);
        file.set("givelife-command-errors.no-lives-left", this.givelifeNoLivesLeft);
        file.set("givelife-command-errors.one-life-left", this.givelifeOneLifeLeft);
        file.set("givelife-command-errors.cannot-revive", this.givelifeCannotRevive);
        file.set("givelife-command-errors.lives-limit-hit", this.givelifeLivesLimitHit);
        file.set("givelife-command-messages.revival-warning", this.givelifeRevivalWarning);
        file.set("givelife-command-messages.sacrifice-confirmation", this.givelifeSacrificeConfirmation);
        file.set("givelife-command-messages.sacrifice-hover", this.givelifeSacrificeHover);
        file.set("givelife-command-messages.giveaway-confirmation", this.givelifeGiveawayConfirmation);
        file.set("givelife-command-messages.giveaway-hover", this.givelifeGiveawayHover);
        file.set("addlife-command-messages.lives-limit-hit", this.addlifeLivesLimitHit);
        file.set("addlife-command-messages.success", this.addlifeSuccess);
        file.set("setlives-command-messages.unspecified-lives", this.setlivesUnspecifiedLives);
        file.set("setlives-command-messages.invalid-lives", this.setlivesInvalidLives);
        file.set("setlives-command-messages.limited-success", this.setlivesLimitedSuccess);
        file.set("setlives-command-messages.success", this.setlivesSuccess);
        file.set("revive-command-messages.alive-player", this.reviveAlivePlayer);
        file.set("revive-command-messages.success", this.reviveSuccess);

    }

    private String commandUnspecifiedPlayer() {

        FileConfiguration messages = messagesFile.getConfig();
        if(messages.getString("givelife-command-errors.unspecified-player") != null) {

            return messages.getString("givelife-command-errors.unspecified-player");

        } else if(messages.getString("addlife-command-messages.unspecified-player") != null) {

            return messages.getString("addlife-command-messages.unspecified-player");

        } else if(messages.getString("setlives-command-messages.unspecified-player") != null) {

            return messages.getString("setlives-command-messages.unspecified-player");

        } else if(messages.getString("revive-command-messages.unspecified-player") != null) {

            return messages.getString("revive-command-messages.unspecified-player");

        } else
            return messages.getString("command-messages.unspecified-player", "&cYou need to specify a player.");

    }

    private String commandNotFound() {

        FileConfiguration messages = messagesFile.getConfig();
        if(messages.getString("givelife-command-errors.not-found") != null) {

            return messages.getString("givelife-command-errors.not-found");

        } else if(messages.getString("addlife-command-messages.not-found") != null) {

            return messages.getString("addlife-command-messages.not-found");

        } else if(messages.getString("setlives-command-messages.not-found") != null) {

            return messages.getString("setlives-command-messages.not-found");

        } else if(messages.getString("revive-command-messages.not-found") != null) {

            return messages.getString("revive-command-messages.not-found");

        } else
            return messages.getString("command-messages.not-found", "&cPlayer not found.");

    }

    private String commandCannotRevive() {

        FileConfiguration messages = messagesFile.getConfig();
        if(messages.getString("addlife-command-messages.cannot-revive") != null) {

            return messages.getString("addlife-command-messages.cannot-revive");

        } else if(messages.getString("setlives-command-messages.cannot-revive") != null) {

            return messages.getString("setlives-command-messages.cannot-revive");

        } else
            return messages.getString("command-messages.cannot-revive", "&cPlease use the /revive command to revive players.");

    }

}
