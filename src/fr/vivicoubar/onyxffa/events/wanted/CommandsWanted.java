package fr.vivicoubar.onyxffa.events.wanted;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsWanted implements CommandExecutor {
    private final OnyxFFaMain main;

    public CommandsWanted(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(s.equalsIgnoreCase("joinwanted")){
                if(main.wantedEvent.state == EventState.STARTING && ((Player) commandSender).getGameMode() == GameMode.ADVENTURE){
                    main.wantedEvent.playerJoinWanted(main.getfFaPlayerManager().getFFaPlayer(main, (Player) commandSender));
                }else{
                    return false;
                }
            }else if(s.equalsIgnoreCase("startwanted") && main.wantedEvent.getState() == EventState.WAITING){
                main.wantedEvent.startWanted();
            }
        }
        return false;
    }
}
