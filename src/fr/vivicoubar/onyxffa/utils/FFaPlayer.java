package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.managers.AutoRespawnManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.UUID;

// Player Object for the whole plugin
public class FFaPlayer {
    private final Player player;
    private final Inventory inventory;
    private Stats stats;
    private FFaPlayerStates state = FFaPlayerStates.WAITING;
    private boolean autorespawnBoolean = true;
    private final OnyxFFaMain main;
    private String lasthitter ="";
    public long timeWhenLastHitted = 0;
    private final AutoRespawnManager autoRespawnManager;
    private final UUID uniqueID;

    public FFaPlayer(OnyxFFaMain onyxFFaMain, Player player) {
        this.main = onyxFFaMain;
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        if (!statsConfiguration.contains("NewOnyxFFa." + player.getUniqueId())) {
            try {
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Kills", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Deaths", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Points", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".HighestScore", 0);
                statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Pseudo", player.getName());
                statsConfiguration.save(main.getStatsFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.uniqueID = player.getUniqueId();
        this.autoRespawnManager = new AutoRespawnManager(this.main);
        this.player = player;
        this.inventory = player.getInventory();
        this.stats = new Stats(this, main);
        this.main.getfFaPlayerManager().getfFaPlayerList().add(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public Inventory getInventory() {
        return this.player.getInventory();
    }

    public Stats getStats() {
        return this.stats;
    }

    public void updateStats() {
        this.stats = new Stats(this, main);
    }

    public void setState(FFaPlayerStates state) {
        this.state = state;
    }
    public FFaPlayerStates getState() {
        return state;
    }
    public boolean isPlaying(){
        return state == FFaPlayerStates.PLAYING;
    }
    public void setHealth(double health) {
        this.player.setHealth(health);
    }

    public boolean isAutorespawnBoolean() {
        return autorespawnBoolean;
    }

    public String getLasthitter() {
        return lasthitter;
    }

    public long getTimeWhenLastHitted() {
        return timeWhenLastHitted;
    }
    public void setTimeWhenLastHitted(long time){
        this.timeWhenLastHitted = time;
    }
    public void setLasthitter(String lasthitter) {
        this.lasthitter = lasthitter;
    }

    public AutoRespawnManager getAutoRespawnManager() {
        return autoRespawnManager;
    }

    public void setLocation(Location location) {
        this.player.teleport(location);
    }

    public void setAutorespawnBoolean() {
        this.autorespawnBoolean = !autorespawnBoolean;
    }

}
