package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

    public void sendPlayerToSpawn(Player p) {
        FileConfiguration configConfiguration = main.getConfigConfiguration();
        p.setGameMode(GameMode.ADVENTURE);
        main.getfFaPlayerManager().getFFaPlayer(main , p).setState(FFaPlayerStates.WAITING);
        p.setHealth(20);
        AttributeInstance attribute = p.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(20);
        p.getInventory().clear();
        p.teleport(main.getLocationBuilder().getLocation("NewOnyxFFa.Spawns.Lobby"));
        ItemStack menuSelector = new ItemStack(Material.getMaterial(configConfiguration.getString("NewOnyxFFa.Config.Menu.Item.Material")));
        ItemMeta menuMeta = menuSelector.getItemMeta();
        if ((boolean) configConfiguration.get("NewOnyxFFa.Config.Menu.Item.Enchanted")) {
            menuMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        }
        menuMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        menuMeta.setDisplayName(configConfiguration.getString("NewOnyxFFa.Config.Menu.Item.Name"));
        menuMeta.setLore(configConfiguration.getStringList("NewOnyxFFa.Config.Menu.Item.Lore"));
        menuSelector.setItemMeta(menuMeta);
        p.getInventory().setItem(4, menuSelector);
        p.getInventory().setHeldItemSlot(4);
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("Spawn")){
            if(sender instanceof Player) {
                if((main.getfFaPlayerManager().getFFaPlayer(main, (Player)sender).isInArena())) {
                    FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, (Player) sender);
                    Player player = fFaPlayer.getPlayer();
                    List<String> SpawnInWait = main.getSpawnsInWait();
                    SpawnInWait.add(player.getUniqueId().toString());
                    player.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.SpawnCommand.Wait").replaceAll("%timer%", main.getConfigConfiguration().getString("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (SpawnInWait.contains(player.getUniqueId().toString())) {
                                SpawnInWait.remove(player.getUniqueId().toString());
                                sendPlayerToSpawn(player);
                            }
                        }

                    }.runTaskLater(this.main, (long) (20 *main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                    return true;
                } else {
                    sendPlayerToSpawn(((Player) sender).getPlayer());
                }
            }
    }
        return false;
}}

