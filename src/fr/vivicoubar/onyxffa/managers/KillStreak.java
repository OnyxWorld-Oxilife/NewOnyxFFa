package fr.vivicoubar.onyxffa.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class KillStreak {

    private static HashMap<Player, Integer> killStreaks = new HashMap<Player, Integer>();

    /* public HashMap getHashMap() {
        return killStreaks;
    }*/

    public void addPlayer(Player player) {
        if (!killStreaks.containsKey(player)) {
            killStreaks.put(player, 0);
        }
    }

    public void removePlayer(Player player) {
        if (killStreaks.containsKey(player)) {
            killStreaks.remove(player);
        }
    }

    public void incrementPlayer(Player player) {
        if (killStreaks.containsKey(player)) {
            killStreaks.replace(player, killStreaks.get(player).intValue() + 1);
        }
    }

    public void resetPlayer(Player player) {
        if (killStreaks.containsKey(player)) {
            killStreaks.replace(player, 0);
        }
    }

    public Integer getValue(Player player) {
        if (killStreaks.containsKey(player)) {
            return killStreaks.get(player).intValue();
        }
        return 0;
    }

}
