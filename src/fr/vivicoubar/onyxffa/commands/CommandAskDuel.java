package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.duels.Duel;
import fr.vivicoubar.onyxffa.duels.DuelManager;
import fr.vivicoubar.onyxffa.duels.DuelState;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length > 0) {
                Player player = (Player) sender;
                FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, player);
                Duel duel = main.duelManager.getDuelByPlayer(fFaPlayer);

                if (duel != null) {
                    if (duel.getState() != DuelState.PENDING) {
                        player.sendMessage("Attendez que votre duel finisse !");
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
                        player.sendMessage("Vous avez accepté le duel avec " + duel.getAsker().getPlayer().getName());
                        duel.getAsker().getPlayer().sendMessage(player.getName() + " a accepté votre invitation en duel");
                    }
                    else if (args[0].equalsIgnoreCase("deny") && duel.getState() == DuelState.PENDING && duel.getAsked() == fFaPlayer) {
                        player.sendMessage("Vous avez refusé le duel avec " + duel.getAsker().getPlayer().getName());
                        duel.getAsker().getPlayer().sendMessage(player.getName() + " a refusé votre invitation en duel");
                        main.duelManager.removeDuel(duel);
                    }
                    else if (args[0].equalsIgnoreCase("cancel") && duel.getState() == DuelState.PENDING && duel.getAsker() == fFaPlayer) {
                        player.sendMessage("Vous avez annulé le duel avec " + duel.getAsked().getPlayer().getName());
                        duel.getAsked().getPlayer().sendMessage(player.getName() + " a annulé sa demande de duel");
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
                                player.sendMessage("Vous avez bien invité " + potentialAsked.getName() + " à vous combattre en duel.");
                                potentialAsked.sendMessage(player.getName() + " vous invite à combattre en duel ! /duel accept pour accepter ce duel !");
                            } else if (asked.getState() == FFaPlayerStates.DUEL) {
                                player.sendMessage("Ce joueur est déjà en duel !");
                            } else {
                                player.sendMessage("Ce joueur est occupé à combattre dans le FFA, il doit être au lobby !");
                            }
                            return true;
                        }
                    }
                    player.sendMessage(args[0] + " n'est pas connecté !");
                    return true;
                }
            } else {

            }
        }
        return false;
    }
}
