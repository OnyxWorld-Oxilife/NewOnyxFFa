package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.List;


public class CommandSpawn implements CommandExecutor {
    private final OnyxFFaMain main;

    public CommandSpawn(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("Spawn")){
            if(sender instanceof Player) {
                FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, (Player) sender);
                Player player = fFaPlayer.getPlayer();
                List<String> SpawnInWait = main.getSpawnsInWait();
                SpawnInWait.add(player.getUniqueId().toString());
                player.getActivePotionEffects().clear();
                player.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.SpawnCommand.Wait").replaceAll("%timer%", main.getConfigConfiguration().getString("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (SpawnInWait.contains(player.getUniqueId().toString())) {
                            SpawnInWait.remove(player.getUniqueId().toString());
                            FileConfiguration spawnConfiguration = main.getSpawnsConfiguration();
                            FileConfiguration configConfiguration = main.getConfigConfiguration();
                            player.setGameMode(GameMode.ADVENTURE);
                            player.setHealth(20);
                            player.getInventory().clear();
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
                            for (PotionEffect effect : player.getActivePotionEffects()) {
                                player.removePotionEffect(effect.getType());
                            }


                        }
                    }

                }.runTaskLater(this.main, (long) (20 *main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                return true;
            }
    }
        return false;
}}

