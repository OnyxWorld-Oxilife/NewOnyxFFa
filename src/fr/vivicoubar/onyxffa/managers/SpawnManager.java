package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnManager {
    private OnyxFFaMain main;

    public SpawnManager(OnyxFFaMain main) {
        this.main = main;
    }


    public Location getSelectedSpawn() {
        List<Location> spawns = main.getSpawnsList();
        HashMap<Location, Double> furthestSpawn;
        HashMap<Player, Double> nearestPlayer;
        Location selectedSpawn;

        furthestSpawn = new HashMap<>();
        for (Location spawn : spawns) {
            nearestPlayer = new HashMap<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getGameMode() == GameMode.SURVIVAL) {
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

    public void restoreArenaPlayer(Player p) {

        FFaPlayer ffaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, p);
        p.setGameMode(GameMode.SURVIVAL);
        ffaPlayer.setState(FFaPlayerStates.INVINCIBLE);

        AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(20);
        p.getInventory().setHeldItemSlot(0);
        p.getInventory().clear();
        FileConfiguration kitConfiguration = main.getKitsConfiguration();
        String rankname = ffaPlayer.getStats().getRank().getName();
        for (String rankpath : main.getKitsConfiguration().getConfigurationSection("NewOnyxFFa.Ffa").getKeys(false)) {
            if (rankname.equalsIgnoreCase(rankpath)) {
                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Items").getKeys(false)) {
                    String objectPath = "NewOnyxFFa.Ffa." + rankpath + ".Items." + path;
                    ItemStack item = main.getItemBuilder().buildItem(objectPath);
                    int slot = main.getKitsConfiguration().getInt(objectPath + ".Slot");
                    switch (slot) {
                        case 103:
                            p.getInventory().setHelmet(item);
                            break;
                        case 102:
                            p.getInventory().setChestplate(item);
                            break;
                        case 101:
                            p.getInventory().setLeggings(item);
                            break;
                        case 100:
                            p.getInventory().setBoots(item);
                            break;
                        default:
                            p.getInventory().setItem(slot, item);
                            break;
                    }
                }
                break;
            }
        }
        main.potionEffectManager.addPotionEffect(p, new PotionEffect(PotionEffectType.GLOWING, 60, 5));
        new BukkitRunnable(){
            @Override
            public void run() {
                ffaPlayer.setState(FFaPlayerStates.PLAYING);
            }
        }.runTaskLater(main,20*5);
    }

    public void respawnPlayer(Player p) {

        p.teleport(getSelectedSpawn(), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
        restoreArenaPlayer(p);

    }
}





