package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaEffectBlock;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.NMS;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class BlockListener implements Listener {
    private OnyxFFaMain main;

    public BlockListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }
    /*
    @EventHandler
    public void onPlaceFFaBlock(BlockPlaceEvent onFFablockPlaceEvent) {
        if (main.getBlockFileConfiguration().getList("NewOnyxFFa.Config.Block.BlockPlacedByPlayers").contains(onFFablockPlaceEvent.getBlock().getType().toString())) {
            if (onFFablockPlaceEvent.getPlayer().getGameMode() != GameMode.CREATIVE) {
                FFaBlock Ffablock = new FFaBlock(main, onFFablockPlaceEvent.getBlock());
            }
        }
    }*/

    @EventHandler
    public void OnPlaceBlock(BlockPlaceEvent onPlaceBlockEvent) {
        if (main.getBlockFileConfiguration().getList("NewOnyxFFa.Config.Block.BlockPlacedByPlayers").contains(onPlaceBlockEvent.getBlock().getType().toString())) {
            if (onPlaceBlockEvent.getPlayer().getGameMode() != GameMode.CREATIVE) {
                onPlaceBlockEvent.getBlock().getDrops().clear();
                final NMS nms = NMS.instance;
                nms.placedBlockTypes.put(onPlaceBlockEvent.getBlock(), onPlaceBlockEvent.getBlock().getType());
                final int timed = 1000 * main.getBlockFileConfiguration().getInt("NewOnyxFFa.Config.Block.TimerUntilBreak");
                long randGenerator;
                long time;
                randGenerator = new Random().nextInt(400) + timed;
                time = System.currentTimeMillis() + randGenerator;
                nms.placedBlocks.put(time, onPlaceBlockEvent.getBlock());
            }
        }

    }


    public void sendListMessage(List<String> messages, Player p) {
        for (Object message : messages) {
            p.sendMessage((String) message);
        }
    }

    public void sendListBroadcastMessage(List<String> messages, Player p) {
        for (Object message : messages) {
            Bukkit.broadcastMessage(message.toString().replaceAll("%player%", p.getName()));
        }
    }


    @EventHandler
    public void onBreakBlock(BlockBreakEvent onBreakBlock) {
        if (main.getBlockEffectList().contains(onBreakBlock.getBlock().getType().toString())) {
            final Player breakerPlayer = onBreakBlock.getPlayer();
            for (String block : main.getBlockFileConfiguration().getConfigurationSection("NewOnyxFFa.Config.Block.BlockWithEffects").getKeys(false)) {
                if (Material.getMaterial(main.getBlockFileConfiguration().getString("NewOnyxFFa.Config.Block.BlockWithEffects." + block + ".Material")) == onBreakBlock.getBlock().getType()) {
                    String blockPath = "NewOnyxFFa.Config.Block.BlockWithEffects." + block;
                    onBreakBlock.setDropItems(false);
                    if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("Potion")) {
                        for (String effect : main.getBlockFileConfiguration().getConfigurationSection(blockPath + ".Effect").getKeys(false)) {
                            String effectpath = blockPath + ".Effect." + effect;
                            int timer = 0;
                            for (PotionEffect potion : breakerPlayer.getActivePotionEffects()) {
                                if (potion.getType() == PotionEffectType.getByName(main.getBlockFileConfiguration().getString(effectpath + ".PotionEffect"))) {
                                    timer = potion.getDuration();
                                    breakerPlayer.removePotionEffect(potion.getType());
                                }
                            }
                            breakerPlayer.addPotionEffect(new PotionEffect(
                                    PotionEffectType.getByName(main.getBlockFileConfiguration().getString(effectpath + ".PotionEffect")),
                                    timer + main.getBlockFileConfiguration().getInt(effectpath + ".Duration") * 20,
                                    main.getBlockFileConfiguration().getInt(effectpath + ".Amplifier")));
                        }
                        sendListMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".Messages"), breakerPlayer);
                        sendListBroadcastMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".BroadcastMessages"), breakerPlayer);
                    } else if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("HealthBonus")) {
                        AttributeInstance attribute = breakerPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        if (attribute.getValue() < main.getBlockFileConfiguration().getInt(blockPath + ".HeartBonus") + 20) {
                            int MAX_HEALTH = main.getBlockFileConfiguration().getInt(blockPath + ".HeartBonus") + 20;
                            attribute.setBaseValue(MAX_HEALTH);
                            breakerPlayer.setHealth(breakerPlayer.getHealth() + main.getBlockFileConfiguration().getInt(blockPath + ".HeartBonus"));
                            sendListMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".Messages"), breakerPlayer);
                            sendListBroadcastMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".BroadcastMessages"), breakerPlayer);
                        } else {

                            breakerPlayer.sendMessage("§cTu as déjà obtenu un booster de vie permanent !");
                            onBreakBlock.setCancelled(true);
                            return;

                        }
                    } else if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("Item")) {
                        for (String item : main.getBlockFileConfiguration().getConfigurationSection(blockPath + ".Item").getKeys(false)) {
                            ItemStack itemStack = new ItemStack(
                                    Material.getMaterial(main.getBlockFileConfiguration().getString(blockPath + ".Item." + item + ".Material")),
                                    main.getBlockFileConfiguration().getInt(blockPath + ".Item." + item + ".Quantity"));
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (main.getBlockFileConfiguration().get(blockPath + ".Item." + item + ".Name") != null) {
                                itemMeta.setDisplayName(main.getBlockFileConfiguration().getString(blockPath + ".Item." + item + ".Name"));
                            }
                            if (main.getBlockFileConfiguration().getBoolean(blockPath + ".Item." + item + ".isUnbreakable")) {
                                itemMeta.setUnbreakable(true);
                                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                            }
                            if (main.getBlockFileConfiguration().getBoolean(blockPath + ".Item." + item + ".IsEnchanted")) {
                                for (String enchant : main.getBlockFileConfiguration().getConfigurationSection(blockPath + ".Item." + item + ".Enchantments").getKeys(false)) {
                                    itemMeta.addEnchant(Enchantment.getByName(main.getBlockFileConfiguration().getString(blockPath + ".Item." + item + ".Enchantments." + enchant + ".Type")), main.getBlockFileConfiguration().getInt(blockPath + ".Item." + item + ".Enchantments." + enchant + ".Amplifier"), false);
                                }
                            }
                            itemStack.setItemMeta(itemMeta);
                            breakerPlayer.getInventory().addItem(itemStack);
                        }
                        sendListMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".Messages"), onBreakBlock.getPlayer());
                        sendListBroadcastMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".BroadcastMessages"), breakerPlayer);
                    } else if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("CommandBlock")) {
                        for (String command : main.getBlockFileConfiguration().getStringList(blockPath + ".Commands")) {
                            command = command.replaceAll("%player%", breakerPlayer.getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                            sendListMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".Messages"), breakerPlayer);
                            sendListBroadcastMessage(main.getBlockFileConfiguration().getStringList(blockPath + ".BroadcastMessages"), breakerPlayer);
                        }
                    }
                    new FFaEffectBlock(main, onBreakBlock.getBlock().getLocation());
                }
            }
        } else if (!main.getBlockFileConfiguration().getList("NewOnyxFFa.Config.Block.AllowedBlocktoBreak").contains(onBreakBlock.getBlock().getType().toString())) {
            if (onBreakBlock.getPlayer().getGameMode() != GameMode.CREATIVE) {
                onBreakBlock.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWalkOnJumpad(PlayerMoveEvent walkOnJumpadEvent) {
        Location playerLocation = walkOnJumpadEvent.getPlayer().getLocation();
        Location blockLocation = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY() - 1, playerLocation.getBlockZ());
        for (String path : main.getBlockFileConfiguration().getConfigurationSection("NewOnyxFFa.Config.Block.JumpadBlock").getKeys(false))
            if (Material.getMaterial(main.getBlockFileConfiguration().getString("NewOnyxFFa.Config.Block.JumpadBlock." + path + ".Material")) == blockLocation.getBlock().getType()) {
                FFaPlayer player = main.getFFaPlayerManager().getFFaPlayer(main, walkOnJumpadEvent.getPlayer());
                player.getPlayer().setVelocity(player.getPlayer().getLocation().getDirection().multiply(main.getBlockFileConfiguration().getDouble("NewOnyxFFa.Config.Block.JumpadBlock." + path + ".VectorCoords.EyeLocationDirectionMovementMultiplier")).setY(main.getBlockFileConfiguration().getDouble("NewOnyxFFa.Config.Block.JumpadBlock." + path + ".VectorCoords.High")));
                player.getPlayer().playSound(playerLocation, Sound.ENTITY_FIREWORK_SHOOT, 10, 10);
            }
    }

    @EventHandler
    public void onWalkOnStaticJumpad(PlayerMoveEvent walkOnStaticJumpadEvent) {
        Player player = walkOnStaticJumpadEvent.getPlayer();
        Location playerLocation = player.getLocation();
        Location roundedPlayerLocation = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY() - 1, playerLocation.getBlockZ());
        FileConfiguration blockConfig = main.getBlockFileConfiguration();
        String defaultPath = "NewOnyxFFa.Config.Block.StaticJumpadBlock";
        for (String path : blockConfig.getConfigurationSection(defaultPath).getKeys(false)) {
            Location blockLocation = new Location(player.getWorld(), blockConfig.getDouble(defaultPath + "." + path + ".Location.x"), blockConfig.getDouble(defaultPath + "." + path + ".Location.y"), blockConfig.getDouble(defaultPath + "." + path + ".Location.z"));
            if (blockLocation.equals(roundedPlayerLocation)) {
                player.setVelocity(player.getLocation().getDirection().multiply(blockConfig.getDouble(defaultPath + "." + path + ".VectorCoords.EyeLocationDirectionMovementMultiplier")).setY(blockConfig.getDouble(defaultPath + "." + path + ".VectorCoords.High")));
                player.playSound(playerLocation, Sound.ENTITY_FIREWORK_SHOOT, 10, 10);
            }
        }
    }


}


