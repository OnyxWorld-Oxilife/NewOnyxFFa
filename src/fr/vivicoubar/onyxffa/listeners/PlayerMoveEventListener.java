package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener {

    OnyxFFaMain main = OnyxFFaMain.getInstance();

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent moveEvent) {
        Player player = moveEvent.getPlayer();
        FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(main, player);
        moveEvent.setCancelled(fFaPlayer.isFrozen());
    }
}
