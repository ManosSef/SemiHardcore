package me.manossef.semihardcore.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Holds the information for a single YML file of this plugin. This class can be used by add-ons to make YML files
 * specific to them.
 */
public class SemiHardcoreYmlFile {

    private final JavaPlugin plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private final String filename;

    /**
     * Constructs a new holder for a YML file. The YML file in question will be created in the provided plugin's data
     * folder if it does not yet exist, and it will contain the data found in the file with the same name in the
     * plugin's jar file, if that file is found.
     *
     * @param plugin   The plugin to give the YML file to.
     * @param filename The filename. This should end in <code>.yml</code>.
     */
    public SemiHardcoreYmlFile(JavaPlugin plugin, String filename) {

        this.plugin = plugin;
        this.filename = filename;
        saveDefaultConfig();

    }

    /**
     * Reloads the configuration from the YML file. SemiHardcore's YML files don't support this operation, but add-ons
     * can make their own files that do.
     */
    public void reloadConfig() {

        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), filename);
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource(filename);
        if(defaultStream != null) {

            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);

        }

    }

    /**
     * Returns the YML configuration of the file.
     *
     * @return The file configuration.
     */
    public FileConfiguration getConfig() {

        if(this.dataConfig == null) {

            reloadConfig();

        }
        return this.dataConfig;

    }

    /**
     * Saves the configuration to the YML file.
     */
    public void saveConfig() {

        if(this.dataConfig == null || this.configFile == null)
            return;
        try {

            this.getConfig().save(this.configFile);

        } catch(IOException e) {

            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);

        }

    }

    /**
     * Creates the file in the plugin's data folder if it does not exist. The created file will contain the data found
     * in the file with the same name in the plugin's jar file, if that file is found. If the file already exists, this
     * will not overwrite its data.
     */
    public void saveDefaultConfig() {

        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), filename);
        if(!this.configFile.exists())
            if(this.plugin.getResource(filename) != null)
                this.plugin.saveResource(filename, false);

    }

}
