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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("Spawn")){
            if(sender instanceof Player) {
                FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, (Player) sender);
                if((main.getfFaPlayerManager().getFFaPlayer(main, (Player)sender).isInArena())) {
                    Player player = fFaPlayer.getPlayer();
                    List<String> SpawnInWait = main.getSpawnsInWait();
                    SpawnInWait.add(player.getUniqueId().toString());
                    player.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.SpawnCommand.Wait").replaceAll("%timer%", main.getConfigConfiguration().getString("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (SpawnInWait.contains(player.getUniqueId().toString())) {
                                SpawnInWait.remove(player.getUniqueId().toString());
                                fFaPlayer.sendToSpawn();
                            }
                        }

                    }.runTaskLater(this.main, (long) (20 *main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                    return true;
                } else {
                    fFaPlayer.sendToSpawn();
                }
            }
    }
        return false;
}}

