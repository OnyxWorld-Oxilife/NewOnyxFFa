package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.duels.Duel;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        OnyxFFaMain main = OnyxFFaMain.getInstance();
        main.wantedEvent.playerQuitOnyxEvent(main.getFFaPlayerManager().getFFaPlayer(main, player));
        if(main.wantedEvent.isTarget(main.getFFaPlayerManager().getFFaPlayer(main, player))){
            main.wantedEvent.setRandomTarget();
        }
        FFaPlayer left = main.getFFaPlayerManager().getFFaPlayer(main, player);
        Duel duel = main.getDuelManager().getDuelByPlayer(left);
        if (duel != null) {
            duel.loseDuel(left);
        }
        quitEvent.setQuitMessage(null);
    }

}
