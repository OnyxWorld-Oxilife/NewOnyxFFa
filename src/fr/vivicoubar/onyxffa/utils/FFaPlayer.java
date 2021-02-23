package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.Map;


public class FFaPlayer {
    private final Player player;
    private final Inventory inventory;
    private Stats stats;
    private final OnyxFFaMain main;
    public FFaPlayer(OnyxFFaMain onyxFFaMain, Player player){
        this.main = onyxFFaMain;
        FileConfiguration statsConfiguration = main.getStatsConfiguration();
        if(!statsConfiguration.contains("NewOnyxFFa."+ player.getUniqueId())) {
            try {
            statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Kills", 0);
            statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Deaths", 0);
            statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Points", 0);
            statsConfiguration.set("NewOnyxFFa." + player.getUniqueId() + ".Pseudo", player.getName());
                statsConfiguration.save(main.getStatsFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.player = player;
        this.inventory = player.getInventory();
        this.stats = new Stats(this, main);
    }

    public Player getPlayer(){
        return this.player;
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

    public void setLocation(Location location){
        this.player.teleport(location);
    }

}
