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

public class CommandPoints implements CommandExecutor {
    private OnyxFFaMain main;
    public CommandPoints(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command label, String arg, String[] args) {
        if(label.getName().equalsIgnoreCase("points")){
            if(sender instanceof Player) {
                if(args.length == 0){
                    return false;
                }else{
                        if(args.length < 3){
                            main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Points.Error");
                            return true;
                        }else{
                            for (Player player : Bukkit.getOnlinePlayers()){
                                if(player.getName().equalsIgnoreCase(args[1])) {
                                    FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, player);
                                    double points = fFaPlayer.getStats().getScore().get(2);
                                    if(args[0].equalsIgnoreCase("add")){
                                        try {
                                            points += Double.parseDouble(args[2]);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Points.Error"));
                                        }
                                    }
                                    else if(args[0].equalsIgnoreCase("set")){
                                        try {
                                            points = Double.parseDouble(args[2]);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Points.Error"));
                                        }
                                    }else{
                                        return false;
                                    }
                                    try {
                                        Rank oldPlayerRank = fFaPlayer.getStats().getRank();
                                        fFaPlayer.getStats().setPoints(points);
                                        fFaPlayer.updateStats();
                                        Rank newPlayerRank = fFaPlayer.getStats().getRank();
                                        if (oldPlayerRank != newPlayerRank) {
                                            for (String command : newPlayerRank.getCommandOnGoToRank()) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", fFaPlayer.getPlayer().getName()));
                                            }
                                            for (String command : oldPlayerRank.getCommandOnLeaveRank()) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", fFaPlayer.getPlayer().getName()));
                                            }
                                        }
                                        sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Points.Success").replaceAll("%player%", fFaPlayer.getPlayer().getName()).replaceAll("%score%", String.valueOf(points)).replaceAll("%highScore%", String.valueOf(fFaPlayer.getStats().getHighscore())));
                                        return true;

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return true;
                                    }
                                }

                            }
                            sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.Points.PlayerOffline").replaceAll("%player%", args[1]));
                            return true;
                        }
                    }
            }

        }
        return false;
    }
}
