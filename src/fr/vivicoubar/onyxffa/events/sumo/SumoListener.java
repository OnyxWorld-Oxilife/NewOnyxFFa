package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SumoListener implements Listener {

    private final OnyxFFaMain main = OnyxFFaMain.getInstance();

    @EventHandler
    public void onSumoFall(PlayerMoveEvent sumoFallEvent){
        if(main.getFFaPlayerManager().getFFaPlayer(main, sumoFallEvent.getPlayer()).getState() == FFaPlayerStates.SUMO){
            FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main , sumoFallEvent.getPlayer());
            Location playerLocation = fFaPlayer.getPlayer().getLocation();
            Location footsLocation = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY() - 1, playerLocation.getBlockZ());
            if(playerLocation.getBlock().getType() == Material.WATER || footsLocation.getBlock().getType() == Material.WATER ){
                main.sumoEvent.eliminatePlayer(fFaPlayer);
            }
        }
        else{
            return;
        };
    }

}
