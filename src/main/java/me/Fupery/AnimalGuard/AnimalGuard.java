package me.Fupery.AnimalGuard;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AnimalGuard extends JavaPlugin {

    private static String denyMessage;

    public static AnimalGuard plugin() {
        return (AnimalGuard) Bukkit.getPluginManager().getPlugin("AnimalGuard");
    }

    public static String getDenyMessage() {
        return denyMessage;
    }

    @Override
    public void onEnable() {
        if (WGBukkit.getPlugin() == null) {
            getLogger().warning("WorldGuard cannot be found, AnimalGuard will be disabled.");
            //todo hardcoding
            getPluginLoader().disablePlugin(this);
            return;
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        FileConfiguration config = getConfig();
        for (EventListener.EventAction action : EventListener.EventAction.values()) {
            action.loadProtections(config);
        }
        denyMessage = config.getString("deny-message");
        getServer().getPluginManager().registerEvents(new PlayerInteractManager(), this);
    }

    @Override
    public void onDisable() {
    }
}
