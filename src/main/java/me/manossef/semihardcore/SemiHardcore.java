package me.manossef.semihardcore;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import me.manossef.semihardcore.commands.*;
import me.manossef.semihardcore.data.LifeData;
import me.manossef.semihardcore.data.Messages;
import me.manossef.semihardcore.data.Options;
import me.manossef.semihardcore.data.Placeholders;
import me.manossef.semihardcore.files.SemiHardcoreYmlFile;
import me.manossef.semihardcore.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * This is the main class of SemiHardcore. This class has control over everything happening in the plugin, keeping track
 * of its options, commands, and listeners.
 *
 * <p>
 * Every add-on for this plugin should first check if the plugin found on the server is an instance of this class, to
 * make sure the plugin is correct, and also so that the various resources contained in this class can be used.
 *
 * @author ManosSef
 */
public class SemiHardcore extends JavaPlugin {

    private SemiHardcoreYmlFile livesFile;
    private SemiHardcoreYmlFile messagesFile;
    private LifeChanges lifeChanges;
    private Options options;
    private Messages messages;
    private LifeData lifeData;
    private Map<String, UpdateChecker> addonsToBeChecked;

    /**
     * Runs automatically when the plugin is enabled, and must never be called by any plugin add-ons.
     */
    @Override
    public void onEnable() {

        this.checkUpdates();
        if(isUnsupportedVersion()) {

            error("SemiHardcore has dropped support for versions below 1.17. This server is running a Minecraft version below 1.17, so this plugin is being disabled.");
            return;

        }
        this.livesFile = new SemiHardcoreYmlFile(this, "lives.yml");
        this.messagesFile = new SemiHardcoreYmlFile(this, "messages.yml");
        File deadPlayersFile = new File(this.getDataFolder().toPath() + "/deadPlayers.yml");
        if(deadPlayersFile.exists()) {

            this.getLogger().log(Level.INFO, "Found legacy deadPlayers.yml file. Deleting.");
            if(!deadPlayersFile.delete())
                this.getLogger().log(Level.WARNING, "Failed to delete deadPlayers.yml file.");

        }
        this.lifeChanges = new LifeChanges(this);
        if(!this.validateOptions()) return;
        this.options = new Options(this.getConfig().getInt("starting-lives.min", 3), this.getConfig().getInt("starting-lives.max", 3),
            this.getConfig().getBoolean("hardcore-hearts", true), this.getConfig().getBoolean("force-hard-difficulty", true),
            this.getConfig().getInt("lives-limit", 5), this.getConfig().getBoolean("players-can-be-generous", true),
            this.getConfig().getBoolean("lightning-strike-on-last-death", false), this.getConfig().getBoolean("sacrifices-for-revival", false),
            this.getConfig().getBoolean("players-can-give-away-last-life", false), this.getConfig().getBoolean("lifesteal.enabled", false),
            this.getConfig().getInt("lifesteal.max-lives-to-steal", 2), this.getConfig().getInt("lifesteal.min-lives-to-be-robbed", 4));
        this.options.logOptions(this.getLogger());
        if(((this.options.startingLivesMin() == 1 && this.options.startingLivesMax() == 1) || (this.options.livesLimit() == 1)) && !this.getServer().isHardcore())
            this.getLogger().log(Level.INFO,
                "Look, I don't want to sound mean, and I appreciate you liking my plugin this much, but why the heck would you set the starting lives or the life limit to 1? You can do that without this plugin, by turning on hardcore!");
        this.messages = new Messages(this.messagesFile);
        this.populateLifeData();
        this.makeWorldsHard();
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

            this.options.saveToFile(this.getConfig());
            this.saveConfig();
            this.messages.saveToFile(this.messagesFile.getConfig());
            this.messagesFile.saveConfig();
            this.lifeData.saveToFile(this.livesFile.getConfig());
            this.livesFile.saveConfig();

        } catch(Exception ignored) {
        }
        this.makeWorldsHardcoreHearts(false);

    }

    /**
     * Returns whether the Minecraft version the server is running is unsupported by the plugin. If the plugin has been successfully enabled, this can only return {@code false}.
     *
     * @return {@code false}.
     */
    public static boolean isUnsupportedVersion() {

        return !Arrays.stream(Material.values()).map(Material::toString).toList().contains("DRIPSTONE_BLOCK");

    }

    private void makeWorldsHardcoreHearts(boolean hasHardcoreHearts) {

        Bukkit.getWorlds().forEach(world -> world.setHardcore(hasHardcoreHearts));

    }

    private void makeWorldsHard() {

        if(!this.getConfig().getBoolean("force-hard-difficulty"))
            return;
        Bukkit.getWorlds().forEach(world -> world.setDifficulty(Difficulty.HARD));

    }

    private boolean validateOptions() {

        if(checkIntOption("starting-lives.min", 1)) return false;
        if(checkIntOption("starting-lives.max", 1)) return false;
        if(this.getConfig().getInt("starting-lives.min", 3) > this.getConfig().getInt("starting-lives.max", 3)) {

            error("Found that \"starting-lives.min\" is greater than \"starting-lives.max\". The minimum value can't be greater than the maximum value, so this plugin is being disabled.");
            return false;

        }
        if(checkBooleanOption("hardcore-hearts")) return false;
        if(checkBooleanOption("force-hard-difficulty")) return false;
        if(checkIntOption("lives-limit", 0)) return false;
        if(this.getConfig().getInt("starting-lives.min", 3) > this.getConfig().getInt("lives-limit", 5)) {

            error("Found that \"starting-lives.min\" is greater than \"lives-limit\". Players can't start with a life count above the limit, so this plugin is being disabled.");
            return false;

        }
        if(this.getConfig().getInt("starting-lives.max", 3) > this.getConfig().getInt("lives-limit", 5)) {

            error("Found that \"starting-lives.max\" is greater than \"lives-limit\". Players can't start with a life count above the limit, so this plugin is being disabled.");
            return false;

        }
        if(checkBooleanOption("players-can-be-generous")) return false;
        if(checkBooleanOption("lightning-strike-on-last-death")) return false;
        if(checkBooleanOption("sacrifices-for-revival")) return false;
        if(checkBooleanOption("players-can-give-away-last-life")) return false;
        if(checkBooleanOption("lifesteal.enabled")) return false;
        if(checkIntOption("lifesteal.max-lives-to-steal", 1)) return false;
        if(this.getConfig().getInt("lifesteal.max-lives-to-steal", 2) > this.getConfig().getInt("lives-limit", 5)) {

            error("Found that \"lifesteal.max-lives-to-steal\" is greater than \"lives-limit\". Players can't have a life count above the limit, so this plugin is being disabled.");
            return false;

        }
        if(checkIntOption("lifesteal.min-lives-to-be-robbed", 1)) return false;
        if(this.getConfig().getInt("lifesteal.min-lives-to-be-robbed", 4) > this.getConfig().getInt("lives-limit", 5)) {

            error("Found that \"lifesteal.min-lives-to-be-robbed\" is greater than \"lives-limit\". Players can't have a life count above the limit, so this plugin is being disabled.");
            return false;

        }
        return true;

    }

    private boolean checkIntOption(String path, int min) {

        Object value = this.getConfig().get(path);
        if(value != null) {

            if(!isInt(value.toString())) {

                error("Found that \"" + path + "\" has been set to a value that is not a number. This plugin is being disabled.");
                return true;

            }
            if(this.getConfig().getInt(path) < min) {

                error("Found that \"" + path + "\" has been set to a value that is less than " + min + ". This plugin is being disabled.");
                return true;

            }

        }
        return false;

    }

    private boolean checkBooleanOption(String path) {

        Object value = this.getConfig().get(path);
        if(value != null)
            if(!isBoolean(value.toString())) {

                error("Found that \"" + path + "\" has been set to a value that is not true or false. This plugin is being disabled.");
                return true;

            }
        return false;

    }

    private void registerCommands() {

        PluginCommand livesCommand = Objects.requireNonNull(this.getCommand("lives"));
        TabExecutor livesExecutor = new LivesCommand(this);
        livesCommand.setExecutor(livesExecutor);
        livesCommand.setTabCompleter(livesExecutor);
        PluginCommand setLivesCommand = Objects.requireNonNull(this.getCommand("setlives"));
        TabExecutor setLivesExecutor = new SetLivesCommand(this);
        setLivesCommand.setExecutor(setLivesExecutor);
        setLivesCommand.setTabCompleter(setLivesExecutor);
        PluginCommand addLifeCommand = Objects.requireNonNull(this.getCommand("addlife"));
        TabExecutor addLifeExecutor = new AddLifeCommand(this);
        addLifeCommand.setExecutor(addLifeExecutor);
        addLifeCommand.setTabCompleter(addLifeExecutor);
        PluginCommand giveLifeCommand = Objects.requireNonNull(this.getCommand("givelife"));
        TabExecutor giveLifeExecutor = new GiveLifeCommand(this);
        giveLifeCommand.setExecutor(giveLifeExecutor);
        giveLifeCommand.setTabCompleter(giveLifeExecutor);
        PluginCommand reviveCommand = Objects.requireNonNull(this.getCommand("revive"));
        TabExecutor reviveExecutor = new ReviveCommand(this);
        reviveCommand.setExecutor(reviveExecutor);
        reviveCommand.setTabCompleter(reviveExecutor);

    }

    private void populateLifeData() {

        Map<UUID, Integer> lives = new HashMap<>();
        ConfigurationSection section = livesFile.getConfig().getConfigurationSection("lives");
        if(section != null)
            section.getKeys(false).forEach(key -> {

                if(isUUID(key))
                    lives.put(UUID.fromString(key), Math.max(section.getInt(key), 0));

            });
        this.lifeData = new LifeData(lives, this.options.livesLimit());

    }

    /**
     * Returns the official list of add-ons, retrieved from <a href=
     * "https://raw.githubusercontent.com/ManosSef/SemiHardcoreAddonList/main/addons.json">this</a> GitHub page, in the
     * form of a {@code JsonReader}.
     *
     * @return The official add-on list.
     */
    public JsonReader getOfficialAddonList() {

        try {

            URL addonList = new URL("https://raw.githubusercontent.com/ManosSef/SemiHardcoreAddonList/main/addons.json");
            InputStream stream = addonList.openStream();
            Scanner scan = new Scanner(stream);
            StringBuilder jsonString = new StringBuilder();
            while(scan.hasNextLine()) {

                jsonString.append(scan.nextLine());

            }
            scan.close();
            Gson gson = new Gson();
            return gson.newJsonReader(new StringReader(jsonString.toString()));

        } catch(IOException e) {

            this.getLogger().log(Level.WARNING, "Failed to retrieve official add-on list!");
            return null;

        }

    }

    private void checkUpdates() {

        try {

            UpdateChecker baseUpdateChecker = new UpdateChecker(this, 95152, "semihardcore");
            baseUpdateChecker.checkForUpdates();
            JsonReader json = this.getOfficialAddonList();
            this.addonsToBeChecked = new HashMap<>();
            json.beginArray();
            while(json.hasNext()) {

                UpdateChecker optional = this.getCheckerForAddon(json);
                if(optional != null) {

                    this.addonsToBeChecked.put(optional.getPluginName(), optional);

                }

            }
            json.endArray();

        } catch(Exception e) {

            this.getLogger().log(Level.WARNING, "Failed to check SemiHardcore and its add-ons for updates!");

        }

    }

    private UpdateChecker getCheckerForAddon(JsonReader json) throws IOException {

        String name = null;
        int resourceId = -1;
        String resourceWebName = null;
        json.beginObject();
        while(json.hasNext()) {

            String tag = json.nextName();
            switch(tag) {

                case "name" -> name = json.nextString();
                case "resource_id" -> resourceId = json.nextInt();
                case "resource_web_name" -> resourceWebName = json.nextString();
                default -> json.skipValue();

            }

        }
        json.endObject();
        if(name == null)
            return null;
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if(plugin != null)
            return new UpdateChecker(plugin, resourceId, resourceWebName);
        else
            return null;

    }

    /**
     * Checks if the given add-on is yet to be checked for updates.
     *
     * @param name The add-on's name.
     * @return {@code true} if the add-on is yet to be checked, {@code false} otherwise.
     */
    public boolean isAddonToBeChecked(String name) {

        return this.addonsToBeChecked.containsKey(name);

    }

    /**
     * Checks the given add-on for updates.
     *
     * @param name The add-on's name.
     */
    public void checkAddonForUpdates(String name) {

        if(isAddonToBeChecked(name)) {

            this.addonsToBeChecked.get(name).checkForUpdates();

        }
        this.addonsToBeChecked.remove(name);

    }

    /**
     * Returns the plugin's configuration options, as an {@link Options} object.
     *
     * @return The plugin's options.
     */
    public Options getOptions() {

        return options;

    }

    /**
     * Returns the plugin's message configuration, as a {@link Messages} object.
     *
     * @return The plugin's messages.
     */
    public Messages getMessages() {

        return messages;

    }

    /**
     * Returns the internal life data.
     *
     * @return The plugin's life data.
     */
    protected LifeData getLifeData() {

        return lifeData;

    }

    /**
     * Returns the plugin's instance of {@link LifeChanges}, the class that is responsible for every change in any
     * player's life count or death status, as well as a lot of the plugin's messages.
     *
     * @return The plugin's instance of {@link LifeChanges}.
     */
    public LifeChanges getLifeChanges() {

        return lifeChanges;

    }

    /**
     * Returns the lives.yml file as a {@link SemiHardcoreYmlFile}.
     *
     * @return The lives.yml file data.
     */
    public SemiHardcoreYmlFile getLivesFile() {

        return livesFile;

    }

    /**
     * Returns the deadPlayers.yml file as a {@link SemiHardcoreYmlFile}.
     *
     * @return The deadPlayers.yml file data.
     * @deprecated The deadPlayers.yml file has been removed.
     */
    @Deprecated(since = "2.7.0", forRemoval = true)
    public SemiHardcoreYmlFile getDeadPlayersFile() {

        throw new UnsupportedOperationException("The deadPlayers.yml file has been removed");

    }

    /**
     * Returns the messages.yml file as a {@link SemiHardcoreYmlFile}.
     *
     * @return The messages.yml file data.
     */
    public SemiHardcoreYmlFile getMessagesFile() {

        return messagesFile;

    }

    private void error(String message) {

        this.getLogger().log(Level.SEVERE, message);
        this.getServer().getPluginManager().disablePlugin(this);

    }

    private boolean isInt(String input) {

        try {

            Integer.parseInt(input);
            return true;

        } catch(Exception e) {

            return false;

        }

    }

    private boolean isUUID(String input) {

        try {

            UUID.fromString(input);
            return true;

        } catch(Exception e) {

            return false;

        }

    }

    private boolean isBoolean(String input) {

        return (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false"));

    }

}
