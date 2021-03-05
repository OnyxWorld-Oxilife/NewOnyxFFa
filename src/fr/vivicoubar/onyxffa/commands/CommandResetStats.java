package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
                    FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, (Player) sender);
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
                            FFaPlayer ffaplayer = main.getfFaPlayerManager().getFFaPlayer(main, Bukkit.getPlayer(strings[0]));
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
