package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnManager {
    List<Location> spawns;
    HashMap<Location, Double> furthestSpawn;
    HashMap<Player, Double> nearestPlayer;
    Location selectedSpawn;
    OnyxFFaMain main;

    public SpawnManager(OnyxFFaMain main, List<Location> configSpawns){
        this.main = main;
        this.spawns = configSpawns;

        furthestSpawn = new HashMap<>();
        for(Location spawn : spawns){
            nearestPlayer = new HashMap<>();
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()){
                if(onlinePlayer.getGameMode() == GameMode.SURVIVAL) {
                    nearestPlayer.put(onlinePlayer,spawn.toVector().subtract(onlinePlayer.getLocation().toVector()).length());
                }else{
                    this.selectedSpawn = configSpawns.get(0);
                }
            }
            List<Map.Entry<Player, Double>> list = new ArrayList<>(nearestPlayer.entrySet());
            list.sort(Map.Entry.comparingByValue());
            if(list.size() != 0) {
                furthestSpawn.put(spawn, list.get(0).getValue());
            }
        }
        List<Map.Entry<Location, Double>> list = new ArrayList<>(furthestSpawn.entrySet());
        list.sort(Map.Entry.comparingByValue());
        if(list.size() != 0) {
            this.selectedSpawn = list.get(list.size() - 1).getKey();
        }
    }


    public Location getSelectedSpawn() {
        return selectedSpawn;
    }
}





