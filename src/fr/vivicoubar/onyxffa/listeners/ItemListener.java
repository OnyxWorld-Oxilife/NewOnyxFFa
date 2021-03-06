package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.managers.SpawnManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemListener implements Listener {
    private OnyxFFaMain main;
    private List<String> description = new ArrayList<>();

    public ItemListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @EventHandler
    private void onClickFFaItem(PlayerInteractEvent interactEvent) {
        if (interactEvent.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK && interactEvent.getAction() != Action.RIGHT_CLICK_AIR) {
                return;
            }
            if (interactEvent.getItem() == null) {
                return;
            }
            if (interactEvent.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(main.getConfigConfiguration().getString("NewOnyxFFa.Config.Menu.Item.Name"))) {
                FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(this.main, interactEvent.getPlayer());
                fFaPlayer.getPlayer().closeInventory();
                fFaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 5));

                Location spawn = new SpawnManager(main, main.getSpawnsList()).getSelectedSpawn();
                fFaPlayer.getPlayer().teleport(spawn);
                fFaPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                fFaPlayer.getPlayer().getInventory().setHeldItemSlot(0);
                fFaPlayer.getInventory().clear();

                String rankname = fFaPlayer.getStats().getRank().getName();
                FileConfiguration kitConfiguration = main.getKitsConfiguration();
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
                fFaPlayer.getPlayer().updateInventory();

            }
        }
    }
}
