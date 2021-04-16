package fr.vivicoubar.onyxffa.duels;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DuelListener implements Listener {
    private OnyxFFaMain main;

    public DuelListener(OnyxFFaMain onyxFFaMain){
        this.main = onyxFFaMain;
    }

    @EventHandler
    public void BeKilledInDuelEvent(EntityDamageByEntityEvent beKilledinDuelEvent){
        if(beKilledinDuelEvent.getDamager() instanceof Player && beKilledinDuelEvent.getEntity() instanceof Player ){
            FFaPlayer victim = main.getfFaPlayerManager().getFFaPlayer(main,(Player) beKilledinDuelEvent.getEntity());
            FFaPlayer damager = main.getfFaPlayerManager().getFFaPlayer(main,(Player) beKilledinDuelEvent.getDamager());
            if(victim.getState() == FFaPlayerStates.DUEL && damager.getState() == FFaPlayerStates.DUEL){
                if(victim.getPlayer().getHealth() <= beKilledinDuelEvent.getFinalDamage()){
                    beKilledinDuelEvent.setCancelled(true);
                    Duel duel = main.getDuelManager().getDuelByPlayer(damager);
                    duel.winDuel(damager);
                    main.getDuelManager().removeDuel(duel);
                }
            }
        }
    }
}
