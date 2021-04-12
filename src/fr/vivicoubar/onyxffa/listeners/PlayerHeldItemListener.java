package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerHeldItemListener implements Listener {

    OnyxFFaMain main = OnyxFFaMain.getInstance();

    @EventHandler
    public void onChangeHeldItemSlot(PlayerItemHeldEvent itemHeldEvent) {
        Player player = itemHeldEvent.getPlayer();
        if (main.fishingPlayers.isFishing(player) && main.fishingPlayers.isHookSpawned(player)) {
            itemHeldEvent.setCancelled(true);
        } else {
            main.fishingPlayers.removePlayer(player);
        }
    }

}
