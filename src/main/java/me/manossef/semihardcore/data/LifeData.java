package me.manossef.semihardcore.data;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.UUID;

/**
 * Stores the internal life data for the plugin. The plugin creates an instance of this class when it's enabled, but
 * does not allow any outside classes to access it to prevent fatal errors. Creating any new instances of this class has
 * no effect on the plugin. Therefore, this class should be of no interest to any add-ons.
 */
public class LifeData {

    private final Map<UUID, Integer> lives;
    private final int livesLimit;

    public LifeData(Map<UUID, Integer> lives, int livesLimit) {

        if(livesLimit < 0)
            throw new IllegalArgumentException("Lives limit cannot be negative");
        this.livesLimit = livesLimit;
        this.lives = lives;
        this.lives.forEach((uuid, count) -> this.lives.put(uuid, Math.max(0, livesLimit > 0 ? Math.min(count, livesLimit) : count)));

    }

    /**
     * Saves the life data to the lives.yml file.
     *
     * @param livesFile The lives.yml file configuration.
     */
    public void saveToFile(FileConfiguration livesFile) {

        lives.forEach((uuid, amount) -> livesFile.set("lives." + uuid.toString(), amount));

    }

    public int getLives(UUID uuid) {

        return this.lives.get(uuid);

    }

    public boolean hasLives(UUID uuid) {

        return this.lives.containsKey(uuid);

    }

    public int setLives(UUID uuid, int lives) {

        int newCount = Math.max(0, livesLimit > 0 ? Math.min(lives, livesLimit) : lives);
        this.lives.put(uuid, newCount);
        return newCount;

    }

}
