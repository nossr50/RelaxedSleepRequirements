package com.gmail.nossr50.relaxedsleeprequirement.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;

public class SleepManager {

    //Tracks players who are sleep ignored
    private HashSet<Player> sleepIgnoredPlayers;

    public SleepManager() {
        //Init hash set
        sleepIgnoredPlayers = new HashSet<>();
    }

    /**
     * Checks to see if it is okay to start an early sleep
     * If it is okay, it then starts the early sleep
     * @param world target world
     */
    private void checkSleep(World world) {
        /*
         * Only count what we consider to be 'valid' players
         * Valid players are alive
         * Valid players are in this world
         */
        double totalValidPlayers = 0;
        double playersTryingToSleep = 0;

        for(Player player : world.getPlayers()) {
            //Add this player to the total valid player count
            if(!player.isDead()) {
                totalValidPlayers++;
            }

            //If the player is sleeping, they are contributing towards our goal
            if(player.isSleeping()) {
                playersTryingToSleep++;
            }
        }
        //As long as at least half the population is trying to sleep we can start the sleep ritual
        int halfPopulation = (int) Math.max(1.0, (totalValidPlayers / 2.0)); //Round down by casting to int and require 1 player to be sleeping

        Bukkit.broadcastMessage("Total Valid Players: "+totalValidPlayers);
        Bukkit.broadcastMessage("Players Trying to Sleep: "+playersTryingToSleep);
        Bukkit.broadcastMessage("Players required to start Sleep: "+halfPopulation);

        //If the requirement has been met, start the sleep cycle thing
        if(playersTryingToSleep >= halfPopulation) {
            Bukkit.broadcastMessage("Sleep requirements met for world: "+world.getName());
            startSleep(world);
        }
    }

    /**
     * Mark everyone in the world as sleep ignored to get the ball rolling
     * @param world target world
     */
    private void startSleep(World world) {
        for(Player player : world.getPlayers()) {
            addSleepIgnored(player);
        }
    }

    /**
     * Add and track sleep ignored players
     * @param player player to add and track for sleep ignore
     */
    private void addSleepIgnored(Player player) {
        sleepIgnoredPlayers.add(player);
        player.setSleepingIgnored(true);
    }

    /**
     * Clear the sleep ignored players
     * Trigger this after time has changed
     */
    public void clearSleepingIgnored() {
        for(Player player : sleepIgnoredPlayers) {
            if(player != null && player.isValid() && player.isOnline())
                player.setSleepingIgnored(false);
        }

        sleepIgnoredPlayers.clear();
    }

    /**
     * Schedules a check 1 tick later to see if its safe to start an early sleep
     * @param plugin our plugin
     * @param world target world
     */
    public void checkSleepDelayed(Plugin plugin, World world) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> checkSleep(world), 1);
    }
}
