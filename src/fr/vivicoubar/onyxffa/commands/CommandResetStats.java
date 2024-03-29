package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandResetStats implements CommandExecutor {
    private OnyxFFaMain main;

    public CommandResetStats(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("resetstats")){
            if(sender instanceof Player) {
                if (strings.length == 0) {
                    FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, (Player) sender);
                    try {
                        fFaPlayer.getStats().reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fFaPlayer.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommand.Success"));
                    return true;
                }else{
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.getName().equalsIgnoreCase(strings[0])){
                            FFaPlayer ffaplayer = main.getFFaPlayerManager().getFFaPlayer(main, Bukkit.getPlayer(strings[0]));
                            try {
                                ffaplayer.getStats().reset();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((Player) sender).getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommandOther.Success").replaceAll("%player%", ffaplayer.getPlayer().getName()));
                            return true;
                        }
                    }
                    sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommandOther.Error").replaceAll("%player%", strings[0]));
                }
            }
        }
        return false;
    }
}
