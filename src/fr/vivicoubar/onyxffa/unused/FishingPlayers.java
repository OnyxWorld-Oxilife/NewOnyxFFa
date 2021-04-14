package fr.vivicoubar.onyxffa.unused;

import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FishingPlayers {

    private static HashMap<Player, FishHook> fishingPlayers = new HashMap<>();

    public void addPlayer(Player player, FishHook fishHook) {
        if (!fishingPlayers.containsKey(player)) {
            fishingPlayers.put(player, fishHook);
        }
    }

    public void removePlayer(Player player) {
        if (fishingPlayers.containsKey(player)) {
            fishingPlayers.remove(player);
        }
    }

    public Boolean isHookSpawned(Player player) {
        return fishingPlayers.get(player).isValid();
    }

    public Boolean isFishing(Player player) {
        return fishingPlayers.containsKey(player);
    }

}
