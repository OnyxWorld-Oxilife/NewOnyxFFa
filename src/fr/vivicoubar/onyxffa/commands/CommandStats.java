package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import fr.vivicoubar.onyxffa.utils.Rank;
import fr.vivicoubar.onyxffa.utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandStats implements CommandExecutor {
    private OnyxFFaMain main;

    public CommandStats(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if (command.getName().equalsIgnoreCase("stats")) {
            if (sender instanceof Player) {
                if (strings.length == 0 || !sender.hasPermission("NewOnyxFFa.stats.others")) {
                    FFaPlayer fFaPlayer = new FFaPlayer(main, (Player) sender);
                    List<Double> score = fFaPlayer.getStats().getScore();
                    double kills = score.get(0);
                    double deaths = score.get(1);
                    double points = score.get(2);
                    Rank rank = fFaPlayer.getStats().getRank();
                    String rankname = rank.getName();
                    sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.StatsCommand.Stats").replaceAll("%rank%", rankname)
                            .replaceAll("%kills%", String.valueOf(kills)).replaceAll("%deaths%", String.valueOf(deaths)).replaceAll("%points%", String.valueOf(points)));
                    return true;
                } else {
                    String playerName = strings[0];
                    List<String> playersInStatFile = new ArrayList<>();
                    for (String playerPath : main.getStatsConfiguration().getConfigurationSection("NewOnyxFFa").getKeys(false))
                        if (!playerPath.equalsIgnoreCase("Description")) {
                            playersInStatFile.add(main.getStatsConfiguration().getString("NewOnyxFFa." + playerPath + ".Pseudo"));
                        }
                    for (String player : playersInStatFile) {
                        if (player.equalsIgnoreCase(playerName)) {
                            for (String playerPath : main.getStatsConfiguration().getConfigurationSection("NewOnyxFFa").getKeys(false))
                                if (!playerPath.equalsIgnoreCase("Description")) {
                                    if (main.getStatsConfiguration().getString("NewOnyxFFa." + playerPath + ".Pseudo").equalsIgnoreCase(player)) {
                                        double kills = main.getStatsConfiguration().getDouble("NewOnyxFFa." + playerPath + ".Kills");
                                        double deaths = main.getStatsConfiguration().getDouble("NewOnyxFFa." + playerPath + ".Deaths");
                                        double points = main.getStatsConfiguration().getDouble("NewOnyxFFa." + playerPath + ".Points");
                                        String rankname = "";
                                        for (Rank setRank : main.getRanksManager().getRanks()) {
                                            if (points >= setRank.getLowerBound() && points < setRank.getUpperBound()) {
                                                rankname = setRank.getName();
                                            }
                                        }
                                        sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.StatsCommand.OtherStats").replaceAll("%rank%", rankname)
                                                .replaceAll("%kills%", String.valueOf(kills)).replaceAll("%deaths%", String.valueOf(deaths)).replaceAll("%points%", String.valueOf(points)).replaceAll("%player%", playerName));
                                        return true;
                                    }


                                }
                        }
                        sender.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.StatsCommand.OtherNotFound").replaceAll("%player%",playerName));
                        return true;
                    }
                }

            }

        }
        return false;
    }
}
