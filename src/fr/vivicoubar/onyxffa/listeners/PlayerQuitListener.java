package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        OnyxFFaMain main = OnyxFFaMain.getInstance();
        main.killStreak.removePlayer(player);
        main.wantedEvent.playerQuitWanted(main.getfFaPlayerManager().getFFaPlayer(main, player));
        if(main.wantedEvent.isTarget(main.getfFaPlayerManager().getFFaPlayer(main, player))){
            main.wantedEvent.setRandomTarget();
        }
        quitEvent.setQuitMessage(null);
    }

}
