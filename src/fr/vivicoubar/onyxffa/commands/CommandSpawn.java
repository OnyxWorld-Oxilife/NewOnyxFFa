package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.List;


public class CommandSpawn implements CommandExecutor {
    private final OnyxFFaMain main;

    public CommandSpawn(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg, String[] strings) {
        if(command.getName().equalsIgnoreCase("Spawn")){
            if(sender instanceof Player) {
                FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, (Player) sender);
                if((main.getFFaPlayerManager().getFFaPlayer(main, (Player)sender).isInArena())) {
                    Player player = fFaPlayer.getPlayer();
                    List<String> SpawnInWait = main.getSpawnsInWait();
                    SpawnInWait.add(player.getUniqueId().toString());
                    player.sendMessage(main.getMessagesConfiguration().getString("NewOnyxFFa.Messages.SpawnCommand.Wait").replaceAll("%timer%", main.getConfigConfiguration().getString("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (SpawnInWait.contains(player.getUniqueId().toString())) {
                                SpawnInWait.remove(player.getUniqueId().toString());
                                fFaPlayer.sendToSpawn();
                            }
                        }

                    }.runTaskLater(this.main, (long) (20 *main.getConfigConfiguration().getDouble("NewOnyxFFa.Config.SpawnCommand.TimerUntilTeleportation")));
                    return true;
                } else if (fFaPlayer.getState() == FFaPlayerStates.DUEL) {
                    sender.sendMessage("§cTu dois finir ton duel avant d'aller au spawn !");
                } else if(fFaPlayer.getState() != FFaPlayerStates.MODO) {
                    sender.sendMessage("§cTu dois retirer ton vanish pour retourner au spawn");
                } else {
                    fFaPlayer.sendToSpawn();
                }
            }
    }
        return false;
}}

