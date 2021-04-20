package fr.vivicoubar.onyxffa.events.sumo;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.events.EventState;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SumoListener implements Listener {

    private final OnyxFFaMain main = OnyxFFaMain.getInstance();

    @EventHandler
    public void onSumoFall(PlayerMoveEvent sumoFallEvent){
        if(main.getFFaPlayerManager().getFFaPlayer(main, sumoFallEvent.getPlayer()).getState() == FFaPlayerStates.SUMO && main.sumoEvent.getState() == EventState.PLAYING) {
            FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main , sumoFallEvent.getPlayer());
            Location playerLocation = fFaPlayer.getPlayer().getLocation();
            Location footsLocation = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY() - 1, playerLocation.getBlockZ());
            if(playerLocation.getBlock().getType() == Material.STATIONARY_WATER|| footsLocation.getBlock().getType() == Material.STATIONARY_WATER ){
                main.sumoEvent.eliminatePlayer(fFaPlayer);
            }
        }
    }
    @EventHandler
    public void onDamageSumo(EntityDamageByEntityEvent entityDamageByEntityEvent){
        if(main.sumoEvent.getState() == EventState.PLAYING){
            if(entityDamageByEntityEvent.getEntity() instanceof Player && entityDamageByEntityEvent.getDamager() instanceof Player){
                FFaPlayer entity = main.getFFaPlayerManager().getFFaPlayer(main, (Player) entityDamageByEntityEvent.getEntity());
                FFaPlayer damager = main.getFFaPlayerManager().getFFaPlayer(main, (Player) entityDamageByEntityEvent.getDamager());
                if(entity.getState() == FFaPlayerStates.SUMO && damager.getState() == FFaPlayerStates.SUMO){
                    entityDamageByEntityEvent.setDamage(0);
                }
            }
        }
    }

}
