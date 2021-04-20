package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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


