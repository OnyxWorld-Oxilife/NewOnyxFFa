package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumoSpawnManager {
    private final OnyxFFaMain main;

    public SumoSpawnManager(OnyxFFaMain onyxFFaMain){
        this.main = onyxFFaMain;
    }

    public Location getSelectedSpawn() {
        List<Location> spawns = main.getSumoSpawnList();
        HashMap<Location, Double> furthestSpawn;
        HashMap<Player, Double> nearestPlayer;
        Location selectedSpawn;

        furthestSpawn = new HashMap<>();
        for (Location spawn : spawns) {
            nearestPlayer = new HashMap<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (main.getFFaPlayerManager().getFFaPlayer(main, onlinePlayer).getState() == FFaPlayerStates.SUMO) {
                    nearestPlayer.put(onlinePlayer, spawn.toVector().subtract(onlinePlayer.getLocation().toVector()).length());
                }
            }
            List<Map.Entry<Player, Double>> list = new ArrayList<>(nearestPlayer.entrySet());
            list.sort(Map.Entry.comparingByValue());
            if (list.size() != 0) {
                furthestSpawn.put(spawn, list.get(0).getValue());
            }
        }
        List<Map.Entry<Location, Double>> list = new ArrayList<>(furthestSpawn.entrySet());
        list.sort(Map.Entry.comparingByValue());
        if (list.size() != 0) {
            selectedSpawn = list.get(list.size() - 1).getKey();
        } else {
            selectedSpawn = spawns.get((int) Math.round(Math.random() * (spawns.size() - 1)));
        }
        return selectedSpawn;
    }

}
