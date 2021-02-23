package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandStats implements CommandExecutor {
    private OnyxFFaMain main;

    public CommandStats(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("stats")){
            if(sender instanceof Player){
                FFaPlayer fFaPlayer = new FFaPlayer(main, (Player) sender);
                List<Double> score = fFaPlayer.getStats().getScore();
                double kills = score.get(0);
                double deaths = score.get(1);
                double points = score.get(2);
                Rank rank = fFaPlayer.getStats().getRank();
                String rankname = rank.getName();
                sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.StatsCommand.Stats").replaceAll("%rank%", rankname)
                .replaceAll("%kills%", String.valueOf(kills)).replaceAll("%deaths%", String.valueOf(deaths)).replaceAll("%points%", String.valueOf(points)));
            }

        }
        return false;
    }
}
