package fr.vivicoubar.onyxffa.listeners;

import fr.vivicoubar.onyxffa.FFaPlayerStates;
import fr.vivicoubar.onyxffa.OnyxFFaMain;
import fr.vivicoubar.onyxffa.utils.FFaPlayer;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class ItemListener implements Listener {
    private OnyxFFaMain main;

    public ItemListener(OnyxFFaMain onyxFFaMain) {
        this.main = onyxFFaMain;
    }

    @EventHandler
    private void onClickFFaItem(PlayerInteractEvent interactEvent) {
        if (interactEvent.getPlayer().getGameMode() == GameMode.ADVENTURE && !main.getFFaPlayerManager().getFFaPlayer(main ,interactEvent.getPlayer()).isInArena()) {
            FFaPlayer fFaPlayer = main.getFFaPlayerManager().getFFaPlayer(this.main, interactEvent.getPlayer());
            if (interactEvent.getAction() != Action.RIGHT_CLICK_BLOCK && interactEvent.getAction() != Action.RIGHT_CLICK_AIR) {
                return;
            }
            if (interactEvent.getItem() == null) {
                return;
            }
            if (interactEvent.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(main.getConfigConfiguration().getString("NewOnyxFFa.Config.Menu.Item.Name")) && fFaPlayer.getState() == FFaPlayerStates.WAITING) {
                fFaPlayer.getPlayer().closeInventory();
                fFaPlayer.spawnInArena();
            }else{
                fFaPlayer.getPlayer().sendMessage("§c Désolé! Tu ne peux pas rejoindre l'arêne pour le moment!");
            }
        }
    }
}
