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
        if(commandSender instanceof Player && main.sumoEvent.getState() == EventState.STARTING) {
            if (strings.length > 0) {
                if(strings[0].equalsIgnoreCase("join") || strings[0].equalsIgnoreCase("quit")) {
                    FFaPlayer sender = main.getFFaPlayerManager().getFFaPlayer(main, (Player) commandSender);

                    if (strings[0].equalsIgnoreCase("join")) {
                        if(sender.getState() == FFaPlayerStates.WAITING ){
                            main.sumoEvent.playerJoinOnyxEvent(sender);
                            return true;
                        }else{
                            sender.getPlayer().sendMessage("§c Tu dois être au spawn pour rejoindre l'Event!");
                            return true;
                        }
                    } else if (strings[0].equalsIgnoreCase("quit")) {
                        if(sender.getState() == FFaPlayerStates.SUMO ) {
                            main.sumoEvent.playerQuitOnyxEvent(sender);
                            return true;
                        }else{
                            sender.getPlayer().sendMessage("§cTu dois participer à un Event Sumo pour pouvoir le quitter!");
                        }
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else if(commandSender instanceof Player){
            if(main.sumoEvent.getState() == EventState.WAITING){
                commandSender.sendMessage("§cIl n'y a pas d'Event en cours!");
            }else if(main.sumoEvent.getState() == EventState.PLAYING || main.sumoEvent.getState() == EventState.STOPPING){
                commandSender.sendMessage("§cL'Event a déjà commencé!");
            }
            return true;
        }
        return false;
    }
}
