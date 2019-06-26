package com.gmail.nossr50.relaxedsleeprequirement;

import com.gmail.nossr50.relaxedsleeprequirement.listeners.PlayerListener;
import com.gmail.nossr50.relaxedsleeprequirement.utils.SleepManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RelaxedSleepRequirement extends JavaPlugin {

    //Handles sleep operations
    private SleepManager sleepManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        sleepManager = new SleepManager(); //Init our sleep manager
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SleepManager getSleepManager() {
        return sleepManager;
    }
}
