package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemListener implements Listener {
    private OnyxFFaMain main;
    private List<String> description = new ArrayList<>();
    public ItemListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }
    @EventHandler
    private void onClickFFaItem(PlayerInteractEvent interactEvent) {
        if (interactEvent.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            if(interactEvent.getItem() == null){
                return;
            }
            if (interactEvent.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(main.getConfigConfiguration().getString("NewOnyxFFa.Config.Menu.Item.Name"))) {
                FFaPlayer fFaPlayer = new FFaPlayer(this.main, interactEvent.getPlayer());
                fFaPlayer.getPlayer().closeInventory();
                fFaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 5));
                int spawn = (int) (Math.random() * (main.getSpawnsList().size() - 1));
                main.getSpawnsList().get(spawn).setWorld(interactEvent.getPlayer().getWorld());
                fFaPlayer.getPlayer().teleport(main.getSpawnsList().get(spawn), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                fFaPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                fFaPlayer.getPlayer().getInventory().setHeldItemSlot(0);
                fFaPlayer.getInventory().clear();
                String rankname = fFaPlayer.getStats().getRank().getName();
                for (String rankpath : main.getKitsConfiguration().getConfigurationSection("NewOnyxFFa.Ffa").getKeys(false)) {
                    if (rankname.equalsIgnoreCase(rankpath)) {
                        FileConfiguration kitConfiguration = main.getKitsConfiguration();
                        if (kitConfiguration.getString("NewOnyxFFa.Ffa." + rankpath + ".Armor.Material").equalsIgnoreCase("ColoredLeather")) {
                            int red = kitConfiguration.getInt("NewOnyxFFa.Ffa." + rankpath + ".Armor.Color.R");
                            int green = kitConfiguration.getInt("NewOnyxFFa.Ffa." + rankpath + ".Armor.Color.G");
                            int blue = kitConfiguration.getInt("NewOnyxFFa.Ffa." + rankpath + ".Armor.Color.B");
                            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
                            LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
                            helmetMeta.setColor(Color.fromRGB(red,green,blue));
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Helmet.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Helmet.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Helmet.Enchantements." + path;
                                    helmetMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            helmetMeta.setUnbreakable(true);
                            helmet.setItemMeta(helmetMeta);
                            fFaPlayer.getPlayer().getInventory().setHelmet(helmet);
                            ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                            LeatherArmorMeta chestPlateMeta = (LeatherArmorMeta) chestPlate.getItemMeta();
                            chestPlateMeta.setColor(Color.fromRGB(red,green,blue));
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Chestplate.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Chestplate.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Chestplate.Enchantements." + path;
                                    chestPlateMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            chestPlateMeta.setUnbreakable(true);
                            chestPlate.setItemMeta(chestPlateMeta);
                            fFaPlayer.getPlayer().getInventory().setChestplate(chestPlate);
                            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                            LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
                            leggingsMeta.setColor(Color.fromRGB(red,green,blue));
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Leggings.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Leggings.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Leggings.Enchantements." + path;
                                    leggingsMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            leggingsMeta.setUnbreakable(true);
                            leggings.setItemMeta(leggingsMeta);
                            fFaPlayer.getPlayer().getInventory().setLeggings(leggings);
                            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
                            LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
                            bootsMeta.setColor(Color.fromRGB(red,green,blue));
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Boots.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Boots.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Boots.Enchantements." + path;
                                    bootsMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            bootsMeta.setUnbreakable(true);
                            boots.setItemMeta(bootsMeta);
                            fFaPlayer.getPlayer().getInventory().setBoots(boots);
                        } else {
                            ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                            ItemStack chestPlate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                            ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                            ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
                            if (kitConfiguration.getString("NewOnyxFFa.Ffa." + rankpath + ".Armor.Material").equalsIgnoreCase("Diamond")) {
                                helmet = new ItemStack(Material.DIAMOND_HELMET);
                                chestPlate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                                leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                                boots = new ItemStack(Material.DIAMOND_BOOTS);
                            } else if (kitConfiguration.getString("NewOnyxFFa.Ffa." + rankpath + ".Armor.Material").equalsIgnoreCase("Gold")) {
                                helmet = new ItemStack(Material.GOLD_HELMET);
                                chestPlate = new ItemStack(Material.GOLD_CHESTPLATE);
                                leggings = new ItemStack(Material.GOLD_LEGGINGS);
                                boots = new ItemStack(Material.GOLD_BOOTS);
                            } else if (kitConfiguration.getString("NewOnyxFFa.Ffa." + rankpath + ".Armor.Material").equalsIgnoreCase("Iron")) {
                                helmet = new ItemStack(Material.IRON_HELMET);
                                chestPlate = new ItemStack(Material.IRON_CHESTPLATE);
                                leggings = new ItemStack(Material.IRON_LEGGINGS);
                                boots = new ItemStack(Material.IRON_BOOTS);
                            } else if (kitConfiguration.getString("NewOnyxFFa.Ffa." + rankpath + ".Armor.Material").equalsIgnoreCase("Leather")) {
                                helmet = new ItemStack(Material.LEATHER_HELMET);
                                chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                                leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                                boots = new ItemStack(Material.LEATHER_BOOTS);
                            } else {
                                helmet = new ItemStack(Material.LEATHER_HELMET);
                                chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                                leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                                boots = new ItemStack(Material.LEATHER_BOOTS);
                            }
                            ItemMeta helmetMeta = helmet.getItemMeta();
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Helmet.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Helmet.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Helmet.Enchantements." + path;
                                    helmetMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            helmet.setItemMeta(helmetMeta);
                            fFaPlayer.getPlayer().getInventory().setHelmet(helmet);
                            ItemMeta chestPlateMeta = chestPlate.getItemMeta();
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Chestplate.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Chestplate.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Chestplate.Enchantements." + path;
                                    chestPlateMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            chestPlate.setItemMeta(chestPlateMeta);
                            fFaPlayer.getPlayer().getInventory().setChestplate(chestPlate);
                            ItemMeta leggingsMeta = leggings.getItemMeta();
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Leggings.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Leggings.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Leggings.Enchantements." + path;
                                    leggingsMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), true);
                                }
                            }
                            leggings.setItemMeta(leggingsMeta);
                            fFaPlayer.getPlayer().getInventory().setLeggings(leggings);
                            ItemMeta bootsMeta = boots.getItemMeta();
                            if (kitConfiguration.getBoolean("NewOnyxFFa.Ffa." + rankpath + ".Armor.Boots.Enchanted")) {
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Armor.Boots.Enchantements").getKeys(false)) {
                                    String enchantPath = "NewOnyxFFa.Ffa." + rankpath + ".Armor.Boots.Enchantements." + path;
                                    bootsMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), false);
                                }
                            }
                            boots.setItemMeta(bootsMeta);
                            fFaPlayer.getPlayer().getInventory().setBoots(boots);
                        }
                        for(String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Items").getKeys(false)) {
                            String objectPath = "NewOnyxFFa.Ffa." + rankpath + ".Items." + path;
                            ItemStack itemStack = new ItemStack(Material.getMaterial(kitConfiguration.getString(objectPath + ".Material")),kitConfiguration.getInt(objectPath + ".Quantity"));
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if(kitConfiguration.getBoolean(objectPath + ".Enchanted")){
                                for(String enchant : kitConfiguration.getConfigurationSection(objectPath + ".Enchantments").getKeys(false)){
                                    String enchantPath = objectPath + ".Enchantments." + enchant ;
                                    itemMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), false);
                                }
                            }
                            if(kitConfiguration.getBoolean(objectPath + ".isDataItem")){
                                itemStack.getData().setData((byte) kitConfiguration.getInt(objectPath + ".Data"));
                            }
                            if(kitConfiguration.getBoolean(objectPath + ".isUnbreakable")){
                                itemMeta.setUnbreakable(true);
                            }
                            itemStack.setItemMeta(itemMeta);
                            fFaPlayer.getInventory().setItem(kitConfiguration.getInt(objectPath + ".Slot"), itemStack);
                        }
                        break;
                    }


                    }
                }

            }
        }
    }
