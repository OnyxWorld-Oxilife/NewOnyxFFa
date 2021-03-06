package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaEffectBlock;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.NMS;
import org.bukkit.*;
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
    public void OnPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        event.getBlock().getDrops().clear();
        final NMS nms = NMS.instance;
        nms.sendBreakPacket(event.getBlock().getLocation(), 0, event.getBlock());
        nms.placedBlockTypes.put(event.getBlock(), event.getBlock().getType());
        final int timed = 10000;
        long randGenerator;
        long time;
        for (randGenerator = new Random().nextInt(600) + timed, time = System.currentTimeMillis() + randGenerator; nms.brokenBlocks.containsKey(time); time = System.currentTimeMillis() + randGenerator) {
            randGenerator = new Random().nextInt(600) + timed;
        }
        nms.placedBlocks.put(time, event.getBlock());
        nms.placedTotalBlocks.put(event.getBlock().getLocation(), event.getBlock());
    }



    @EventHandler
    public void onBreakBlock(BlockBreakEvent onBreakBlock) {
        if (main.getBlockEffectList().contains(onBreakBlock.getBlock().getType().toString())) {
            for (String block : main.getBlockFileConfiguration().getConfigurationSection("NewOnyxFFa.Config.Block.BlockWithEffects").getKeys(false)) {
                if (Material.getMaterial(main.getBlockFileConfiguration().getString("NewOnyxFFa.Config.Block.BlockWithEffects." + block + ".Material")) == onBreakBlock.getBlock().getType()) {
                    String blockPath = "NewOnyxFFa.Config.Block.BlockWithEffects." + block;
                    onBreakBlock.setDropItems(false);
                    FFaEffectBlock blockEffect = new FFaEffectBlock(main, onBreakBlock.getBlock().getLocation());
                    if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("Potion")) {
                        for (String effect : main.getBlockFileConfiguration().getConfigurationSection(blockPath + ".Effect").getKeys(false)) {
                            String effectpath = blockPath + ".Effect." + effect;
                            int timer = 0;
                            for(PotionEffect potion : onBreakBlock.getPlayer().getActivePotionEffects()){
                                if(potion.getType() == PotionEffectType.getByName(main.getBlockFileConfiguration().getString(effectpath + ".PotionEffect"))){
                                    timer = potion.getDuration();
                                }
                            }
                            onBreakBlock.getPlayer().addPotionEffect(new PotionEffect(
                                    PotionEffectType.getByName(main.getBlockFileConfiguration().getString(effectpath + ".PotionEffect")),
                                    timer + main.getBlockFileConfiguration().getInt(effectpath + ".Duration"),
                                    main.getBlockFileConfiguration().getInt(effectpath + ".Amplifier")));
                        }
                    } else if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("HealthBonus")) {
                        int MAX_HEALTH = main.getBlockFileConfiguration().getInt(main.getBlockFileConfiguration().getString(blockPath + ".HeartBonus"));
                        onBreakBlock.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2000, MAX_HEALTH));
                    } else if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("Item")) {
                        for (String item : main.getBlockFileConfiguration().getConfigurationSection(blockPath + ".Item").getKeys(false)) {
                            ItemStack itemStack = new ItemStack(
                                    Material.getMaterial(main.getBlockFileConfiguration().getString(blockPath + ".Item." + item + ".Material")),
                                    main.getBlockFileConfiguration().getInt(blockPath + ".Item." + item + ".Quantity"));
                            ItemMeta itemMeta = itemStack.getItemMeta();
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
                            onBreakBlock.getPlayer().getInventory().addItem(itemStack);


                        }
                    } else if (main.getBlockFileConfiguration().getString(blockPath + ".EffectType").equalsIgnoreCase("CommandBlock")) {
                        for (String command : main.getBlockFileConfiguration().getStringList(blockPath + ".Commands")) {
                            command = command.replaceAll("%player%", onBreakBlock.getPlayer().getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        }
                    }
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
            if (Material.getMaterial(main.getBlockFileConfiguration().getString("NewOnyxFFa.Config.Block.JumpadBlock." + path + ".Material")) == blockLocation.getBlock().getType()){
                FFaPlayer player = new FFaPlayer(main, walkOnJumpadEvent.getPlayer());
                player.getPlayer().setVelocity(player.getPlayer().getLocation().getDirection().multiply(main.getBlockFileConfiguration().getDouble("NewOnyxFFa.Config.Block.JumpadBlock."+ path +".VectorCoords.EyeLocationDirectionMovementMultiplier")).setY(main.getBlockFileConfiguration().getDouble("NewOnyxFFa.Config.Block.JumpadBlock."+ path +".VectorCoords.High")));
                player.getPlayer().playSound(playerLocation, Sound.ENTITY_FIREWORK_SHOOT, 10, 10);
            }
    }
}


