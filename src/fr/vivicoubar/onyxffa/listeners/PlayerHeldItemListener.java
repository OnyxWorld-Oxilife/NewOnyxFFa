package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerHeldItemListener implements Listener {

    OnyxFFaMain main = OnyxFFaMain.getInstance();

    @EventHandler
    public void onChangeHeldItemSlot(PlayerItemHeldEvent itemHeldEvent) {
        FFaPlayer fFaPlayer = main.getfFaPlayerManager().getFFaPlayer(main, itemHeldEvent.getPlayer());
        if (fFaPlayer.isFishing() && fFaPlayer.getFishHook().isValid()) {
            itemHeldEvent.setCancelled(true);
        } else {
            fFaPlayer.setFishing(false);
            fFaPlayer.setFishHook(null);
        }
    }

}
