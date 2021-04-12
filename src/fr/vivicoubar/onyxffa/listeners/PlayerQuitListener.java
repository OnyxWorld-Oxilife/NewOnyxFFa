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
        OnyxFFaMain.getInstance().killStreak.removePlayer(player);
        quitEvent.setQuitMessage(null);
    }

}
