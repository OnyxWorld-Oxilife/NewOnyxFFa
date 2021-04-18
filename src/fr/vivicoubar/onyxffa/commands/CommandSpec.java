package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandSpec implements CommandExecutor, Listener {
    private final OnyxFFaMain main;
    public CommandSpec(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }
    private final List<FFaPlayer> modosInSpec = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            FFaPlayer modo = main.getfFaPlayerManager().getFFaPlayer(main, (Player) commandSender);
            if(args.length <1){
                return false;
            }else{
                if(Bukkit.getPlayer(args[0]) != null){
                    if(modo.getState() == FFaPlayerStates.SPECTATOR || modo.getState() == FFaPlayerStates.WAITING) {
                        modo.setState(FFaPlayerStates.MODO);
                        modo.getPlayer().setGameMode(GameMode.SPECTATOR);
                        modosInSpec.add(modo);
                        for(Player player : Bukkit.getOnlinePlayers()){
                            if(modo.getUniqueID() != player.getUniqueId() && !player.hasPermission("NewOnyxFFa.spec.see") && player.canSee(modo.getPlayer())){
                                player.hidePlayer(main, modo.getPlayer());
                            }
                        }
                        modo.getPlayer().teleport(Bukkit.getPlayer(args[0]));
                        modo.getPlayer().sendMessage("§cSpec Activé!");
                        return true;
                    }else if(modo.getState() == FFaPlayerStates.MODO && modo.getPlayer().getGameMode() == GameMode.SPECTATOR){
                        modo.setState(FFaPlayerStates.WAITING);
                        modo.sendToSpawn();
                        modosInSpec.remove(modo);
                        for(Player player : Bukkit.getOnlinePlayers()){
                            if(!player.hasPermission("NewOnyxFFa.spec.see") && !player.canSee(modo.getPlayer())){
                                player.showPlayer(main, modo.getPlayer());

                            }
                        }
                        modo.getPlayer().sendMessage("§cSpec Désactivé!");
                        return true;
                    }
                }else{
                    commandSender.sendMessage("§cErreur! Le joueur" + args[0] + "n'est pas connecté...");
                    return true;
                }

            }
        }
        return false;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void HidePlayerWhenJoinEvent(PlayerJoinEvent hidePlayerEvent){
        for(FFaPlayer modo : modosInSpec){
            hidePlayerEvent.getPlayer().hidePlayer(main, modo.getPlayer());
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuitVanishedModo(PlayerJoinEvent playerJoinEvent){
        modosInSpec.remove(main.getfFaPlayerManager().getFFaPlayer(main,playerJoinEvent.getPlayer()));
    }
}
