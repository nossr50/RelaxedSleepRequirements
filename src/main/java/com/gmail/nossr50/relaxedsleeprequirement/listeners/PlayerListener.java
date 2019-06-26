package com.gmail.nossr50.relaxedsleeprequirement.listeners;

import com.gmail.nossr50.relaxedsleeprequirement.RelaxedSleepRequirement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {
    private final RelaxedSleepRequirement pluginRef;

    public PlayerListener(RelaxedSleepRequirement pluginRef) {
        this.pluginRef = pluginRef; //Dependency injection
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        //Event fires before the player has entered the bed so schedule the check for 1 tick later
        pluginRef.getSleepManager().checkSleepDelayed(pluginRef, event.getPlayer().getWorld());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        //Player has died so we don't care if they are sleeping, so we start a sleep check
        pluginRef.getSleepManager().checkSleepDelayed(pluginRef, event.getEntity().getWorld());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        //Player is leaving a world, check that world for valid sleep conditions in 1 tick since that player is no longer part of its population
        pluginRef.getSleepManager().checkSleepDelayed(pluginRef, event.getFrom().getWorld());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        //If anyone gets out of a bed clear all sleep ignored players
        pluginRef.getSleepManager().clearSleepingIgnored();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Someone has quit, so the population of that world is reduced, start a sleep check
        pluginRef.getSleepManager().checkSleepDelayed(pluginRef, event.getPlayer().getWorld());
    }
}
