package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.managers.AutoRespawnManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.UUID;


public class FFaPlayer {
    private final Player player;
    private final Inventory inventory;
    private Stats stats;
    private boolean autorespawnBoolean = true;
    private final OnyxFFaMain main;
    private AutoRespawnManager autoRespawnManager;
    private UUID uniqueID;

    public FFaPlayer(OnyxFFaMain onyxFFaMain, Player player){
        this.main = onyxFFaMain;
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        if(!statsConfiguration.contains("NewOnyxFFa."+ player.getUniqueId())) {
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

    public Player getPlayer(){
        return this.player;
    }
    public UUID getUniqueID() {
        return uniqueID;
    }
    public Inventory getInventory(){
        return this.player.getInventory();
    }
    public Stats getStats(){
        return this.stats;
    }
    public void updateStats(){
        this.stats = new Stats(this, main);
    }
    public void setHealth(double health){
        this.player.setHealth(health);
    }
    public boolean isAutorespawnBoolean() {
        return autorespawnBoolean;
    }
    public AutoRespawnManager getAutoRespawnManager() {
        return autoRespawnManager;
    }
    public void setLocation(Location location){
        this.player.teleport(location);
    }
    public void setAutorespawnBoolean(){
        this.autorespawnBoolean = !autorespawnBoolean;
    }

}
