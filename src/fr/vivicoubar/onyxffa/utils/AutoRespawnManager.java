package fr.vivicoubar.onyxffa.utils;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
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

            Location spawn = new SpawnManager(main, main.getSpawnsList()).getSelectedSpawn();
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
                        fFaPlayer.getPlayer().teleport(spawn, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
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
                                    helmetMeta.setColor(Color.fromRGB(red, green, blue));
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
                                    chestPlateMeta.setColor(Color.fromRGB(red, green, blue));
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
                                    leggingsMeta.setColor(Color.fromRGB(red, green, blue));
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
                                    bootsMeta.setColor(Color.fromRGB(red, green, blue));
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
                                for (String path : kitConfiguration.getConfigurationSection("NewOnyxFFa.Ffa." + rankpath + ".Items").getKeys(false)) {
                                    String objectPath = "NewOnyxFFa.Ffa." + rankpath + ".Items." + path;
                                    ItemStack itemStack = new ItemStack(Material.getMaterial(kitConfiguration.getString(objectPath + ".Material")), kitConfiguration.getInt(objectPath + ".Quantity"));
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    if (kitConfiguration.getBoolean(objectPath + ".Enchanted")) {
                                        for (String enchant : kitConfiguration.getConfigurationSection(objectPath + ".Enchantments").getKeys(false)) {
                                            String enchantPath = objectPath + ".Enchantments." + enchant;
                                            itemMeta.addEnchant(Enchantment.getByName(kitConfiguration.getString(enchantPath + ".Type")), kitConfiguration.getInt(enchantPath + ".Amplifier"), false);
                                        }
                                    }
                                    if (kitConfiguration.getBoolean(objectPath + ".isDataItem")) {
                                        itemStack.getData().setData((byte) kitConfiguration.getInt(objectPath + ".Data"));
                                    }
                                    if (kitConfiguration.getBoolean(objectPath + ".isUnbreakable")) {
                                        itemMeta.setUnbreakable(true);
                                    }
                                    itemStack.setItemMeta(itemMeta);
                                    fFaPlayer.getInventory().setItem(kitConfiguration.getInt(objectPath + ".Slot"), itemStack);
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
                    player.teleport(
                            new Location(Bukkit.getWorld(spawnConfiguration.getString("NewOnyxFFa.Spawns.Lobby.WorldName")),
                                    spawnConfiguration.getDouble("NewOnyxFFa.Spawns.Lobby.x"),
                                    spawnConfiguration.getDouble("NewOnyxFFa.Spawns.Lobby.y"),
                                    spawnConfiguration.getDouble("NewOnyxFFa.Spawns.Lobby.z"),
                                    (float) spawnConfiguration.getDouble("NewOnyxFFa.Spawns.Lobby.yaw"),
                                    (float) spawnConfiguration.getDouble("NewOnyxFFa.Spawns.Lobby.pitch")));
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
