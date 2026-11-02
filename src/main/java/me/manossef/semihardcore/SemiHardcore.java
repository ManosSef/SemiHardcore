package me.manossef.semihardcore;

import me.manossef.alexandria.Alexandria;
import me.manossef.api.alexandria.TypeChecks;
import me.manossef.api.alexandria.commands.TabCompletions;
import me.manossef.api.alexandria.config.OptionValidations;
import me.manossef.api.alexandria.files.YamlFile;
import me.manossef.api.alexandria.updates.SpigotMCUpdateChecker;
import me.manossef.semihardcore.commands.*;
import me.manossef.semihardcore.data.LifeData;
import me.manossef.semihardcore.data.Messages;
import me.manossef.semihardcore.data.Options;
import me.manossef.semihardcore.data.Placeholders;
import me.manossef.semihardcore.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;

/**
 * This is the main class of SemiHardcore. This class has control over everything happening in the plugin, keeping track
 * of its options, commands, and listeners.
 * <p>
 * Every add-on for this plugin should first check if the plugin found on the server is an instance of this class, to
 * make sure the plugin is correct, and also so that the various resources contained in this class can be used.
 */
public class SemiHardcore extends JavaPlugin {
    private YamlFile livesFile;
    private YamlFile messagesFile;
    private LifeChanges lifeChanges;
    private Options options;
    private Messages messages;
    private LifeData lifeData;

    /**
     * Runs automatically when the plugin is enabled, and must never be called by any plugin add-ons.
     */
    @Override
    public void onEnable() {
        Plugin alexandria = this.getServer().getPluginManager().getPlugin("Alexandria");
        if(!(alexandria instanceof Alexandria parent)) {
            this.error("Found a plugin called \"Alexandria\" but it seems as though it is the wrong one. Please download Alexandria by ManosSef at https://www.spigotmc.org/resources/alexandria.140195/");
            return;
        }
        parent.checkVersion("1.0.0", this);
        try {
            new SpigotMCUpdateChecker(this, 95152, "semihardcore").checkForUpdates();
        } catch(MalformedURLException ignored) {
        }
        if(isUnsupportedVersion()) {
            this.error("SemiHardcore has dropped support for versions below 1.17. This server is running a Minecraft version below 1.17, so this plugin is being disabled.");
            return;
        }
        File deadPlayersFile = new File(this.getDataFolder().toPath() + "/deadPlayers.yml");
        if(deadPlayersFile.exists()) {
            this.getLogger().log(Level.INFO, "Found legacy deadPlayers.yml file. Deleting.");
            if(!deadPlayersFile.delete())
                this.getLogger().log(Level.WARNING, "Failed to delete deadPlayers.yml file.");
        }
        this.lifeChanges = new LifeChanges(this);
        if(!this.validateOptions()) return;
        this.options = new Options(this.getConfig().getInt("starting-lives.min", 3), this.getConfig().getInt("starting-lives.max", 3),
            this.getConfig().getBoolean("hardcore-hearts", false), this.getConfig().getBoolean("force-hard-difficulty", true),
            this.getConfig().getInt("lives-limit", 5), this.getConfig().getBoolean("players-can-be-generous", true),
            this.getConfig().getBoolean("lightning-strike-on-last-death", false), this.getConfig().getBoolean("sacrifices-for-revival", false),
            this.getConfig().getBoolean("players-can-give-away-last-life", false), this.getConfig().getBoolean("lifesteal.enabled", false),
            this.getConfig().getInt("lifesteal.max-lives-to-steal", 2), this.getConfig().getInt("lifesteal.min-lives-to-be-robbed", 4));
        this.options.logOptions(this.getLogger());
        if(((this.options.startingLivesMin() == 1 && this.options.startingLivesMax() == 1) || (this.options.livesLimit() == 1)) && !this.getServer().isHardcore())
            this.getLogger().log(Level.INFO,
                "Look, I don't want to sound mean, and I appreciate you liking my plugin this much, but why the heck would you set the starting lives or the life limit to 1? You can do that without this plugin, by turning on hardcore!");
        this.saveDefaultConfig();
        this.options.saveToFile(this.getConfig());
        this.saveConfig();
        this.livesFile = new YamlFile(this, "lives.yml");
        this.messagesFile = new YamlFile(this, "messages.yml");
        this.messages = new Messages(this.messagesFile);
        this.messagesFile.saveDefaultConfig();
        this.messages.saveToFile(this.messagesFile.getConfig());
        this.messagesFile.saveConfig();
        this.populateLifeData();
        if(this.options.forceHardDifficulty()) this.makeWorldsHard();
        this.makeWorldsHardcoreHearts(this.options.hardcoreHearts());
        this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
        this.registerCommands();
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.getLogger().log(Level.INFO, "PlaceholderAPI detected. Enabling placeholder expansion.");
            new Placeholders(this).register();
        }
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!this.lifeChanges.hasLives(player)) {
                this.lifeChanges.setLives(player, this.lifeChanges.getLivesForNewPlayer());
                this.lifeChanges.welcomeMessage(player);
            } else this.lifeChanges.livesMessage(player);
        });
    }

    /**
     * Runs automatically when the plugin is disabled, and must never be called by any plugin add-ons.
     */
    @Override
    public void onDisable() {
        if(isUnsupportedVersion())
            return;
        try {
            this.lifeData.saveToFile(this.livesFile.getConfig());
            this.livesFile.saveConfig();
        } catch(Exception ignored) {
        }
        this.makeWorldsHardcoreHearts(this.getServer().isHardcore());
    }

    /**
     * Returns the plugin's configuration options, as an {@link Options} object.
     * @return The plugin's options.
     */
    public Options getOptions() {
        return this.options;
    }

    /**
     * Returns the plugin's message configuration, as a {@link Messages} object.
     * @return The plugin's messages.
     */
    public Messages getMessages() {
        return this.messages;
    }

    /**
     * Returns the plugin's instance of {@link LifeChanges}, the class that is responsible for every change in any
     * player's life count or death status, as well as a lot of the plugin's messages.
     * @return The plugin's instance of {@link LifeChanges}.
     */
    public LifeChanges getLifeChanges() {
        return this.lifeChanges;
    }

    /**
     * Returns the lives.yml file as a {@link YamlFile}.
     * @return The lives.yml file data.
     */
    public YamlFile getLivesFile() {
        return this.livesFile;
    }

    /**
     * Returns the messages.yml file as a {@link YamlFile}.
     * @return The messages.yml file data.
     */
    public YamlFile getMessagesFile() {
        return this.messagesFile;
    }

    /**
     * Returns the internal life data.
     * @return The plugin's life data.
     */
    protected LifeData getLifeData() {
        return this.lifeData;
    }

    private boolean isUnsupportedVersion() {
        return !Arrays.stream(Material.values()).map(Material::toString).toList().contains("DRIPSTONE_BLOCK");
    }

    private void makeWorldsHardcoreHearts(boolean hasHardcoreHearts) {
        Bukkit.getWorlds().forEach(world -> world.setHardcore(hasHardcoreHearts));
    }

    private void makeWorldsHard() {
        Bukkit.getWorlds().forEach(world -> world.setDifficulty(Difficulty.HARD));
    }

    private boolean validateOptions() {
        if(!OptionValidations.checkMinMaxIntOption("starting-lives", 1, this)) return false;
        if(!OptionValidations.checkBooleanOption("hardcore-hearts", this)) return false;
        if(!OptionValidations.checkBooleanOption("force-hard-difficulty", this)) return false;
        if(!OptionValidations.checkIntOption("lives-limit", 0, this)) return false;
        if(this.getConfig().getInt("starting-lives.min", 3) > this.getConfig().getInt("lives-limit", 5)) {
            this.error("Found that \"starting-lives.min\" is greater than \"lives-limit\". Players can't start with a life count above the limit, so this plugin is being disabled.");
            return false;
        }
        if(this.getConfig().getInt("starting-lives.max", 3) > this.getConfig().getInt("lives-limit", 5)) {
            this.error("Found that \"starting-lives.max\" is greater than \"lives-limit\". Players can't start with a life count above the limit, so this plugin is being disabled.");
            return false;
        }
        if(!OptionValidations.checkBooleanOption("players-can-be-generous", this)) return false;
        if(!OptionValidations.checkBooleanOption("lightning-strike-on-last-death", this)) return false;
        if(!OptionValidations.checkBooleanOption("sacrifices-for-revival", this)) return false;
        if(!OptionValidations.checkBooleanOption("players-can-give-away-last-life", this)) return false;
        if(!OptionValidations.checkBooleanOption("lifesteal.enabled", this)) return false;
        if(!OptionValidations.checkIntOption("lifesteal.max-lives-to-steal", 1, this)) return false;
        if(this.getConfig().getInt("lifesteal.max-lives-to-steal", 2) > this.getConfig().getInt("lives-limit", 5)) {
            this.error("Found that \"lifesteal.max-lives-to-steal\" is greater than \"lives-limit\". Players can't have a life count above the limit, so this plugin is being disabled.");
            return false;
        }
        if(!OptionValidations.checkIntOption("lifesteal.min-lives-to-be-robbed", 1, this)) return false;
        if(this.getConfig().getInt("lifesteal.min-lives-to-be-robbed", 4) > this.getConfig().getInt("lives-limit", 5)) {
            this.error("Found that \"lifesteal.min-lives-to-be-robbed\" is greater than \"lives-limit\". Players can't have a life count above the limit, so this plugin is being disabled.");
            return false;
        }
        return true;
    }

    private void registerCommands() {
        setUpCommand(Objects.requireNonNull(this.getCommand("lives")), new LivesCommand(this), TabCompletions.none());
        setUpCommand(Objects.requireNonNull(this.getCommand("setlives")), new SetLivesCommand(this), TabCompletions.completers(TabCompletions.players(), TabCompletions.literals("1", "2", "3", "4", "5")));
        setUpCommand(Objects.requireNonNull(this.getCommand("addlife")), new AddLifeCommand(this), TabCompletions.playersAtFirst());
        setUpCommand(Objects.requireNonNull(this.getCommand("givelife")), new GiveLifeCommand(this), TabCompletions.playersAtFirst());
        setUpCommand(Objects.requireNonNull(this.getCommand("revive")), new ReviveCommand(this), TabCompletions.playersAtFirst());
    }

    private void setUpCommand(PluginCommand command, CommandExecutor executor, TabCompleter tabCompleter) {
        command.setExecutor(executor);
        command.setTabCompleter(tabCompleter);
    }

    private void populateLifeData() {
        Map<UUID, Integer> lives = new HashMap<>();
        ConfigurationSection section = this.livesFile.getConfig().getConfigurationSection("lives");
        if(section != null)
            section.getKeys(false).forEach(key -> {
                if(TypeChecks.isUUID(key))
                    lives.put(UUID.fromString(key), Math.max(section.getInt(key), 0));
            });
        this.lifeData = new LifeData(lives, this.options.livesLimit());
    }

    private void error(String message) {
        this.getLogger().severe(message);
        this.getServer().getPluginManager().disablePlugin(this);
    }
}