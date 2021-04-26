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
    private final List<FFaPlayer> modoInVanish = new ArrayList<>();
    public CommandVanish(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            FFaPlayer modo = main.getFFaPlayerManager().getFFaPlayer(main, ((Player) sender).getPlayer());
            if(modo.getState() == FFaPlayerStates.WAITING){
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(modo.getUniqueID() != player.getUniqueId() && !player.hasPermission("NewOnyxFFa.spec.see")) {
                        player.hidePlayer(main, modo.getPlayer());
                    }
                }
                modo.setState(FFaPlayerStates.MODO);
                modoInVanish.add(modo);
                modo.getPlayer().teleport(middle);
                modo.getPlayer().setGameMode(GameMode.SPECTATOR);
                modo.getPlayer().sendMessage("§cVous êtes invisible!");
                return true;
            }else{
                modo.setState(FFaPlayerStates.WAITING);
                modo.sendToSpawn();
                modoInVanish.remove(modo);
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.showPlayer(main, modo.getPlayer());
                }
                modo.getPlayer().sendMessage("§cVous êtes de nouveau visible!");
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHideVanishedModo(PlayerJoinEvent playerJoinEvent){
        for(FFaPlayer modo : modoInVanish){
            if(!playerJoinEvent.getPlayer().hasPermission("NewOnyxFFa.spec.see")) {
                playerJoinEvent.getPlayer().hidePlayer(main, modo.getPlayer());
            }
        }
        if(playerJoinEvent.getPlayer().hasPermission("NewOnyxFFa.vanish.use")){
            FFaPlayer modo = main.getFFaPlayerManager().getFFaPlayer(main ,playerJoinEvent.getPlayer());
            modoInVanish.add(modo);
            for(Player player : Bukkit.getOnlinePlayers()){
                if(!player.hasPermission("NewOnyxFFa.spec.see")) {
                    player.hidePlayer(main, modo.getPlayer());
                }
            }
            modo.setState(FFaPlayerStates.MODO);
            modo.getPlayer().setGameMode(GameMode.SPECTATOR);
            modo.getPlayer().sendMessage("§cVous êtes invisible!");
            modo.getPlayer().teleport(middle);

        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuitVanishedModo(PlayerQuitEvent playerQuitEvent){
        modoInVanish.remove(main.getFFaPlayerManager().getFFaPlayer(main,playerQuitEvent.getPlayer()));
    }
}
