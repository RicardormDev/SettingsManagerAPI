package io.github.ricardormdev.paperserveranalytics;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

/**
 * Manager for spigot/bukkit configuration files.
 *
 * @author RichTheLord (RicardrmDev)
 * @author Someone else that I don't remember who was.
 * @version 3.0
 */
public class SettingsManager {

    private static final SettingsManager
            c = new SettingsManager("config");

    public static SettingsManager getConfig() {
        return c;
    }

    private File file;
    private FileConfiguration config;
    private String fileName;

    /**
     * Create a configuration file.
     *
     * @param fileName Name of the configuration file.
     */
    private SettingsManager(String fileName) {
        this.fileName = fileName;
        if (!PaperServerAnalytics.getInstance().getDataFolder().exists()) {
            if(!PaperServerAnalytics.getInstance().getDataFolder().mkdir()) {
                PaperServerAnalytics.getInstance().getLogger().severe("CONFIGURATION FILE NOT GENERATED.");
            }
        }

        file = new File(PaperServerAnalytics.getInstance().getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            try {
                if(!file.createNewFile())
                    PaperServerAnalytics.getInstance().getLogger().severe("CONFIGURATION FILE NOT GENERATED.");
                InputStream is = PaperServerAnalytics.class.getResourceAsStream("/" + fileName + ".yml");
                if (is != null) {
                    FileUtils.copyInputStreamToFile(is, file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Get something from the configuration file.
     *
     * @param path Path
     * @param <T>  Type of the object you need to return.
     * @return The object from the configuration.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    /**
     * Get the "bare" keys on the configuration.
     *
     * @return All the keys.
     */
    public Set<String> getKeys() {
        return config.getKeys(false);
    }

    /**
     * Set an object on the configuration.
     *
     * @param path  Where in the config will be configurated
     * @param value Object.
     */
    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    /**
     * Check if the configuration files contains that key.
     *
     * @param path
     * @return
     */
    public boolean contains(String path) {
        return config.contains(path);
    }

    /**
     * Create a section in the configuration.
     *
     * @param path Where does this needs to be stored.
     * @return The created section.
     */
    public ConfigurationSection createSection(String path) {
        ConfigurationSection section = config.createSection(path);
        save();
        return section;
    }

    /**
     * Reload the configuration.
     */
    public void reload() {
        if (!PaperServerAnalytics.getInstance().getDataFolder().exists()) {
            PaperServerAnalytics.getInstance().getDataFolder().mkdir();
        }

        file = new File(PaperServerAnalytics.getInstance().getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream is = PaperServerAnalytics.class.getResourceAsStream(fileName + ".yml");
                if (is != null) {
                    FileUtils.copyInputStreamToFile(is, file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Save the configuration.
     */
    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
