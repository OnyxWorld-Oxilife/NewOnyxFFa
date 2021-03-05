package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAutoRespawn implements CommandExecutor {
    private OnyxFFaMain main;
    public CommandAutoRespawn(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            for(FFaPlayer player : main.getfFaPlayerManager().getfFaPlayerList()){
                if(player.getUniqueID() == sender.getUniqueId()){
                    FFaPlayer ffasender = player;
                    ffasender.setAutorespawnBoolean();
                    if(ffasender.isAutorespawnBoolean()) {
                        ffasender.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ChangeAutoRespawn.true"));
                    }else{
                        ffasender.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ChangeAutoRespawn.false"));
                    }
                        return true;

                }
            }
            FFaPlayer ffasender = new FFaPlayer(this.main, sender);
            ffasender.setAutorespawnBoolean();
            return true;
        }
        return false;
    }
}
