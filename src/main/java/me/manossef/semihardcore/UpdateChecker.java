package me.manossef.semihardcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * An update checker for a resource from SpigotMC that is installed as a plugin on the server.
 */
public class UpdateChecker {

    private final Plugin plugin;
    private final int resourceId;
    private final String resourceWebName;

    /**
     * Constructs a new update checker for a resource.
     *
     * @param plugin          The resource in its current version.
     * @param resourceId      The numeric ID of the resource on SpigotMC.
     * @param resourceWebName The URL name of the resource on SpigotMC.
     */
    public UpdateChecker(Plugin plugin, int resourceId, String resourceWebName) {

        this.plugin = plugin;
        this.resourceId = resourceId;
        this.resourceWebName = resourceWebName;

    }

    /**
     * Checks the resource for updates.
     */
    public void checkForUpdates() {

        String currentVersion = plugin.getDescription().getVersion();
        if(!isStable(currentVersion)) {

            plugin.getLogger().log(Level.WARNING,
                "This server is running a development version of " + plugin.getName() + ", " + currentVersion + ".");
            plugin.getLogger().log(Level.WARNING, "Issues may arise; you have been warned.");
            return;

        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            try {

                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                InputStream stream = url.openStream();
                Scanner scan = new Scanner(stream);
                if(scan.hasNext()) {

                    String newVersion = scan.next();
                    scan.close();
                    int currentNumber = this.getReleaseVersionNumber(currentVersion);
                    int newNumber = this.getReleaseVersionNumber(newVersion);
                    if(currentNumber < newNumber) {

                        plugin.getLogger().log(Level.WARNING, "This server is running an older version of "
                            + plugin.getName() + ", " + currentVersion + ".");
                        plugin.getLogger().log(Level.WARNING, "There is a new version available " + newVersion + ".");
                        plugin.getLogger().log(Level.WARNING,
                            "You can download it here: https://www.spigotmc.org/resources/" + resourceWebName + "."
                                + resourceId + "/");

                    } else if(currentNumber > newNumber) {

                        plugin.getLogger().log(Level.INFO, "This server is running a soon to be released version of "
                            + plugin.getName() + ", " + currentVersion + ".");
                        plugin.getLogger().log(Level.INFO, "Last version I knew of was " + newVersion + ".");
                        plugin.getLogger().log(Level.INFO,
                            "Thank you developer, for enlightening me on your short term plans.");

                    }

                }

            } catch(IOException e) {

                plugin.getLogger().log(Level.WARNING, "Failed to check for updates for " + plugin.getName() + "!");

            }

        });

    }

    /**
     * Returns the resource's name.
     *
     * @return The resource's name.
     */
    public String getPluginName() {

        return this.plugin.getName();

    }

    private boolean isStable(String version) {

        String[] parts = version.split("\\.");
        if(parts.length != 3)
            return false;
        for(String part : parts) {

            if(!isInt(part))
                return false;

        }
        return true;

    }

    private int getReleaseVersionNumber(String version) {

        if(!isStable(version))
            return -1;
        String[] parts = version.split("\\.");
        int[] digits = new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
        return (10000 * digits[0]) + (100 * digits[1]) + (digits[2]);

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
