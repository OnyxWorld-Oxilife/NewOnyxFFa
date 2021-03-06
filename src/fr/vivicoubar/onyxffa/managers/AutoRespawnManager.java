package fr.vivicoubar.onyxffa.managers;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoRespawnManager {
    OnyxFFaMain main;

    public AutoRespawnManager(OnyxFFaMain main) {
        this.main = main;
    }

    public void askRespawn(FFaPlayer fFaPlayer) {
        if (fFaPlayer.isAutorespawnBoolean()) {
            fFaPlayer.getPlayer().closeInventory();
            fFaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 5));

            Location temp = new Location(fFaPlayer.getPlayer().getWorld(), 738, 48, 625);

            fFaPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            fFaPlayer.getPlayer().teleport(temp);

            new BukkitRunnable() {
                int timer = 3 ;
                @Override
                public void run() {

                    if(timer > 0){
                        fFaPlayer.getPlayer().sendTitle("§7Vous êtes §cMort!", "§7Respawn dans §c"  + timer + "...", 5 , 10 ,5);
                    } else if(timer == 0){
                        cancel();
                        Location spawn = new SpawnManager(main, main.getSpawnsList()).getSelectedSpawn();
                        fFaPlayer.getPlayer().teleport(spawn, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                        fFaPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                        fFaPlayer.getPlayer().getInventory().setHeldItemSlot(0);
                        fFaPlayer.getInventory().clear();
                        FileConfiguration kitConfiguration = main.getKitsConfiguration();
                        String rankname = fFaPlayer.getStats().getRank().getName();
                        for (String rankpath : main.getKitsConfiguration().getConfigurationSection("NewOnyxFFa.Ffa").getKeys(false)) {
                            if (rankname.equalsIgnoreCase(rankpath)) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Items").getKeys(false)) {
                                    String objectPath = "NewOnyxFFa.Ffa." + rankpath + ".Items." + path;
                                    ItemStack item = main.getItemBuilder().buildItem(objectPath);
                                    int slot = main.getKitsConfiguration().getInt(objectPath + ".Slot");
                                    switch (slot) {
                                        case 103:
                                            fFaPlayer.getPlayer().getInventory().setHelmet(item);
                                            break;
                                        case 102:
                                            fFaPlayer.getPlayer().getInventory().setChestplate(item);
                                            break;
                                        case 101:
                                            fFaPlayer.getPlayer().getInventory().setLeggings(item);
                                            break;
                                        case 100:
                                            fFaPlayer.getPlayer().getInventory().setBoots(item);
                                            break;
                                        default:
                                            fFaPlayer.getInventory().setItem(slot, item);
                                            break;
                                    }
                                }
                                break;
                            }
                        }
                    }

                timer--;
                }
            }
            .runTaskTimer(this.main, 0, 20L);
                }else

                {
                    FileConfiguration spawnConfiguration = main.getSpawnsConfiguration();
                    FileConfiguration configConfiguration = main.getConfigConfiguration();
                    Player player = fFaPlayer.getPlayer();
                    player.getInventory().setHeldItemSlot(4);
                    player.setGameMode(GameMode.ADVENTURE);
                    player.setFoodLevel(20);
                    player.getActivePotionEffects().clear();
                    //Téléportation au spawn

                    player.teleport(main.getLocationBuilder().getLocation("NewOnyxFFa.Spawns.Lobby"));
                    ItemStack menuSelector = new ItemStack(Material.getMaterial(configConfiguration.getString("NewOnyxFFa.Config.Menu.Item.Material")));
                    ItemMeta menuMeta = menuSelector.getItemMeta();
                    if ((boolean) configConfiguration.get("NewOnyxFFa.Config.Menu.Item.Enchanted")) {
                        menuMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                    }
                    menuMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
                    menuMeta.setDisplayName(configConfiguration.getString("NewOnyxFFa.Config.Menu.Item.Name"));
                    menuMeta.setLore(configConfiguration.getStringList("NewOnyxFFa.Config.Menu.Item.Lore"));
                    menuSelector.setItemMeta(menuMeta);
                    player.getInventory().setItem(4, menuSelector);
                }




    }
}
