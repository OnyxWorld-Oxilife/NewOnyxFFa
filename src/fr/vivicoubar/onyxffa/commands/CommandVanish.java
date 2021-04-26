package fr.vivicoubar.onyxffa.commands;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandVanish implements Listener, CommandExecutor {
    private final OnyxFFaMain main;
    private final Location middle = new Location(Bukkit.getWorld("world"), 738, 48, 625);
    public CommandVanish(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            // Command = vanish
            if(label.equalsIgnoreCase("vanish")){
                FFaPlayer modo = main.getFFaPlayerManager().getFFaPlayer(main, (Player) sender);
                if(modo.isVisible()){
                    modo.getPlayer().setGameMode(GameMode.SPECTATOR);
                    modo.getPlayer().sendMessage("§cVous êtes invisible!");
                    modo.getPlayer().teleport(middle);
                }else{
                    modo.getPlayer().sendMessage("§cVous êtes de nouveau visible...");
                }
                modo.setVanished(modo.isVisible());
                return true;
                // Command = spec

            }if(label.equalsIgnoreCase("spec")){
                FFaPlayer modo = main.getFFaPlayerManager().getFFaPlayer(main, (Player) sender);
                if(args.length > 0 ){
                    if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        if(modo.isVisible()){
                            modo.getPlayer().setGameMode(GameMode.SPECTATOR);
                            modo.getPlayer().sendMessage("§cVous êtes invisible!");
                            modo.getPlayer().teleport(Bukkit.getPlayer(args[0]));
                        }else{
                            modo.getPlayer().sendMessage("§cVous êtes de nouveau visible...");
                        }
                        modo.setVanished(modo.isVisible());
                        return true;
                    }else{
                        if(modo.isVisible()){
                            return false;
                        }else{
                            modo.getPlayer().sendMessage("§cVous êtes de nouveau visible...");
                            modo.setVanished(modo.isVisible());
                            return true;
                        }
                    }
                }else {
                    return false;
                }
            }


        }
        return false;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHideVanishedModo(PlayerJoinEvent playerJoinEvent){
        for(Player modo : Bukkit.getOnlinePlayers()){
            if(!playerJoinEvent.getPlayer().hasPermission("NewOnyxFFa.vanish.see") && !main.getFFaPlayerManager().getFFaPlayer(main, modo).isVisible()) {
                playerJoinEvent.getPlayer().hidePlayer(main, modo);
            }
        }
        if(playerJoinEvent.getPlayer().hasPermission("NewOnyxFFa.vanish.use")){
            FFaPlayer modo = main.getFFaPlayerManager().getFFaPlayer(main ,playerJoinEvent.getPlayer());
            modo.setVanished(true);
            modo.getPlayer().setGameMode(GameMode.SPECTATOR);
            modo.getPlayer().sendMessage("§cVous êtes invisible!");
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuitVanishedModo(PlayerQuitEvent playerQuitEvent){
        FFaPlayer modo = main.getFFaPlayerManager().getFFaPlayer(main,playerQuitEvent.getPlayer());;
        modo.setVanished(false);
    }
}
