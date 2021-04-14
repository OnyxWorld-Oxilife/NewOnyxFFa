package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAskDuel implements CommandExecutor {
    private OnyxFFaMain main;

    public CommandAskDuel(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arg) {
        if(sender instanceof Player){
            if(arg.length > 0){
                FFaPlayer asker = main.getfFaPlayerManager().getFFaPlayer(main, (Player) sender);
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player.getName().equalsIgnoreCase(arg[0])){
                        FFaPlayer asked = main.getfFaPlayerManager().getFFaPlayer(main ,player);
                        if(asked.getState() == FFaPlayerStates.WAITING){
                            //TODO CHOISIR UNE ARENE EN VERIFIANT QUELLE SOIT LIBRE

                        }
                    }
                }
            }
        }
        return false;
    }
}
