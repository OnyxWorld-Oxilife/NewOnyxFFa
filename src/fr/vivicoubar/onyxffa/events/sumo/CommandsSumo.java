package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandsSumo implements CommandExecutor {
    private OnyxFFaMain main = OnyxFFaMain.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            FFaPlayer sender = main.getFFaPlayerManager().getFFaPlayer(main, (Player) commandSender);
            if(strings.length >0){
                switch (strings[0]) {
                    case "join":
                        if(main.sumoEvent.getState() == EventState.STARTING && sender.getState() == FFaPlayerStates.WAITING){
                            main.sumoEvent.playerJoinOnyxEvent(sender);
                        }else if(main.sumoEvent.getState() == EventState.STARTING){
                            sender.getPlayer().sendMessage("§cErreur, tu dois être au spawn pour rejoindre un Event!");
                        }else{
                            sender.getPlayer().sendMessage("§cErreur, tu ne peux pas rejoindre l'Event pour le moment!");
                        }
                        return true;
                    case "quit":
                        if(sender.getState() == FFaPlayerStates.SUMO){
                            main.sumoEvent.playerQuitOnyxEvent(sender);
                        }else{
                            sender.getPlayer().sendMessage("§cErreur, tu ne peux pas quitter l'Event Sumo si tu ne participes pas!");
                        }
                        return true;
                    case "startevent":
                        if(sender.getPlayer().hasPermission("NewOnyxFFa.sumo.admin")){
                           if(main.sumoEvent.getState() == EventState.WAITING) {
                               main.sumoEvent.startEvent();
                           }else {
                               sender.getPlayer().sendMessage("§cErreur, un Event a déjà commencé!");
                           }
                            return true;
                        }else{
                            return false;
                        }
                    case "stopevent":
                        if(sender.getPlayer().hasPermission("NewOnyxFFa.sumo.admin")) {
                            if(main.sumoEvent.getState() != EventState.WAITING) {
                                main.sumoEvent.stopEvent();
                            }else {
                                sender.getPlayer().sendMessage("§cErreur, il n'y a pas d'Event en cours");
                            }
                            return true;
                        }else{
                            return false;
                        }
                    case "setspawn":
                        if(sender.getPlayer().hasPermission("NewOnyxFFa.sumo.admin")) {
                            int spawncounter = 1;
                            if (main.getArenaFileConfiguration().get("NewOnyxFFa.SumoEvent.Spawns") != null){
                                for (String spawn : main.getArenaFileConfiguration().getConfigurationSection("NewOnyxFFa.SumoEvent.Spawns").getKeys(false)) {
                                    spawncounter++;
                                }
                            }
                            main.getArenaFileConfiguration().set("NewOnyxFFa.SumoEvent.Spawns." +spawncounter + ".x", sender.getPlayer().getLocation().getBlockX());
                            main.getArenaFileConfiguration().set("NewOnyxFFa.SumoEvent.Spawns." +spawncounter + ".y", sender.getPlayer().getLocation().getBlockY());
                            main.getArenaFileConfiguration().set("NewOnyxFFa.SumoEvent.Spawns." +spawncounter + ".z", sender.getPlayer().getLocation().getBlockZ());
                            main.getArenaFileConfiguration().set("NewOnyxFFa.SumoEvent.Spawns." +spawncounter + ".pitch", sender.getPlayer().getLocation().getPitch());
                            main.getArenaFileConfiguration().set("NewOnyxFFa.SumoEvent.Spawns." +spawncounter + ".yaw", sender.getPlayer().getLocation().getYaw());
                            try {
                                main.getArenaFileConfiguration().save(main.getArenaFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }else{
                            return false;
                        }
                    case "clearspawn":
                        if(sender.getPlayer().hasPermission("NewOnyxFFa.sumo.admin")) {
                            main.getArenaFileConfiguration().set("NewOnyxFFa.SumoEvent.Spawns", null);
                            try {
                                main.getArenaFileConfiguration().save(main.getArenaFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }else{
                            return false;
                        }
                    default:
                        return false;
                }
                }else{
                    return false;
                }
        }else{
            return false;
        }
    }
}


