package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.duels.Duel;
import fr.vivicoubar.onyxffa.duels.DuelState;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandAskDuel implements CommandExecutor {
    private OnyxFFaMain main;
    private FileConfiguration messagesConfig = main.getMessagesConfiguration();
    private String path = "NewOnyxFFa.Messages.Duel.";

    public CommandAskDuel(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length > 0) {
                Player player = (Player) sender;
                FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, player);
                Duel duel = main.duelManager.getDuelByPlayer(fFaPlayer);

                if (duel != null) {
                    if (duel.getState() != DuelState.PENDING) {
                        player.sendMessage(messagesConfig.getString(path + "NotFinished"));
                    }
                    else if (args.length < 1) {
                        if (duel.getAsked() == fFaPlayer) {
                            player.sendMessage("§cUsage : /duel <accept/deny>");
                        }
                        if (duel.getAsker() == fFaPlayer) {
                            player.sendMessage("§cUsage : /duel <cancel>");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("accept") && duel.getState() == DuelState.PENDING && duel.getAsked() == fFaPlayer) {
                        duel.teleportDuel();
                        player.sendMessage(messagesConfig.getString(path + "Invitation.Accepted.Asked").replaceAll("%player%", duel.getAsker().getPlayer().getName()));
                        duel.getAsker().getPlayer().sendMessage(messagesConfig.getString(path + "Invitation.Accepted.Sender").replaceAll("%player%", player.getName()));
                    }
                    else if (args[0].equalsIgnoreCase("deny") && duel.getState() == DuelState.PENDING && duel.getAsked() == fFaPlayer) {
                        player.sendMessage(messagesConfig.getString(path + "Invitation.Denied.Asked").replaceAll("%player%", duel.getAsker().getPlayer().getName()));
                        duel.getAsker().getPlayer().sendMessage(messagesConfig.getString(path + "Invitation.Denied.Sender").replaceAll("%player%", player.getName()));
                        main.duelManager.removeDuel(duel);
                    }
                    else if (args[0].equalsIgnoreCase("cancel") && duel.getState() == DuelState.PENDING && duel.getAsker() == fFaPlayer) {
                        player.sendMessage(messagesConfig.getString(path + "Invitation.Cancel.Asked").replaceAll("%player%", duel.getAsked().getPlayer().getName()));
                        duel.getAsked().getPlayer().sendMessage(messagesConfig.getString(path + "Invitation.Cancel.Sender").replaceAll("%player%", player.getName()));
                        main.duelManager.removeDuel(duel);
                    }
                    else {
                        if (duel.getAsked() == fFaPlayer) {
                            player.sendMessage("§cUsage : /duel <accept/deny>");
                        }
                        if (duel.getAsker() == fFaPlayer) {
                            player.sendMessage("§cUsage : /duel <cancel>");
                        }
                    }
                    return true;
                } else {
                    for (Player potentialAsked : Bukkit.getOnlinePlayers()) {
                        if (potentialAsked.getName().equalsIgnoreCase(args[0])) {
                            FFaPlayer asked = main.getfFaPlayerManager().getFFaPlayer(main, potentialAsked);
                            if (asked.getState() == FFaPlayerStates.WAITING) {
                                main.getDuelManager().addDuel(fFaPlayer, asked);
                                player.sendMessage(messagesConfig.getString(path + "Invitation.Demand.Sender").replaceAll("%player%", potentialAsked.getName()));
                                potentialAsked.sendMessage(messagesConfig.getString(path + "Invitation.Demand.Asked").replaceAll("%player%", player.getName()));
                            } else if (asked.getState() == FFaPlayerStates.DUEL) {
                                player.sendMessage(messagesConfig.getString(path + "AlreadyInDuel").replaceAll("%player%", asked.getPlayer().getName()));
                            } else {
                                player.sendMessage(messagesConfig.getString(path + "InFFAArena").replaceAll("%player%", asked.getPlayer().getName()));;
                            }
                            return true;
                        }
                    }
                    player.sendMessage("§c" + args[0] + " n'est pas connecté !");
                    return true;
                }
            } else {

            }
        }
        return false;
    }
}
