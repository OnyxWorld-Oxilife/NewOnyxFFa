package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    FFaPlayer fFaPlayer = new FFaPlayer(main, (Player) sender);
                    fFaPlayer.getStats().reset();
                    fFaPlayer.getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommand.Success"));
                }else{
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.getName().equalsIgnoreCase(strings[0])){
                            FFaPlayer ffaplayer = new FFaPlayer(main, Bukkit.getPlayer(strings[0]));
                            ffaplayer.getStats().reset();
                            ((Player) sender).getPlayer().sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommandOther.Success").replaceAll("%player%", ffaplayer.getPlayer().getName()));
                            break;
                        }
                    }
                    sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.ResetStatsCommandOther.Error").replaceAll("%player%", strings[0]));
                }
            }
        }
        return false;
    }
}
