package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandOffa implements CommandExecutor {
    private OnyxFFaMain main;
    public CommandOffa(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if(command.getName().equalsIgnoreCase("offa")){
            if (strings.length != 0){
                if (strings[0].equalsIgnoreCase("setspawn")){
                    if(commandSender instanceof Player){
                        FFaPlayer sender = main.getFFaPlayerManager().getFFaPlayer(main, (Player) commandSender);
                        Location senderLocation = sender.getPlayer().getLocation();
                        FileConfiguration spawnConfiguration = main.getSpawnsConfiguration();
                        List<String> spawns = new ArrayList<>();
                        for(String number : spawnConfiguration.getConfigurationSection("NewOnyxFFa.Spawns").getKeys(false))
                            spawns.add(number);
                        int num = spawns.size();
                        spawnConfiguration.set("NewOnyxFFa.Spawns." + num + ".x", senderLocation.getX());
                        spawnConfiguration.set("NewOnyxFFa.Spawns." + num + ".y", senderLocation.getY());
                        spawnConfiguration.set("NewOnyxFFa.Spawns." + num + ".z", senderLocation.getZ());
                        spawnConfiguration.set("NewOnyxFFa.Spawns." + num + ".yaw", senderLocation.getYaw());
                        spawnConfiguration.set("NewOnyxFFa.Spawns." + num + ".pitch", senderLocation.getPitch());
                        try {
                            spawnConfiguration.save(main.getSpawnsFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sender.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Offa.Spawn.Success")
                                .replaceAll("%x%", String.valueOf(senderLocation.getBlockX()))
                                .replaceAll("%y%", String.valueOf(senderLocation.getBlockY()))
                                .replaceAll("%z%", String.valueOf(senderLocation.getBlockZ()))
                                .replaceAll("%yaw%", String.valueOf(senderLocation.getYaw()))
                                .replaceAll("%pitch%", String.valueOf(senderLocation.getPitch())));
                        return true;


                    }
                }
                else if (strings[0].equalsIgnoreCase("breakBlock")){
                    if(commandSender instanceof Player){
                        Player player = (Player) commandSender;
                        Block target = player.getTargetBlock(null, 5);
                        if(target.getType() == Material.AIR){
                           player.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Offa.Break.Error"));
                            return true;
                        }else{
                            player.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Offa.Break.Success"));
                            target.setType(Material.AIR);
                            return true;
                        }

                    }
                }
            }
        }
        return false;
        }

}
